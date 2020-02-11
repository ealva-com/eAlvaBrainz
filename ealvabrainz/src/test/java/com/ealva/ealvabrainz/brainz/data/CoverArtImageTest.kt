/*
 * Copyright (c) 2020  Eric A. Snell
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

package com.ealva.ealvabrainz.brainz.data

import com.nhaarman.expect.expect
import org.junit.Test

internal class CoverArtImageTest {
  /** Currently the list may contain nulls, so let's test they get filtered and others converted */
  @Test
  fun `test imageTypes converts list of raw type string to CoverArtImageType list`() {
    val image = CoverArtImage(
      types = listOf(
        null,
        CoverArtImageType.TYPE_FRONT.value,
        null,
        CoverArtImageType.TYPE_BACK.value,
        null,
        null,
        CoverArtImageType.TYPE_POSTER.value,
        null)
    )
    val sequence = image.imageTypes
    expect(sequence.first()).toBe(CoverArtImageType.TYPE_FRONT)
    expect(sequence.elementAt(1)).toBe(CoverArtImageType.TYPE_BACK)
    expect(sequence.elementAt(2)).toBe(CoverArtImageType.TYPE_POSTER)
  }
}
