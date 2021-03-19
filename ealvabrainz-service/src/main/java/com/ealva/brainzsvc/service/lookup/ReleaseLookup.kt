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

import com.ealva.brainzsvc.common.TocParam
import com.ealva.brainzsvc.common.buildQueryMap
import com.ealva.brainzsvc.common.cdstubs
import com.ealva.brainzsvc.common.include
import com.ealva.brainzsvc.common.mediaFormat
import com.ealva.brainzsvc.common.status
import com.ealva.brainzsvc.common.toc
import com.ealva.brainzsvc.common.types
import com.ealva.ealvabrainz.brainz.MusicBrainz
import com.ealva.ealvabrainz.brainz.data.BrowseReleaseList
import com.ealva.ealvabrainz.brainz.data.DiscLookupList
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.common.DiscId
import com.ealva.ealvabrainz.common.ReleaseMbid
import retrofit2.Response

public interface ReleaseLookup : EntitySubqueryLookup<Release.Subquery, Release.Misc>

internal class ReleaseLookupOp :
  BaseSubqueryLookup<Release.Subquery, Release.Misc>(), ReleaseLookup {

  suspend fun lookupRelease(
    brainz: MusicBrainz,
    mbid: ReleaseMbid
  ): Response<Release> = brainz.lookupRelease(
    mbid.value,
    buildQueryMap {
      include(includeSet)
      types(typeSet)
      status(statusSet)
    }
  )

  suspend fun lookupDisc(
    brainz: MusicBrainz,
    discId: DiscId,
    toc: TocParam?,
    excludeCDStubs: Boolean,
    allMediumFormats: Boolean,
  ): Response<DiscLookupList> = brainz.lookupDisc(
    discId.value,
    buildQueryMap {
      include(includeSet)
      toc(toc)
      cdstubs(excludeCDStubs)
      mediaFormat(allMediumFormats)
    }
  )

  suspend fun fuzzyLookupTOC(
    brainz: MusicBrainz,
    toc: TocParam,
    excludeCDStubs: Boolean,
    allMediumFormats: Boolean,
  ): Response<BrowseReleaseList> = brainz.fuzzyLookupTOC(
    buildQueryMap {
      include(includeSet)
      toc(toc)
      cdstubs(excludeCDStubs)
      mediaFormat(allMediumFormats)
    }
  )
}
