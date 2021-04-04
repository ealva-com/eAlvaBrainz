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

import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.common.QueryMap
import com.ealva.ealvabrainz.common.TocParam
import com.ealva.ealvabrainz.common.buildQueryMap
import com.ealva.ealvabrainz.common.cdstubs
import com.ealva.ealvabrainz.common.include
import com.ealva.ealvabrainz.common.mediaFormat
import com.ealva.ealvabrainz.common.status
import com.ealva.ealvabrainz.common.toc
import com.ealva.ealvabrainz.common.types

public interface ReleaseLookup : EntitySubqueryLookup<Release.Include> {
  public companion object {
    public operator fun invoke(lookup: ReleaseLookup.() -> Unit): QueryMap =
      ReleaseLookupOp().apply(lookup).queryMap()

    public operator fun invoke(
      toc: TocParam?,
      excludeCDStubs: Boolean,
      allMediumFormats: Boolean,
      lookup: ReleaseLookup.() -> Unit
    ): QueryMap = ReleaseLookupOp().apply(lookup).queryMap(toc, excludeCDStubs, allMediumFormats)
  }
}

private class ReleaseLookupOp : BaseSubqueryLookup<Release.Include>(), ReleaseLookup {
  override fun validateInclude(set: Set<Release.Include>) {
    set.ifContainsThenRequires(Release.Include.Isrcs, Release.Include.Recordings)
  }

  fun queryMap(): QueryMap = buildQueryMap {
    val incSet = allIncludes
    include(incSet)
    types(typeSet)
    status(statusSet)
  }

  fun queryMap(toc: TocParam?, excludeCDStubs: Boolean, allMediumFormats: Boolean) = buildQueryMap {
    val incSet = allIncludes
    include(incSet)
    toc(toc)
    cdstubs(excludeCDStubs)
    mediaFormat(allMediumFormats)
  }
}
