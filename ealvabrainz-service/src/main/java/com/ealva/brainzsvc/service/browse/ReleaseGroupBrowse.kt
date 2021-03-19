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

import com.ealva.brainzsvc.common.ArtistQueryMapItem
import com.ealva.brainzsvc.common.QueryMap
import com.ealva.brainzsvc.common.QueryMapItem
import com.ealva.brainzsvc.common.ReleaseQueryMapItem
import com.ealva.brainzsvc.service.browse.ReleaseGroupBrowse.BrowseOn
import com.ealva.ealvabrainz.brainz.MusicBrainz
import com.ealva.ealvabrainz.brainz.data.BrowseReleaseGroupList
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup
import com.ealva.ealvabrainz.common.ArtistMbid
import com.ealva.ealvabrainz.common.ReleaseMbid
import retrofit2.Response

/**
 * Builds the release browsing call based on [BrowseOn] type, [include], [relationships], [types],
 * and [status]
 */
public interface ReleaseGroupBrowse : EntityBrowse<ReleaseGroup.Browse> {
  /**
   * BrowseOn the entity related to a group of releases.
   */
  @Suppress("unused")
  public sealed interface BrowseOn : QueryMapItem {
    public class Artist(mbid: ArtistMbid) : BrowseOn, ArtistQueryMapItem(mbid)
    public class Release(mbid: ReleaseMbid) : BrowseOn, ReleaseQueryMapItem(mbid)
  }
}

internal class ReleaseGroupBrowseOp(
  override val browseOn: BrowseOn
) : ReleaseGroupBrowse, BaseEntityBrowse<ReleaseGroup.Browse, BrowseReleaseGroupList>() {
  override suspend fun doExecute(
    musicBrainz: MusicBrainz,
    queryMap: QueryMap
  ): Response<BrowseReleaseGroupList> {
    return musicBrainz.browseReleaseGroups(queryMap)
  }
}
