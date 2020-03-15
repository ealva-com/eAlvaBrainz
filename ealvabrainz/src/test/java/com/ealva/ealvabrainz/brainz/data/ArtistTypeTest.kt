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

class ArtistTypeTest {
  @Test
  fun `test String to ArtistType`() {
    expect("Person".toArtistType()).toBe(ArtistType.Person)
    expect("Group".toArtistType()).toBe(ArtistType.Group)
    expect("Orchestra".toArtistType()).toBe(ArtistType.Orchestra)
    expect("Choir".toArtistType()).toBe(ArtistType.Choir)
    expect("Character".toArtistType()).toBe(ArtistType.Character)
    expect("Other".toArtistType()).toBe(ArtistType.Other)
    expect("Unknown".toArtistType()).toBe(ArtistType.Unknown)
    expect("".toArtistType()).toBe(ArtistType.Unknown)
    expect("person".toArtistType()).toBe(ArtistType.Unknown)
    expect("group".toArtistType()).toBe(ArtistType.Unknown)
    expect("orchestra".toArtistType()).toBe(ArtistType.Unknown)
    expect("choir".toArtistType()).toBe(ArtistType.Unknown)
    expect("character".toArtistType()).toBe(ArtistType.Unknown)
    expect("unknown".toArtistType()).toBe(ArtistType.Unknown)
  }
}
