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

import com.ealva.brainzsvc.common.buildQueryMap
import com.ealva.brainzsvc.common.include
import com.ealva.brainzsvc.common.status
import com.ealva.brainzsvc.common.types
import com.ealva.ealvabrainz.brainz.MusicBrainz
import com.ealva.ealvabrainz.brainz.data.IsrcRecordingList
import com.ealva.ealvabrainz.brainz.data.Recording
import com.ealva.ealvabrainz.common.Isrc
import retrofit2.Response

internal class IsrcLookupOp :
  BaseSubqueryLookup<Recording.Subquery, Recording.Misc>(), RecordingLookup {

  suspend fun execute(
    isrc: Isrc,
    brainz: MusicBrainz
  ): Response<IsrcRecordingList> = brainz.lookupIsrc(
    isrc.value,
    buildQueryMap {
      include(includeSet)
      types(typeSet?.ensureValidType(includeSet))
      status(statusSet?.ensureValidStatus(includeSet))
    }
  )
}
