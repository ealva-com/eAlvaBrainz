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

package com.ealva.ealvabrainz.lookup

import com.ealva.ealvabrainz.common.QueryMap
import com.ealva.ealvabrainz.common.buildQueryMap
import com.ealva.ealvabrainz.brainz.data.Artist
import com.ealva.ealvabrainz.brainz.data.Artist.Include.ArtistCredits
import com.ealva.ealvabrainz.brainz.data.Artist.Include.DiscIds
import com.ealva.ealvabrainz.brainz.data.Artist.Include.Isrcs
import com.ealva.ealvabrainz.brainz.data.Artist.Include.Media
import com.ealva.ealvabrainz.brainz.data.Artist.Include.Recordings
import com.ealva.ealvabrainz.brainz.data.Artist.Include.ReleaseGroups
import com.ealva.ealvabrainz.brainz.data.Artist.Include.Releases
import com.ealva.ealvabrainz.brainz.data.Artist.Include.Works
import com.ealva.ealvabrainz.common.include
import com.ealva.ealvabrainz.common.status
import com.ealva.ealvabrainz.common.types

public interface ArtistLookup : EntitySubqueryLookup<Artist.Include> {
  public companion object {
    public operator fun invoke(lookup: ArtistLookup.() -> Unit): QueryMap {
      return ArtistLookupOp().apply(lookup).queryMap()
    }
  }
}

private class ArtistLookupOp : BaseSubqueryLookup<Artist.Include>(), ArtistLookup {
  override fun validateInclude(set: Set<Artist.Include>) {
    set.ifContainsThenRequires(DiscIds, Releases)
    set.ifContainsThenRequires(Media, Releases)
    set.ifContainsThenRequires(Isrcs, Recordings)
    set.ifContainsThenRequires(ArtistCredits, ReleaseGroups, Works, Recordings, Releases)
  }

  fun queryMap(): QueryMap = buildQueryMap {
    val incSet = allIncludes
    include(incSet)
    types(typeSet?.ensureValidType(incSet))
    status(statusSet?.ensureValidStatus(incSet))
  }
}
