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
import com.ealva.brainzsvc.common.QueryMap
import com.ealva.brainzsvc.common.QueryMapItem
import com.ealva.brainzsvc.common.RecordingQueryMapItem
import com.ealva.brainzsvc.common.ReleaseGroupQueryMapItem
import com.ealva.brainzsvc.common.ReleaseQueryMapItem
import com.ealva.brainzsvc.common.WorkQueryMapItem
import com.ealva.brainzsvc.service.browse.ArtistBrowse.BrowseOn
import com.ealva.ealvabrainz.brainz.MusicBrainz
import com.ealva.ealvabrainz.brainz.data.Artist
import com.ealva.ealvabrainz.brainz.data.BrowseArtistList
import com.ealva.ealvabrainz.common.AreaMbid
import com.ealva.ealvabrainz.common.RecordingMbid
import com.ealva.ealvabrainz.common.ReleaseGroupMbid
import com.ealva.ealvabrainz.common.ReleaseMbid
import com.ealva.ealvabrainz.common.WorkMbid
import retrofit2.Response

/**
 * Builds the artist browsing call based on [BrowseOn] type, [include], [relationships], [types],
 * and [status]
 */
public interface ArtistBrowse : EntityBrowse<Artist.Browse> {
  /**
   * BrowseOn the entity related to a group of releases.
   */
  @Suppress("unused")
  public sealed interface BrowseOn : QueryMapItem {
    public class Area(mbid: AreaMbid) : BrowseOn, AreaQueryMapItem(mbid)
    public class Recording(mbid: RecordingMbid) : BrowseOn, RecordingQueryMapItem(mbid)
    public class Release(mbid: ReleaseMbid) : BrowseOn, ReleaseQueryMapItem(mbid)
    public class ReleaseGroup(mbid: ReleaseGroupMbid) : BrowseOn, ReleaseGroupQueryMapItem(mbid)
    public class Work(mbid: WorkMbid) : BrowseOn, WorkQueryMapItem(mbid)
  }
}

internal class ArtistBrowseOp(
  override val browseOn: BrowseOn
) : ArtistBrowse, BaseEntityBrowse<Artist.Browse, BrowseArtistList>() {
  override suspend fun doExecute(
    musicBrainz: MusicBrainz,
    queryMap: QueryMap
  ): Response<BrowseArtistList> = musicBrainz.browseArtists(queryMap)
}
