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
import com.ealva.ealvabrainz.brainz.data.Artist
import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.brainz.data.join
import retrofit2.Response

public interface ArtistLookup :
  EntitySubqueryLookup<Artist.Subquery, Artist.Misc>

internal class ArtistLookupOp :
  BaseSubqueryLookup<Artist.Subquery, Artist.Misc>(), ArtistLookup {

  suspend fun execute(
    mbid: ArtistMbid,
    brainz: MusicBrainz
  ): Response<Artist> = brainz.lookupArtist(
    mbid.value,
    if (includeSet.isNotEmpty()) includeSet.join() else null,
    typeSet?.ensureValidType(includeSet)?.join(),
    statusSet?.ensureValidStatus(includeSet)?.join()
  )
}
