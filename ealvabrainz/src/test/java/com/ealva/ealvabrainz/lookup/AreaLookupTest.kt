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

import com.ealva.ealvabrainz.brainz.data.Area
import com.ealva.ealvabrainz.brainz.data.Relationships
import com.ealva.ealvabrainz.matchers.expect
import com.ealva.ealvabrainz.shared.Includes.ALL_MISC_INCLUDES
import com.ealva.ealvabrainz.shared.Includes.ALL_RELS
import com.nhaarman.expect.expect
import org.junit.Test

public class AreaLookupTest {
  @Test
  public fun `test no includes`() {
    expect(AreaLookup {}).toBeNull()
  }

  @Test
  public fun `test include all`() {
    val include = AreaLookup {
      include(*Area.Include.values())
      relationships(*Relationships.values())
    }

    expect(include).toNotBeNull()
    val includes = include?.split('+')?.toSet() ?: emptySet()
    expect(includes.size).toBe(ALL_MISC_INCLUDES.size + ALL_RELS.size)
    expect(includes - (ALL_MISC_INCLUDES + ALL_RELS)).toBeEmpty()
  }
}
