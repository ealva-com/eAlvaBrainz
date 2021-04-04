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

import com.ealva.ealvabrainz.brainz.data.Artist
import com.ealva.ealvabrainz.brainz.data.Relationships
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup
import com.ealva.ealvabrainz.common.BrainzInvalidIncludesException
import com.ealva.ealvabrainz.matchers.expect
import com.ealva.ealvabrainz.shared.Includes.ALL_MISC_INCLUDES
import com.ealva.ealvabrainz.shared.Includes.ALL_RELS
import com.ealva.ealvabrainz.shared.Includes.ALL_STATUS
import com.ealva.ealvabrainz.shared.Includes.ALL_TYPES
import com.nhaarman.expect.expect
import org.junit.Test

public class ArtistLookupTest {
  @Test
  public fun `test empty`() {
    expect(ArtistLookup {}).toBeEmpty()
  }

  @Test
  public fun `test include DiscIds`() {
    ArtistLookup {
      include(Artist.Include.Releases, Artist.Include.DiscIds)
    }
  }

  @Test(expected = BrainzInvalidIncludesException::class)
  public fun `test include DiscIds without Releases`() {
    ArtistLookup {
      include(Artist.Include.DiscIds)
    }
  }

  @Test
  public fun `test include Media`() {
    ArtistLookup {
      include(Artist.Include.Releases, Artist.Include.Media)
    }
  }

  @Test(expected = BrainzInvalidIncludesException::class)
  public fun `test include Media without Releases`() {
    ArtistLookup {
      include(Artist.Include.Media)
    }
  }

  @Test
  public fun `test include Isrc`() {
    ArtistLookup {
      include(Artist.Include.Recordings, Artist.Include.Isrcs)
    }
  }

  @Test(expected = BrainzInvalidIncludesException::class)
  public fun `test include Isrc without Releases`() {
    ArtistLookup {
      include(Artist.Include.Isrcs)
    }
  }

  @Test
  public fun `test include ArtistCredits`() {
    ArtistLookup {
      include(Artist.Include.ReleaseGroups, Artist.Include.ArtistCredits)
    }
    ArtistLookup {
      include(Artist.Include.Works, Artist.Include.ArtistCredits)
    }
    ArtistLookup {
      include(Artist.Include.Recordings, Artist.Include.ArtistCredits)
    }
    ArtistLookup {
      include(Artist.Include.Releases, Artist.Include.ArtistCredits)
    }
  }

  @Test(expected = BrainzInvalidIncludesException::class)
  public fun `test include ArtistCredits without other required`() {
    ArtistLookup {
      include(Artist.Include.ArtistCredits)
    }
  }

  @Test
  public fun `test include all`() {
    val map: Map<String, String> = ArtistLookup {
      include(*Artist.Include.values())
      relationships(*Relationships.values())
      types(*ReleaseGroup.Type.values())
      status(*Release.Status.values())
    }
    expect(map).toContainKey("inc")
    val includes = map["inc"]?.split('+')?.toSet() ?: emptySet()
    expect(includes.size).toBe(ALL_ARTIST_INCLUDES.size + ALL_RELS.size)
    expect(includes - (ALL_ARTIST_INCLUDES + ALL_RELS)).toBeEmpty()

    expect(map).toContainKey("type")
    val types = map["type"]?.split('|')?.toSet() ?: emptySet()
    expect(types.size).toBe(ALL_TYPES.size)
    expect(types - ALL_TYPES).toBeEmpty()

    expect(map).toContainKey("status")
    val status = map["status"]?.split('|')?.toSet() ?: emptySet()
    expect(status.size).toBe(ALL_STATUS.size)
    expect(status - ALL_STATUS).toBeEmpty()
  }
}

private val ALL_ARTIST_INCLUDES: Set<String> = setOf(
  "recordings",
  "releases",
  "release-groups",
  "works",
  "discids",
  "media",
  "isrcs",
  "artist-credits",
  "various-artists",
) + ALL_MISC_INCLUDES
