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

import com.ealva.ealvabrainz.brainz.MusicBrainz
import com.ealva.ealvabrainz.brainz.data.Area
import com.ealva.ealvabrainz.brainz.data.joinOrNull
import com.ealva.ealvabrainz.common.AreaMbid
import retrofit2.Response

public interface AreaLookup : EntityLookup<Area.Misc>

internal class AreaLookupOp : BaseEntityLookup<Area.Misc>(), AreaLookup {
  suspend fun execute(
    brainz: MusicBrainz,
    mbid: AreaMbid
  ): Response<Area> = brainz.lookupArea(
    mbid.value,
    includeSet.joinOrNull()
  )
}
