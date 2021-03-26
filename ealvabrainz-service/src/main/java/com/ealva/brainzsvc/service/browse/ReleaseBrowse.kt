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
import com.ealva.brainzsvc.common.CollectionQueryMapItem
import com.ealva.brainzsvc.common.LabelQueryMapItem
import com.ealva.brainzsvc.common.Limit
import com.ealva.brainzsvc.common.Offset
import com.ealva.brainzsvc.common.QueryMap
import com.ealva.brainzsvc.common.QueryMapItem
import com.ealva.brainzsvc.common.RecordingQueryMapItem
import com.ealva.brainzsvc.common.ReleaseGroupQueryMapItem
import com.ealva.brainzsvc.common.TrackArtistQueryMapItem
import com.ealva.brainzsvc.common.TrackQueryMapItem
import com.ealva.brainzsvc.service.browse.ReleaseBrowse.BrowseOn
import com.ealva.ealvabrainz.brainz.data.Inc
import com.ealva.ealvabrainz.brainz.data.Release.Browse
import com.ealva.ealvabrainz.common.AreaMbid
import com.ealva.ealvabrainz.common.ArtistMbid
import com.ealva.ealvabrainz.common.CollectionMbid
import com.ealva.ealvabrainz.common.LabelMbid
import com.ealva.ealvabrainz.common.RecordingMbid
import com.ealva.ealvabrainz.common.ReleaseGroupMbid
import com.ealva.ealvabrainz.common.TrackMbid

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
    public class Collection(mbid: CollectionMbid) : BrowseOn, CollectionQueryMapItem(mbid)
    public class Label(mbid: LabelMbid) : BrowseOn, LabelQueryMapItem(mbid)
    public class Recording(mbid: RecordingMbid) : BrowseOn, RecordingQueryMapItem(mbid)
    public class ReleaseGroup(mbid: ReleaseGroupMbid) : BrowseOn, ReleaseGroupQueryMapItem(mbid)
    public class Track(mbid: TrackMbid) : BrowseOn, TrackQueryMapItem(mbid)
    public class TrackArtist(mbid: ArtistMbid) : BrowseOn, TrackArtistQueryMapItem(mbid)
  }

  public companion object {
    internal operator fun invoke(
      browseOn: BrowseOn,
      limit: Limit?,
      offset: Offset?,
      browse: ReleaseBrowse.() -> Unit
    ): QueryMap =
      ReleaseBrowseOp(browseOn).apply(browse).queryMap(limit, offset)
  }
}

private class ReleaseBrowseOp(browseOn: BrowseOn) : ReleaseBrowse, BaseBrowse<Browse>(browseOn) {
  override fun Set<Inc>.verifyIncludes(): Set<Inc> = apply {
    // All entities except area, place, release, and series support ratings/user-ratings
    val supportsRatings = setOf(Browse.Labels, Browse.Recordings, Browse.ReleaseGroups)
    if (contains(Browse.Ratings))
      require(any(supportsRatings::contains)) {
        "If includes contains ${Browse.Ratings} it must also contain one of $supportsRatings"
      }
  }
}
