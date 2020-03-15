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
import com.ealva.brainzapp.data.GenreItem
import com.ealva.brainzapp.data.Isni
import com.ealva.brainzapp.data.Isni.Companion.NullIsni
import com.ealva.brainzapp.data.StarRating
import com.ealva.brainzapp.data.toCountry
import com.ealva.brainzapp.data.toGenreItems
import com.ealva.brainzapp.data.toIsni
import com.ealva.brainzapp.data.toPrimaryReleaseGroupType
import com.ealva.brainzapp.data.toSecondaryReleaseGroupList
import com.ealva.brainzapp.data.toStarRating
import com.ealva.brainzsvc.common.ArtistName
import com.ealva.brainzsvc.common.toArtistName
import com.ealva.brainzsvc.common.toLabelName
import com.ealva.brainzsvc.common.toReleaseGroupName
import com.ealva.brainzsvc.common.toReleaseName
import com.ealva.brainzsvc.service.MusicBrainzResult.Success
import com.ealva.brainzsvc.service.MusicBrainzResult.Unsuccessful
import com.ealva.brainzsvc.service.MusicBrainzResult.Unsuccessful.Exceptional
import com.ealva.brainzsvc.service.MusicBrainzService
import com.ealva.ealvabrainz.brainz.data.Artist
import com.ealva.ealvabrainz.brainz.data.ArtistCredit
import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.brainz.data.ArtistType
import com.ealva.ealvabrainz.brainz.data.LabelInfo
import com.ealva.ealvabrainz.brainz.data.Medium
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.ReleaseEvent
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupMbid
import com.ealva.ealvabrainz.brainz.data.ReleaseMbid
import com.ealva.ealvabrainz.brainz.data.artistType
import com.ealva.ealvabrainz.brainz.data.isNullObject
import com.ealva.ealvabrainz.brainz.data.isValid
import com.ealva.ealvabrainz.brainz.data.mbid
import com.ealva.ealvabrainz.brainz.data.toArtistMbid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class DisplayArtist(
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
  @Suppress("unused") val ratingVotes: Int,
  val genres: List<GenreItem>
)

