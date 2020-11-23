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

package com.ealva.brainzapp.ui.artist

import com.ealva.ealvabrainz.R
import com.ealva.ealvabrainz.brainz.data.ArtistType
import com.nhaarman.expect.expect
import org.junit.Test

public class ArtistTypeLabelsTest {
  @Test
  public fun `test ArtistType to ArtistTypeLabels`() {
    expect(ArtistType.Person.labels).toBe(ArtistTypeLabels.PersonLabels)
    expect(ArtistType.Group.labels).toBe(ArtistTypeLabels.GroupLabels)
    expect(ArtistType.Orchestra.labels).toBe(ArtistTypeLabels.OrchestraLabels)
    expect(ArtistType.Choir.labels).toBe(ArtistTypeLabels.ChoirLabels)
    expect(ArtistType.Character.labels).toBe(ArtistTypeLabels.CharacterLabels)
    expect(ArtistType.Other.labels).toBe(ArtistTypeLabels.OtherLabels)
    expect(ArtistType.Unknown.labels).toBe(ArtistTypeLabels.UnknownLabels)
  }

  @Test
  public fun `test ArtistType labels`() {
    doTestLabels(
      ArtistTypeLabels.PersonLabels,
      R.string.Person,
      R.string.BornLabel,
      R.string.DiedLabel,
      R.string.BornInLabel,
      R.string.DiedInLabel
    )
    doTestLabels(
      ArtistTypeLabels.GroupLabels,
      R.string.Group,
      R.string.FoundedLabel,
      R.string.DissolvedLabel,
      R.string.FoundedInLabel,
      R.string.DissolvedInLabel
    )
    doTestLabels(
      ArtistTypeLabels.OrchestraLabels,
      R.string.Orchestra,
      R.string.FoundedLabel,
      R.string.DissolvedLabel,
      R.string.FoundedInLabel,
      R.string.DissolvedInLabel
    )
    doTestLabels(
      ArtistTypeLabels.ChoirLabels,
      R.string.Choir,
      R.string.FoundedLabel,
      R.string.DissolvedLabel,
      R.string.FoundedInLabel,
      R.string.DissolvedInLabel
    )
    doTestLabels(
      ArtistTypeLabels.CharacterLabels,
      R.string.Character,
      R.string.CreatedLabel,
      R.string.empty,
      R.string.CreatedInLabel,
      R.string.empty
    )
    doTestLabels(
      ArtistTypeLabels.OtherLabels,
      R.string.Other,
      R.string.StartedLabel,
      R.string.EndedLabel,
      R.string.StartedInLabel,
      R.string.EndedInLabel
    )
    doTestLabels(
      ArtistTypeLabels.UnknownLabels,
      R.string.Unknown,
      R.string.empty,
      R.string.empty,
      R.string.empty,
      R.string.empty
    )
  }

  private fun doTestLabels(
    artistTypeLabels: ArtistTypeLabels,
    typeName: Int,
    lifespanBegin: Int,
    lifespanEnd: Int,
    startArea: Int,
    endArea: Int
  ) {
    expect(artistTypeLabels.typeName).toBe(typeName) { "ArtistType:$artistTypeLabels typeName" }
    expect(artistTypeLabels.lifespanBeginLabel)
      .toBe(lifespanBegin) { "ArtistType:$artistTypeLabels lifespanBeginLabel" }
    expect(artistTypeLabels.lifespanEndLabel)
      .toBe(lifespanEnd) { "ArtistType:$artistTypeLabels lifespanEndLabel" }
    expect(artistTypeLabels.startAreaLabel)
      .toBe(startArea) { "ArtistType:$artistTypeLabels startAreaLabel" }
    expect(artistTypeLabels.endAreaLabel)
      .toBe(endArea) { "ArtistType:$artistTypeLabels endAreaLabel" }
  }
}
