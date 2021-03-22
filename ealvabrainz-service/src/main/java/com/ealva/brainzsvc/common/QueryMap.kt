/*
 * Copyright (c) 2021  Eric A. Snell
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

package com.ealva.brainzsvc.common

import com.ealva.ealvabrainz.brainz.data.Include
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.joinToString
import com.ealva.ealvabrainz.common.AreaMbid
import com.ealva.ealvabrainz.common.ArtistMbid
import com.ealva.ealvabrainz.common.LabelMbid
import com.ealva.ealvabrainz.common.PlaceMbid
import com.ealva.ealvabrainz.common.RecordingMbid
import com.ealva.ealvabrainz.common.ReleaseGroupMbid
import com.ealva.ealvabrainz.common.ReleaseMbid
import com.ealva.ealvabrainz.common.TrackMbid
import com.ealva.ealvabrainz.common.WorkMbid
import com.ealva.ealvabrainz.lucene.BrainzMarker
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@Suppress("NOTHING_TO_INLINE")
@OptIn(ExperimentalContracts::class)
private inline fun <E> Set<E>?.neitherNullNorEmpty(): Boolean {
  contract {
    returns(true) implies (this@neitherNullNorEmpty != null)
  }
  return !isNullOrEmpty()
}

public typealias QueryMap = MutableMap<String, String>

@BrainzMarker
public inline fun buildQueryMap(builder: QueryMap.() -> Unit): QueryMap =
  mutableMapOf<String, String>().apply(builder)

public fun QueryMap.putItem(item: QueryMapItem): QueryMap = apply { item.put(this) }

public fun QueryMap.include(inc: Set<Include>?): QueryMap = apply {
  if (inc.neitherNullNorEmpty()) {
    this["inc"] = inc.joinToString()
  }
}

public fun QueryMap.types(typeSet: Set<Release.Type>?): QueryMap = apply {
  if (typeSet.neitherNullNorEmpty()) {
    this["type"] = typeSet.joinToString()
  }
}

public fun QueryMap.status(statusSet: Set<Release.Status>?): QueryMap = apply {
  if (statusSet.neitherNullNorEmpty()) {
    this["status"] = statusSet.joinToString()
  }
}

public fun QueryMap.limit(limit: Limit?): QueryMap = apply {
  limit?.let { this["limit"] = it.value.toString() }
}

public fun QueryMap.offset(offset: Offset?): QueryMap = apply {
  offset?.let { this["offset"] = it.value.toString() }
}

public fun QueryMap.area(areaId: AreaMbid): QueryMap = apply {
  this["area"] = areaId.value
}

public fun QueryMap.artist(artistId: ArtistMbid): QueryMap = apply {
  this["artist"] = artistId.value
}

public fun QueryMap.label(labelId: LabelMbid): QueryMap = apply {
  this["label"] = labelId.value
}

public fun QueryMap.place(placeId: PlaceMbid): QueryMap = apply {
  this["place"] = placeId.value
}

public fun QueryMap.recording(recordingId: RecordingMbid): QueryMap = apply {
  this["recording"] = recordingId.value
}

public fun QueryMap.release(releaseId: ReleaseMbid): QueryMap = apply {
  this["release"] = releaseId.value
}

public fun QueryMap.releaseGroup(releaseGroupId: ReleaseGroupMbid): QueryMap = apply {
  this["release-group"] = releaseGroupId.value
}

public fun QueryMap.track(trackMbid: TrackMbid): QueryMap = apply {
  this["track"] = trackMbid.value
}

public fun QueryMap.trackArtist(artistId: ArtistMbid): QueryMap = apply {
  this["track_artist"] = artistId.value
}

public fun QueryMap.work(workId: WorkMbid): QueryMap = apply {
  this["work"] = workId.value
}

public fun QueryMap.toc(tocParam: TocParam?): QueryMap = apply {
  tocParam?.let { this["toc"] = it.toString() }
}

public fun QueryMap.cdstubs(excludeCDStubs: Boolean): QueryMap = apply {
  if (excludeCDStubs) this["cdstubs"] = "no"
}

public fun QueryMap.mediaFormat(allMediumFormats: Boolean): QueryMap = apply {
  if (allMediumFormats) this["media-format"] = "all"
}

@JvmInline
public value class Limit(public val value: Int)

@JvmInline
public value class Offset(public val value: Int)
