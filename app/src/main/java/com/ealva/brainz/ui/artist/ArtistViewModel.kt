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
import com.ealva.ealvabrainz.brainz.data.Artist
import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.service.MusicBrainzResult
import com.ealva.ealvabrainz.service.MusicBrainzService
import kotlinx.coroutines.launch

interface ArtistViewModel {
  val artist: LiveData<Artist>
  val isBusy: LiveData<Boolean>
  val error: LiveData<MusicBrainzResult<Nothing>>

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
    require(modelClass.isAssignableFrom(ArtistSearchViewModelImpl::class.java))
    @Suppress("UNCHECKED_CAST")
    return ArtistSearchViewModelImpl(brainz) as T
  }
}

internal class ArtistViewModelImpl(
  private val brainz: MusicBrainzService
) : ViewModel(), ArtistViewModel {
  override val artist: MutableLiveData<Artist> = MutableLiveData()
  override val isBusy: MutableLiveData<Boolean> = MutableLiveData(false)
  override val error: MutableLiveData<MusicBrainzResult<Nothing>> = MutableLiveData()

  private data class QueryData(
    val query: String
  )

  override fun lookupArtist(mbid: ArtistMbid) {
    viewModelScope.launch {
      when(val result = brainz.lookupArtist(mbid, listOf(Artist.Subquery.ReleaseGroups))) {
        is MusicBrainzResult.Success -> handleArtist(result.value)
        is MusicBrainzResult.Error -> error.postValue(result)
        is MusicBrainzResult.Exceptional -> error.postValue(result)
      }
    }
  }

  private fun handleArtist(artist: Artist) {

  }
}
