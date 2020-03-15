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
import com.ealva.brainzapp.data.toCountry
import com.ealva.brainzsvc.common.ArtistName
import com.ealva.brainzsvc.common.toArtistName
import com.ealva.brainzsvc.service.MusicBrainzResult.Success
import com.ealva.brainzsvc.service.MusicBrainzResult.Unsuccessful
import com.ealva.brainzsvc.service.MusicBrainzService
import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.brainz.data.ArtistType
import com.ealva.ealvabrainz.brainz.data.artistType
import com.ealva.ealvabrainz.brainz.data.toArtistMbid
import com.ealva.ealvabrainz.brainz.data.toArtistType
import com.ealva.ealvabrainz.common.ensureExhaustive
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.launch
import timber.log.Timber

data class ArtistSearchResult(
  val mbid: ArtistMbid,
  val type: ArtistType,
  val name: ArtistName,
  val country: Country,
  val disambiguation: String,
  val score: Int
) {
  companion object {
    val NullArtistSearchResult = ArtistSearchResult(
      "".toArtistMbid(), "".toArtistType(), "".toArtistName(), "".toCountry(), "", 0)
  }
}

interface ArtistSearchViewModel {
  val itemList: LiveData<List<ArtistSearchResult>>
  val lastQuery: LiveData<String>
  val isBusy: LiveData<Boolean>
  val unsuccessful: LiveData<Unsuccessful>

  fun findArtist(query: String)

//  fun isEmptyAndNotBusy(): Boolean {
//    val list = itemList.value
//    return list.isNullOrEmpty() || isBusy.value != true
//  }

}

fun Fragment.getArtistSearchViewModel(brainz: MusicBrainzService): ArtistSearchViewModel {
  return ViewModelProvider(
    this,
    ArtistSearchViewModelFactory(brainz)
  )[ArtistSearchViewModelImpl::class.java]
}

private class ArtistSearchViewModelFactory(
  private val brainz: MusicBrainzService
) : ViewModelProvider.Factory {
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    require(modelClass.isAssignableFrom(ArtistSearchViewModelImpl::class.java))
    @Suppress("UNCHECKED_CAST")
    return ArtistSearchViewModelImpl(brainz) as T
  }
}

internal class ArtistSearchViewModelImpl(
  private val brainz: MusicBrainzService
) : ViewModel(), ArtistSearchViewModel {
  override val itemList: MutableLiveData<List<ArtistSearchResult>> = MutableLiveData(emptyList())
  override val lastQuery: MutableLiveData<String> = MutableLiveData("")
  override val isBusy: MutableLiveData<Boolean> = MutableLiveData(false)
  override val unsuccessful: MutableLiveData<Unsuccessful> = MutableLiveData()

  private data class QueryData(
    val query: String
  )

  private var lastQueryData: QueryData? = null
  private var loadJob: Job? = null

  @OptIn(ObsoleteCoroutinesApi::class)
  private val actor = viewModelScope.actor<QueryData>(capacity = Channel.CONFLATED) {
    for ((query) in channel) {
      handleQuery(query)
    }
  }

  private suspend fun handleQuery(query: String) {
    viewModelScope.launch(Dispatchers.Default) {
      unsuccessful.postValue(Unsuccessful.None)
      lastQuery.postValue(query)
      val theJob = coroutineContext[Job] as Job
      loadJob = theJob
      busy(isBusy) {
        when (val result = brainz.brainz { it.findArtist(query) }) {
          is Success -> {
            val list = result.value.artists.map { artist ->
              ArtistSearchResult(
                artist.id.toArtistMbid(),
                artist.artistType,
                artist.name.toArtistName(),
                artist.country.toCountry(),
                artist.disambiguation,
                artist.score
              )
            }
            itemList.postValue(list)
          }
          is Unsuccessful -> unsuccessful.postValue(result)
        }.ensureExhaustive
      }
      loadJob = null
    }
  }

  override fun findArtist(query: String) {
    offerLoadData(QueryData(query.trim()))
  }

  private fun offerLoadData(queryData: QueryData) {
    lastQueryData = queryData
    try {
      loadJob?.let { job ->
        job.cancel(CancellationException("New query supersedes any current job"))
        loadJob = null
      }
      actor.offer(queryData)
    } catch (e: Exception) {
      if (e !is CancellationException) Timber.e(e)
    }
  }

}

inline fun <T> busy(busy: MutableLiveData<Boolean>, block: () -> T): T {
  busy.postValue(true)
  try {
    return block()
  } finally {
    busy.postValue(false)
  }
}

