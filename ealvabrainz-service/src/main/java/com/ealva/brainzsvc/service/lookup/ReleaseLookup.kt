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

package com.ealva.brainzsvc.service.lookup

import com.ealva.brainzsvc.common.QueryMap
import com.ealva.brainzsvc.common.TocParam
import com.ealva.brainzsvc.common.buildQueryMap
import com.ealva.brainzsvc.common.cdstubs
import com.ealva.brainzsvc.common.include
import com.ealva.brainzsvc.common.mediaFormat
import com.ealva.brainzsvc.common.status
import com.ealva.brainzsvc.common.toc
import com.ealva.brainzsvc.common.types
import com.ealva.ealvabrainz.brainz.data.Release

public interface ReleaseLookup : EntitySubqueryLookup<Release.Include> {
  public companion object {
    internal operator fun invoke(lookup: ReleaseLookup.() -> Unit): QueryMap =
      ReleaseLookupOp().apply(lookup).queryMap()

    internal operator fun invoke(
      toc: TocParam?,
      excludeCDStubs: Boolean,
      allMediumFormats: Boolean,
      lookup: ReleaseLookup.() -> Unit
    ): QueryMap = ReleaseLookupOp().apply(lookup).queryMap(toc, excludeCDStubs, allMediumFormats)
  }
}

private class ReleaseLookupOp : BaseSubqueryLookup<Release.Include>(), ReleaseLookup {
  fun queryMap(): QueryMap = buildQueryMap {
    include(incSet)
    types(typeSet)
    status(statusSet)
  }

  fun queryMap(toc: TocParam?, excludeCDStubs: Boolean, allMediumFormats: Boolean) = buildQueryMap {
    include(incSet)
    toc(toc)
    cdstubs(excludeCDStubs)
    mediaFormat(allMediumFormats)
  }
}
