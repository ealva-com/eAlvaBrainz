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

import com.ealva.ealvabrainz.common.AreaMbid
import com.ealva.ealvabrainz.common.ArtistMbid
import com.ealva.ealvabrainz.common.LabelMbid
import com.ealva.ealvabrainz.common.PlaceMbid
import com.ealva.ealvabrainz.common.RecordingMbid
import com.ealva.ealvabrainz.common.ReleaseGroupMbid
import com.ealva.ealvabrainz.common.ReleaseMbid
import com.ealva.ealvabrainz.common.TrackMbid
import com.ealva.ealvabrainz.common.WorkMbid

public interface QueryMapItem {
  public fun put(map: QueryMap): QueryMap
}

public open class AreaQueryMapItem(private val areaMbid: AreaMbid) : QueryMapItem {
  override fun put(map: QueryMap): QueryMap = map.area(areaMbid)
}

public open class ArtistQueryMapItem(private val artistMbid: ArtistMbid) : QueryMapItem {
  override fun put(map: QueryMap): QueryMap = map.artist(artistMbid)
}

public open class LabelQueryMapItem(private val labelMbid: LabelMbid) : QueryMapItem {
  override fun put(map: QueryMap): QueryMap = map.label(labelMbid)
}

public open class PlaceQueryMapItem(private val placeMbid: PlaceMbid) : QueryMapItem {
  override fun put(map: QueryMap): QueryMap = map.place(placeMbid)
}

public open class RecordingQueryMapItem(private val recordingMbid: RecordingMbid) : QueryMapItem {
  override fun put(map: QueryMap): QueryMap = map.recording(recordingMbid)
}

public open class ReleaseQueryMapItem(private val releaseMbid: ReleaseMbid) : QueryMapItem {
  override fun put(map: QueryMap): QueryMap = map.release(releaseMbid)
}

public open class ReleaseGroupQueryMapItem(
  private val releaseGroupMbid: ReleaseGroupMbid
) : QueryMapItem {
  override fun put(map: QueryMap): QueryMap = map.releaseGroup(releaseGroupMbid)
}

public open class TrackQueryMapItem(private val trackMbid: TrackMbid) : QueryMapItem {
  override fun put(map: QueryMap): QueryMap = map.track(trackMbid)
}

public open class WorkQueryMapItem(private val workMbid: WorkMbid) : QueryMapItem {
  override fun put(map: QueryMap): QueryMap = map.work(workMbid)
}