interface ArtistViewModel {
  val artist: LiveData<DisplayArtist>
  val releaseGroups: LiveData<List<ReleaseGroupItem>>
  val releases: LiveData<List<ReleaseItem>>
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

private fun List<Medium>.toFormatStrings(): Pair<String, String> {
  val map = mutableMapOf<String, Int>()
  val tracks = buildString {
    append("(")
    this@toFormatStrings.forEachIndexed { index, medium ->
      var count = map[medium.format] ?: 0
      map[medium.format] = ++count
      if (index > 0) append(" + ")
      append(medium.trackCount)
    }
    append(")")
  }

  return Pair(buildString {
    map.keys.forEachIndexed { index, format ->
      val count = map[format] ?: 0
      if (index > 0) append(" + ")
      if (count > 0) {
        if (count > 1) {
          append(count)
          append("x")
        }
        append(if (format.isNotBlank()) format else "(unknown)")
      }
    }
  }, tracks)
}

private val List<LabelInfo>.catalogNumber: String
  get() {
    forEach { labelnfo ->
      if (labelnfo.catalogNumber.isNotBlank()) return labelnfo.catalogNumber
    }
    return ""
  }

private val List<ReleaseEvent>.firstDate: String
  get() {
    forEach { event ->
      if (event.date.isNotBlank()) return event.date
    }
    return ""
  }

private const val sorterForEmpty = "aaaa"
inline fun <reified T> mutableDataEmptyList(): MutableLiveData<List<T>> {
  return MutableLiveData(emptyList())
}

/** Have seen more than 1600 for a single artist - 2048 should rarely need to be grown */
private const val RELEASE_HASHMAP_MAX_SIZE = 2048

internal class ArtistViewModelImpl(
  private val brainz: MusicBrainzService
) : ViewModel(), ArtistViewModel {
  override val artist: MutableLiveData<DisplayArtist> = MutableLiveData()
  override val releaseGroups: MutableLiveData<List<ReleaseGroupItem>> = mutableDataEmptyList()
  override val releases: MutableLiveData<List<ReleaseItem>> = mutableDataEmptyList()
  override val isBusy: MutableLiveData<Boolean> = MutableLiveData(false)
  override val unsuccessful: MutableLiveData<Unsuccessful> = MutableLiveData()

  @OptIn(ExperimentalCoroutinesApi::class)
  override fun lookupArtist(mbid: ArtistMbid) {
    val currentArtist = artist.value
    if (currentArtist == null || currentArtist.mbid != mbid) {
      viewModelScope.launch(Dispatchers.Default) {
        unsuccessful.postValue(Unsuccessful.None)
        val groupToReleaseMap = mutableMapOf<ReleaseGroupMbid, MutableList<Release>>()
        val releaseMap = mutableMapOf<ReleaseMbid, ReleaseItem>()
        val displayMap = HashMap<ReleaseGroupMbid, ReleaseGroupItem>(RELEASE_HASHMAP_MAX_SIZE)
        busy(isBusy) {
          if (doArtistLookup(mbid)) {
            brainz.artistReleases(
                mbid,
                listOf(
                  Release.Browse.ArtistCredits,
                  Release.Browse.ReleaseGroups,
                  Release.Browse.Ratings,
                  Release.Browse.Media,
                  Release.Browse.Labels
                ),
                status = listOf(Release.Status.Official)
              )
              .catch { ex ->
                Timber.e(ex)
                unsuccessful.postValue(Exceptional.make("Artist release flow", ex))
              }
              .collect { handleReleases(it, groupToReleaseMap, displayMap, releaseMap) }
          }
        }
      }
    }
  }

  private fun handleReleases(
    releaseList: List<Release>,
    groupToReleaseMap: MutableMap<ReleaseGroupMbid, MutableList<Release>>,
    displayMap: MutableMap<ReleaseGroupMbid, ReleaseGroupItem>,
    releaseMap: MutableMap<ReleaseMbid, ReleaseItem>
  ) {
    val newGroupMap = mutableMapOf<ReleaseGroupMbid, ReleaseGroup>()
    releaseList.forEach { release ->
      val (format, tracks) = release.media.toFormatStrings()
      val releaseMbid = release.mbid
      if (!releaseMap.containsKey(releaseMbid)) {
        releaseMap[releaseMbid] = ReleaseItem.make(
          releaseMbid,
          release.title.toReleaseName(),
          format,
          tracks,
          release.country,
          release.releaseEvents.firstDate,
          release.labelInfo.toLabelItems(),
          release.labelInfo.catalogNumber,
          release.barcode,
          release.artistCredit.toCreditItems()
        )
      }
      val group = release.releaseGroup
      val mbid = group.mbid
      if (mbid.isValid()) {
        newGroupMap[mbid] = group
        val groupReleases =
          if (groupToReleaseMap.containsKey(mbid)) groupToReleaseMap[mbid]!! else mutableListOf()
        groupToReleaseMap[mbid] = groupReleases.apply { add(release) }
      }
    }
    newGroupMap.entries.forEach { entry ->
      val mbid = entry.value.mbid
      if (!displayMap.containsKey(entry.key)) {
        entry.value.run {
          displayMap[entry.key] = ReleaseGroupItem.make(
            mbid,
            title.toReleaseGroupName(),
            primaryType.toPrimaryReleaseGroupType(),
            secondaryTypes.toSecondaryReleaseGroupList(),
            rating.value.toStarRating(),
            rating.votesCount,
            firstReleaseDate,
            groupToReleaseMap[mbid]?.size ?: 0
          )
        }
      } else {
        displayMap[entry.key]?.releaseCount = groupToReleaseMap[mbid]?.size ?: 0
      }
    }
    val list = displayMap.values.asSequence()
      .sortedBy {
        val date = it.date
        if (date.isBlank()) {
          "${sorterForEmpty}${it.name.value}"
        } else date
      }
      .toList()
    releaseGroups.postValue(list)

    releases.postValue(
      releaseMap.values.asSequence()
        .sortedBy {
          val date = it.date
          if (date.isBlank()) {
            "${sorterForEmpty}${it.name.value}"
          } else date
        }
        .toList()
    )
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
    artist.postValue(
      DisplayArtist(
        mbid = resultMbid,
        type = brainzArtist.artistType,
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
        genres = brainzArtist.genres.toGenreItems()
      )
    )
  }
}

private fun List<ArtistCredit>.toCreditItems(): List<CreditItem> = map {
  CreditItem(it.artist.mbid, it.artist.name.toArtistName(), it.joinphrase)
}.toList()

private fun List<LabelInfo>.toLabelItems(): MutableList<LabelItem> = asSequence()
  .filterNot { it.label.isNullObject }
  .distinctBy { it.label.id }
  .mapTo(ArrayList(size)) { labelInfo ->
    LabelItem(
      labelInfo.label.mbid,
      labelInfo.label.name.toLabelName(),
      labelInfo.label.disambiguation
    )
  }
