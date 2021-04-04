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

package com.ealva.ealvabrainz.browse

import com.ealva.ealvabrainz.common.AreaQueryMapItem
import com.ealva.ealvabrainz.common.ArtistQueryMapItem
import com.ealva.ealvabrainz.common.EditorQueryMapItem
import com.ealva.ealvabrainz.common.EventQueryMapItem
import com.ealva.ealvabrainz.common.LabelQueryMapItem
import com.ealva.ealvabrainz.common.Limit
import com.ealva.ealvabrainz.common.Offset
import com.ealva.ealvabrainz.common.PlaceQueryMapItem
import com.ealva.ealvabrainz.common.QueryMap
import com.ealva.ealvabrainz.common.QueryMapItem
import com.ealva.ealvabrainz.common.RecordingQueryMapItem
import com.ealva.ealvabrainz.common.ReleaseGroupQueryMapItem
import com.ealva.ealvabrainz.common.ReleaseQueryMapItem
import com.ealva.ealvabrainz.common.WorkQueryMapItem
import com.ealva.ealvabrainz.browse.CollectionBrowse.BrowseOn
import com.ealva.ealvabrainz.brainz.data.Collection.Browse
import com.ealva.ealvabrainz.brainz.data.AreaMbid
import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.common.EditorName
import com.ealva.ealvabrainz.brainz.data.EventMbid
import com.ealva.ealvabrainz.brainz.data.LabelMbid
import com.ealva.ealvabrainz.brainz.data.PlaceMbid
import com.ealva.ealvabrainz.brainz.data.RecordingMbid
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupMbid
import com.ealva.ealvabrainz.brainz.data.ReleaseMbid
import com.ealva.ealvabrainz.brainz.data.WorkMbid

/**
 * Builds the release browsing call based on [BrowseOn] type, [include], [relationships], [types],
 * and [status]
 */
public interface CollectionBrowse : EntityBrowse<Browse> {
  /**
   * BrowseOn the entity related to a group of releases.
   */
  @Suppress("unused")
  public sealed interface BrowseOn : QueryMapItem {
    public class Area(mbid: AreaMbid) : BrowseOn, AreaQueryMapItem(mbid)
    public class Artist(mbid: ArtistMbid) : BrowseOn, ArtistQueryMapItem(mbid)
    public class Editor(editor: EditorName) : BrowseOn, EditorQueryMapItem(editor)
    public class Event(eventMbid: EventMbid) : BrowseOn, EventQueryMapItem(eventMbid)
    public class Label(labelMbid: LabelMbid) : BrowseOn, LabelQueryMapItem(labelMbid)
    public class Place(placeMbid: PlaceMbid) : BrowseOn, PlaceQueryMapItem(placeMbid)
    public class Recording(mbid: RecordingMbid) : BrowseOn, RecordingQueryMapItem(mbid)
    public class Release(mbid: ReleaseMbid) : BrowseOn, ReleaseQueryMapItem(mbid)
    public class ReleaseGroup(mbid: ReleaseGroupMbid) : BrowseOn, ReleaseGroupQueryMapItem(mbid)
    public class Work(mbid: WorkMbid) : BrowseOn, WorkQueryMapItem(mbid)
  }

  public companion object {
    public operator fun invoke(
      browseOn: BrowseOn,
      limit: Limit?,
      offset: Offset?,
      browse: CollectionBrowse.() -> Unit
    ): QueryMap =
      CollectionBrowseOp(browseOn).apply(browse).queryMap(limit, offset)
  }
}

private class CollectionBrowseOp(
  browseOn: BrowseOn
) : CollectionBrowse, BaseBrowse<Browse>(browseOn)
