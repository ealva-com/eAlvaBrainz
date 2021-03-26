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
import com.ealva.brainzsvc.common.EditorQueryMapItem
import com.ealva.brainzsvc.common.EventQueryMapItem
import com.ealva.brainzsvc.common.LabelQueryMapItem
import com.ealva.brainzsvc.common.Limit
import com.ealva.brainzsvc.common.Offset
import com.ealva.brainzsvc.common.PlaceQueryMapItem
import com.ealva.brainzsvc.common.QueryMap
import com.ealva.brainzsvc.common.QueryMapItem
import com.ealva.brainzsvc.common.RecordingQueryMapItem
import com.ealva.brainzsvc.common.ReleaseGroupQueryMapItem
import com.ealva.brainzsvc.common.ReleaseQueryMapItem
import com.ealva.brainzsvc.common.WorkQueryMapItem
import com.ealva.brainzsvc.service.browse.CollectionBrowse.BrowseOn
import com.ealva.ealvabrainz.brainz.data.Collection.Browse
import com.ealva.ealvabrainz.common.AreaMbid
import com.ealva.ealvabrainz.common.ArtistMbid
import com.ealva.ealvabrainz.common.EditorName
import com.ealva.ealvabrainz.common.EventMbid
import com.ealva.ealvabrainz.common.LabelMbid
import com.ealva.ealvabrainz.common.PlaceMbid
import com.ealva.ealvabrainz.common.RecordingMbid
import com.ealva.ealvabrainz.common.ReleaseGroupMbid
import com.ealva.ealvabrainz.common.ReleaseMbid
import com.ealva.ealvabrainz.common.WorkMbid

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
    internal operator fun invoke(
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
