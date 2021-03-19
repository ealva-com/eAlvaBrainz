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

package com.ealva.brainzsvc.service.browse

import com.ealva.brainzsvc.common.AreaQueryMapItem
import com.ealva.brainzsvc.common.ArtistQueryMapItem
import com.ealva.brainzsvc.common.LabelQueryMapItem
import com.ealva.brainzsvc.common.QueryMap
import com.ealva.brainzsvc.common.QueryMapItem
import com.ealva.brainzsvc.common.RecordingQueryMapItem
import com.ealva.brainzsvc.common.ReleaseGroupQueryMapItem
import com.ealva.brainzsvc.common.TrackQueryMapItem
import com.ealva.brainzsvc.service.browse.ReleaseBrowse.BrowseOn
import com.ealva.ealvabrainz.brainz.MusicBrainz
import com.ealva.ealvabrainz.brainz.data.BrowseReleaseList
import com.ealva.ealvabrainz.brainz.data.Include
import com.ealva.ealvabrainz.brainz.data.Release.Browse
import com.ealva.ealvabrainz.common.AreaMbid
import com.ealva.ealvabrainz.common.ArtistMbid
import com.ealva.ealvabrainz.common.LabelMbid
import com.ealva.ealvabrainz.common.RecordingMbid
import com.ealva.ealvabrainz.common.ReleaseGroupMbid
import com.ealva.ealvabrainz.common.TrackMbid
import retrofit2.Response

/**
 * Builds the release browsing call based on [BrowseOn] type, [include], [relationships], [types],
 * and [status]
 */
public interface ReleaseBrowse : EntityBrowse<Browse> {
  /**
   * BrowseOn the entity related to a group of releases.
   */
  @Suppress("unused")
  public sealed interface BrowseOn : QueryMapItem {
    public class Area(mbid: AreaMbid) : BrowseOn, AreaQueryMapItem(mbid)
    public class Artist(mbid: ArtistMbid) : BrowseOn, ArtistQueryMapItem(mbid)
    public class Label(mbid: LabelMbid) : BrowseOn, LabelQueryMapItem(mbid)
    public class Recording(mbid: RecordingMbid) : BrowseOn, RecordingQueryMapItem(mbid)
    public class ReleaseGroup(mbid: ReleaseGroupMbid) : BrowseOn, ReleaseGroupQueryMapItem(mbid)
    public class Track(mbid: TrackMbid) : BrowseOn, TrackQueryMapItem(mbid)
    public class TrackArtist(mbid: ArtistMbid) : BrowseOn, ArtistQueryMapItem(mbid)
  }
}

internal class ReleaseBrowseOp(
  override val browseOn: BrowseOn
) : ReleaseBrowse, BaseEntityBrowse<Browse, BrowseReleaseList>() {
  override fun verifyIncludes(set: Set<Include>) {
    if (set.contains(Browse.Ratings))
      require(set.contains(Browse.ReleaseGroups)) {
        "If includes contains ${Browse.Ratings} it must also contain ${Browse.ReleaseGroups}"
      }
  }

  override suspend fun doExecute(
    musicBrainz: MusicBrainz,
    queryMap: QueryMap
  ): Response<BrowseReleaseList> = musicBrainz.browseReleases(queryMap)
}
