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

package com.ealva.brainzsvc.service.search

import com.ealva.ealvabrainz.common.ArtistMbid
import com.ealva.ealvabrainz.common.toArtistName
import com.ealva.ealvabrainz.lucene.luceneEscape
import com.ealva.ealvabrainz.lucene.prohibit
import com.ealva.ealvabrainz.lucene.require
import com.ealva.ealvabrainz.lucene.toTerm
import com.nhaarman.expect.expect
import org.junit.Test

public class ReleaseSearchTest {
  @Test
  public fun `test alias`() {
    expect(ReleaseSearch().alias { "AnAlias" }.toString()).toBe("alias:AnAlias")
    expect(ReleaseSearch().alias { "An Alias" }.toString()).toBe("""alias:"An Alias"""")
    expect(ReleaseSearch().alias { "AnAlias".toTerm().require() }.toString())
      .toBe("alias:\\+AnAlias")
  }

  @Test
  public fun `test artist mbid`() {
    expect(ReleaseSearch().artistId { NIRVANA_MBID }.toString())
      .toBe("arid:${NIRVANA_MBID.value.luceneEscape()}")
    expect(ReleaseSearch().artistId { NIRVANA_MBID.value.toTerm() }.toString())
      .toBe("arid:${NIRVANA_MBID.value.luceneEscape()}")
  }

  @Test
  public fun `test artist`() {
    expect(ReleaseSearch().artist { JETHRO_TULL }.toString())
      .toBe("artist:\"${JETHRO_TULL.value}\"")
    expect(ReleaseSearch().artist { JETHRO_TULL.value.toTerm().prohibit() }.toString())
      .toBe("artist:\\-\"${JETHRO_TULL.value}\"")
  }

  @Test
  public fun `test artist name`() {
    expect(ReleaseSearch().artistName { JETHRO_TULL }.toString())
      .toBe("artistname:\"${JETHRO_TULL.value}\"")
    expect(ReleaseSearch().artistName { JETHRO_TULL.value.toTerm().prohibit() }.toString())
      .toBe("artistname:\\-\"${JETHRO_TULL.value}\"")
  }
}

private val NIRVANA_MBID = ArtistMbid("5b11f4ce-a62d-471e-81fc-a69a8278c7da")
private val JETHRO_TULL = "Jethro Tull".toArtistName()
