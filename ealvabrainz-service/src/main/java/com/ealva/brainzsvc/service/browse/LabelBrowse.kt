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
import com.ealva.brainzsvc.common.CollectionQueryMapItem
import com.ealva.brainzsvc.common.Limit
import com.ealva.brainzsvc.common.Offset
import com.ealva.brainzsvc.common.QueryMap
import com.ealva.brainzsvc.common.QueryMapItem
import com.ealva.brainzsvc.common.ReleaseQueryMapItem
import com.ealva.brainzsvc.service.browse.LabelBrowse.BrowseOn
import com.ealva.ealvabrainz.brainz.data.Label.Browse
import com.ealva.ealvabrainz.common.AreaMbid
import com.ealva.ealvabrainz.common.CollectionMbid
import com.ealva.ealvabrainz.common.ReleaseMbid

/**
 * Builds the release browsing call based on [BrowseOn] type, [include], [relationships], [types],
 * and [status]
 */
public interface LabelBrowse : EntityBrowse<Browse> {
  /**
   * BrowseOn the entity related to a group of releases.
   */
  @Suppress("unused")
  public sealed interface BrowseOn : QueryMapItem {
    public class Area(mbid: AreaMbid) : BrowseOn, AreaQueryMapItem(mbid)
    public class Collection(mbid: CollectionMbid) : BrowseOn, CollectionQueryMapItem(mbid)
    public class Release(mbid: ReleaseMbid) : BrowseOn, ReleaseQueryMapItem(mbid)
  }

  public companion object {
    internal operator fun invoke(
      browseOn: BrowseOn,
      limit: Limit?,
      offset: Offset?,
      browse: LabelBrowse.() -> Unit
    ): QueryMap =
      LabelBrowseOp(browseOn).apply(browse).queryMap(limit, offset)
  }
}

private class LabelBrowseOp(browseOn: BrowseOn) : LabelBrowse, BaseBrowse<Browse>(browseOn)
