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

package com.ealva.brainzsvc.common

import com.nhaarman.expect.expect
import org.junit.Test

public class TocParamTest {
  @Test
  public fun `test ctor and toString`() {
    expect(
      makeTocString(
        267257,
        150,
        22767, 41887, 58317, 72102, 91375, 104652, 115380, 132165, 143932, 159870, 174597
      )
    ).toBe(
      "1+12+267257+150+22767+41887+58317+72102+91375+104652+115380+132165+143932+159870+174597"
    )

    expect(makeTocString(400000, 145)).toBe("1+1+400000+145")
  }

  @Test(expected = IllegalArgumentException::class)
  public fun `test make TOC with bad leadoutSectorOffset`() {
    makeTocString(0, 0)
  }

  private fun makeTocString(
    leadoutSectorOffset: Int,
    firstSectorOffset: Int,
    vararg sectorOffsets: Int
  ): String = TocParam(leadoutSectorOffset, firstSectorOffset, *sectorOffsets).toString()
}
