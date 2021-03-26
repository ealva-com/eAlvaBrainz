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
import com.ealva.brainzsvc.common.buildQueryMap
import com.ealva.brainzsvc.common.include
import com.ealva.brainzsvc.common.status
import com.ealva.brainzsvc.common.types
import com.ealva.ealvabrainz.brainz.data.Label

public interface LabelLookup : EntitySubqueryLookup<Label.Include> {
  public companion object {
    internal operator fun invoke(lookup: LabelLookup.() -> Unit): QueryMap =
      LabelLookupOp().apply(lookup).queryMap()
  }
}

private class LabelLookupOp : BaseSubqueryLookup<Label.Include>(), LabelLookup {
  fun queryMap(): QueryMap = buildQueryMap {
    include(incSet)
    types(typeSet?.ensureValidType(incSet))
    status(statusSet?.ensureValidStatus(incSet))
  }
}
