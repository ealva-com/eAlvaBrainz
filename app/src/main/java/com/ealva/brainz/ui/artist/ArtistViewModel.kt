/*
 * Copyright (c) 2020  Eric A. Snell
 *
 * This file is part of eAlvaBrainz
 *
 * eAlvaBrainz is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 *  the License, or (at your option) any later version.
 *
 * eAlvaBrainz is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with eAlvaBrainz.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package com.ealva.brainz.ui.artist

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ealva.brainz.data.Country
import com.ealva.brainz.data.toCountry
import com.ealva.ealvabrainz.brainz.data.Artist
import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.brainz.data.ArtistType
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupMbid
import com.ealva.ealvabrainz.brainz.data.toArtistMbid
import com.ealva.ealvabrainz.brainz.data.toArtistType
import com.ealva.ealvabrainz.brainz.data.toReleaseGroupMbid
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.common.ensureExhaustive
import com.ealva.ealvabrainz.common.toArtistName
import com.ealva.ealvabrainz.service.MusicBrainzResult.Success
import com.ealva.ealvabrainz.service.MusicBrainzResult.Unsuccessful
import com.ealva.ealvabrainz.service.MusicBrainzService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

data class DisplayArtist(
  val mbid: ArtistMbid,
  val type: ArtistType,
  val name: ArtistName,
  val country: Country
)

data class DisplayReleaseGroup(
  val mbid: ReleaseGroupMbid,
  val name: String,
  val type: String,
  /** rating value 0.0..5.0 inclusive */
  val rating: Float
)

interface ArtistViewModel {
  val artist: LiveData<DisplayArtist>
  val releaseGroups: LiveData<List<DisplayReleaseGroup>>
  val isBusy: LiveData<Boolean>
  val unsuccessful: LiveData<Unsuccessful>

  fun lookupArtist(mbid: ArtistMbid)
}

fun Fragment.getArtistViewModel(brainz: MusicBrainzService): ArtistViewModel {
  return ViewModelProvider(
    this,
    ArtistViewModelFactory(brainz)
  )[ArtistViewModelImpl::class.java]
}

private class ArtistViewModelFactory(
  private val brainz: MusicBrainzService
) : ViewModelProvider.Factory {
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    require(modelClass.isAssignableFrom(ArtistViewModelImpl::class.java))
    @Suppress("UNCHECKED_CAST")
    return ArtistViewModelImpl(brainz) as T
  }
}

internal class ArtistViewModelImpl(
  private val brainz: MusicBrainzService
) : ViewModel(), ArtistViewModel {
  override val artist: MutableLiveData<DisplayArtist> = MutableLiveData()
  override val releaseGroups: MutableLiveData<List<DisplayReleaseGroup>> =
    MutableLiveData(emptyList())
  override val isBusy: MutableLiveData<Boolean> = MutableLiveData(false)
  override val unsuccessful: MutableLiveData<Unsuccessful> = MutableLiveData()

  override fun lookupArtist(mbid: ArtistMbid) {
    viewModelScope.launch(Dispatchers.Default) {
      unsuccessful.postValue(Unsuccessful.None)
      busy(isBusy) {
        if (doArtistLookup(mbid)) {
          when (val result =
            brainz.getArtistReleaseGroups(mbid, ReleaseGroup.Browse.values().toList())) {
            is Success -> handleReleaseGroups(result.value)
            is Unsuccessful -> unsuccessful.postValue(result)
          }.ensureExhaustive
        }
      }
    }
  }

  private fun handleReleaseGroups(groupList: List<ReleaseGroup>) {
    val list = groupList.asSequence()
      .sortedBy { it.firstReleaseDate }
      .map { brainzGroup ->
        DisplayReleaseGroup(
          brainzGroup.id.toReleaseGroupMbid(),
          brainzGroup.title,
          brainzGroup.primaryType,
          brainzGroup.rating.value
        )
      }
      .toList()
    releaseGroups.postValue(list)
  }

  private suspend fun doArtistLookup(mbid: ArtistMbid): Boolean =
    when (val result = brainz.lookupArtist(mbid)) {
      is Success -> {
        handleArtist(result.value, mbid)
        true
      }
      is Unsuccessful -> {
        unsuccessful.postValue(result)
        false
      }
    }

  private fun handleArtist(
    brainzArtist: Artist,
    searchMbid: ArtistMbid
  ) = brainzArtist.run {
    val resultMbid = id.toArtistMbid()
    if (resultMbid != searchMbid) Timber.e("Result %s != search %s", resultMbid, searchMbid)
    artist.postValue(
      DisplayArtist(
        resultMbid,
        type.toArtistType(),
        name.toArtistName(),
        country.toCountry()
      )
    )
  }
}
