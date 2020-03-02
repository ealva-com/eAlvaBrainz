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

package com.ealva.brainzapp.ui.artist

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ealva.brainzapp.data.Country
import com.ealva.brainzapp.data.DisplayGenre
import com.ealva.brainzapp.data.Isni
import com.ealva.brainzapp.data.Isni.Companion.NullIsni
import com.ealva.brainzapp.data.ReleaseGroupType
import com.ealva.brainzapp.data.StarRating
import com.ealva.brainzapp.data.toCountry
import com.ealva.brainzapp.data.toDisplayGenres
import com.ealva.brainzapp.data.toIsni
import com.ealva.brainzapp.data.toPrimaryReleaseGroupType
import com.ealva.brainzapp.data.toSecondaryReleaseGroupList
import com.ealva.brainzapp.data.toStarRating
import com.ealva.brainzsvc.common.ArtistName
import com.ealva.brainzsvc.common.ReleaseGroupName
import com.ealva.brainzsvc.common.toArtistName
import com.ealva.brainzsvc.common.toReleaseGroupName
import com.ealva.brainzsvc.service.MusicBrainzResult.Success
import com.ealva.brainzsvc.service.MusicBrainzResult.Unsuccessful
import com.ealva.brainzsvc.service.MusicBrainzService
import com.ealva.ealvabrainz.brainz.data.Artist
import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.brainz.data.ArtistType
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupMbid
import com.ealva.ealvabrainz.brainz.data.toArtistMbid
import com.ealva.ealvabrainz.brainz.data.toArtistType
import com.ealva.ealvabrainz.brainz.data.toJson
import com.ealva.ealvabrainz.brainz.data.toReleaseGroupMbid
import com.ealva.ealvabrainz.common.ensureExhaustive
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

data class DisplayArtist(
  val mbid: ArtistMbid,
  val type: ArtistType,
  val name: ArtistName,
  val country: Country,
  val area: String,
  val lifespanBegin: String,
  val startArea: String,
  val lifespanEnded: Boolean,
  val lifespanEnd: String,
  val endArea: String,
  val isni: Isni,
  val rating: StarRating,
  val ratingVotes: Int,
  val genres: List<DisplayGenre>
)

data class DisplayReleaseGroup(
  val mbid: ReleaseGroupMbid,
  val name: ReleaseGroupName,
  val type: ReleaseGroupType.Primary,
  val secondaryTypes: List<ReleaseGroupType.Secondary>,
  val rating: StarRating,
  val ratingVotes: Int,
  val date: String
) {
  companion object {
    val NullDisplayReleaseGroup = DisplayReleaseGroup(
      "".toReleaseGroupMbid(),
      "".toReleaseGroupName(),
      ReleaseGroupType.Primary.Unknown,
      emptyList(),
      0.0F.toStarRating(),
      0,
      ""
      )
  }
}

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

private const val sorterForEmpty = "aaaa"

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
      .sortedBy {
        val date = it.firstReleaseDate
        if (date.isBlank()) {
          "${sorterForEmpty}${it.title}"
        } else date
      }
      .map { brainzGroup ->
        DisplayReleaseGroup(
          brainzGroup.id.toReleaseGroupMbid(),
          brainzGroup.title.toReleaseGroupName(),
          brainzGroup.primaryType.toPrimaryReleaseGroupType(),
          brainzGroup.secondaryTypes.toSecondaryReleaseGroupList(),
          brainzGroup.rating.value.toStarRating(),
          brainzGroup.rating.votesCount,
          brainzGroup.firstReleaseDate
        )
      }
      .toList()
    releaseGroups.postValue(list)
  }

  private suspend fun doArtistLookup(mbid: ArtistMbid): Boolean =
    when (val result = brainz.lookupArtist(mbid, Artist.Misc.all)) {
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
    Timber.e(brainzArtist.toJson())
    artist.postValue(
      DisplayArtist(
        mbid = resultMbid,
        type = brainzArtist.type.toArtistType(),
        name = brainzArtist.name.toArtistName(),
        country = brainzArtist.country.toCountry(),
        area = brainzArtist.area.name,
        lifespanBegin = brainzArtist.lifeSpan.begin,
        startArea = brainzArtist.beginArea.name,
        lifespanEnded = brainzArtist.lifeSpan.ended,
        lifespanEnd = brainzArtist.lifeSpan.end,
        endArea = brainzArtist.endArea.name,
        isni = brainzArtist.isnis.firstOrNull()?.toIsni() ?: NullIsni,
        rating = brainzArtist.rating.value.toStarRating(),
        ratingVotes = brainzArtist.rating.votesCount,
        genres = brainzArtist.genres.toDisplayGenres()
      )
    )
  }

}
