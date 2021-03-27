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

import com.ealva.ealvabrainz.common.CollectionQueryMapItem
import com.ealva.ealvabrainz.common.Limit
import com.ealva.ealvabrainz.common.Offset
import com.ealva.ealvabrainz.common.QueryMap
import com.ealva.ealvabrainz.common.QueryMapItem
import com.ealva.ealvabrainz.browse.InstrumentBrowse.BrowseOn
import com.ealva.ealvabrainz.brainz.data.Instrument.Browse
import com.ealva.ealvabrainz.common.CollectionMbid

/**
 * Builds the release browsing call based on [BrowseOn] type, [include], [relationships], [types],
 * and [status]
 */
public interface InstrumentBrowse : EntityBrowse<Browse> {
  /**
   * BrowseOn the entity related to a group of releases.
   */
  @Suppress("unused")
  public sealed interface BrowseOn : QueryMapItem {
    public class Collection(mbid: CollectionMbid) : BrowseOn, CollectionQueryMapItem(mbid)
  }

  public companion object {
    public operator fun invoke(
      browseOn: BrowseOn,
      limit: Limit?,
      offset: Offset?,
      browse: InstrumentBrowse.() -> Unit
    ): QueryMap =
      InstrumentBrowseOp(browseOn).apply(browse).queryMap(limit, offset)
  }
}

private class InstrumentBrowseOp(
  browseOn: BrowseOn
) : InstrumentBrowse, BaseBrowse<Browse>(browseOn)
