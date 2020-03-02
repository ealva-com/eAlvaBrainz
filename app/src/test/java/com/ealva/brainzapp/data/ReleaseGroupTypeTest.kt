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

package com.ealva.brainzapp.data

import com.ealva.brainzapp.data.ReleaseGroupType.Primary.Album
import com.ealva.brainzapp.data.ReleaseGroupType.Primary.Broadcast
import com.ealva.brainzapp.data.ReleaseGroupType.Primary.EP
import com.ealva.brainzapp.data.ReleaseGroupType.Primary.Other
import com.ealva.brainzapp.data.ReleaseGroupType.Primary.Single
import com.ealva.brainzapp.data.ReleaseGroupType.Secondary.AudioDrama
import com.ealva.brainzapp.data.ReleaseGroupType.Secondary.Audiobook
import com.ealva.brainzapp.data.ReleaseGroupType.Secondary.Compilation
import com.ealva.brainzapp.data.ReleaseGroupType.Secondary.DJMix
import com.ealva.brainzapp.data.ReleaseGroupType.Secondary.Demo
import com.ealva.brainzapp.data.ReleaseGroupType.Secondary.Interview
import com.ealva.brainzapp.data.ReleaseGroupType.Secondary.Live
import com.ealva.brainzapp.data.ReleaseGroupType.Secondary.MixtapeStreet
import com.ealva.brainzapp.data.ReleaseGroupType.Secondary.Remix
import com.ealva.brainzapp.data.ReleaseGroupType.Secondary.Soundtrack
import com.ealva.brainzapp.data.ReleaseGroupType.Secondary.SpokenWord
import com.ealva.ealvabrainz.R
import com.nhaarman.expect.expect
import org.junit.Test
import com.ealva.brainzapp.data.ReleaseGroupType.Primary.Unknown as PRIMARY_UNKNOWN
import com.ealva.brainzapp.data.ReleaseGroupType.Primary.Unrecognized as PRIMARY_UNRECOGNIZED
import com.ealva.brainzapp.data.ReleaseGroupType.Secondary.Unknown as SECONDARY_UNKNOWN
import com.ealva.brainzapp.data.ReleaseGroupType.Secondary.Unrecognized as SECONDARY_UNRECOGNIZED

class ReleaseGroupTypeTest {
  @Test
  fun `test toPrimaryReleaseGroupType`() {
    expect("Album".toPrimaryReleaseGroupType()).toBe(Album)
    expect("Single".toPrimaryReleaseGroupType()).toBe(Single)
    expect("EP".toPrimaryReleaseGroupType()).toBe(EP)
    expect("Broadcast".toPrimaryReleaseGroupType()).toBe(Broadcast)
    expect("Other".toPrimaryReleaseGroupType()).toBe(Other)
    expect("".toPrimaryReleaseGroupType()).toBe(PRIMARY_UNKNOWN)
    expect("album".toPrimaryReleaseGroupType()).toBeInstanceOf<PRIMARY_UNRECOGNIZED> {
      expect(it.name).toBe("album")
    }
  }

  @Test
  fun `test toSecondaryReleaseGroupType`() {
    expect("Compilation".toSecondaryReleaseGroupType()).toBe(Compilation)
    expect("Soundtrack".toSecondaryReleaseGroupType()).toBe(Soundtrack)
    expect("Spokenword".toSecondaryReleaseGroupType()).toBe(SpokenWord)
    expect("Interview".toSecondaryReleaseGroupType()).toBe(Interview)
    expect("Audiobook".toSecondaryReleaseGroupType()).toBe(Audiobook)
    expect("Audio drama".toSecondaryReleaseGroupType()).toBe(AudioDrama)
    expect("Live".toSecondaryReleaseGroupType()).toBe(Live)
    expect("Remix".toSecondaryReleaseGroupType()).toBe(Remix)
    expect("DJ-mix".toSecondaryReleaseGroupType()).toBe(DJMix)
    expect("Mixtape/Street".toSecondaryReleaseGroupType()).toBe(MixtapeStreet)
    expect("Demo".toSecondaryReleaseGroupType()).toBe(Demo)
    expect("".toSecondaryReleaseGroupType()).toBe(SECONDARY_UNKNOWN)
    expect("compilation".toSecondaryReleaseGroupType()).toBeInstanceOf<SECONDARY_UNRECOGNIZED> {
      expect(it.name).toBe("compilation")
    }
  }

  @Test
  fun `test toSecondaryReleaseGroupList`() {
    doTestList(listOf(), listOf())
    doTestList(listOf("Compilation"), listOf(Compilation))
    doTestList(listOf("Soundtrack"), listOf(Soundtrack))
    doTestList(listOf("Spokenword"), listOf(SpokenWord))
    doTestList(listOf("Interview"), listOf(Interview))
    doTestList(listOf("Audiobook"), listOf(Audiobook))
    doTestList(listOf("Audio drama"), listOf(AudioDrama))
    doTestList(listOf("Live"), listOf(Live))
    doTestList(listOf("Remix"), listOf(Remix))
    doTestList(listOf("DJ-mix"), listOf(DJMix))
    doTestList(listOf("Mixtape/Street"), listOf(MixtapeStreet))
    doTestList(listOf("compilation"), listOf(SECONDARY_UNRECOGNIZED("compilation")))
    doTestList(listOf(""), listOf(SECONDARY_UNKNOWN))
    doTestList(
      listOf("Compilation", "Interview", "blah", ""),
      listOf(Compilation, Interview, SECONDARY_UNRECOGNIZED("blah"), SECONDARY_UNKNOWN)
    )
  }

  @Test
  fun `test Primary toDisplayString`() {
    expect(Album.toDisplayString(listOf(Compilation), ::fetcher)).toBe("Album + Compilation")
    expect(PRIMARY_UNKNOWN.toDisplayString(listOf(Soundtrack), ::fetcher)).toBe("Soundtrack")

    expect(PRIMARY_UNRECOGNIZED("blah").toDisplayString(listOf(Live), ::fetcher))
      .toBe("blah + Live")

    expect(Single.toDisplayString(listOf(), ::fetcher)).toBe("Single")
    expect(EP.toDisplayString(listOf(Remix, DJMix), ::fetcher)).toBe("EP + Remix + DJ mix")

    expect(Broadcast.toDisplayString(listOf(SpokenWord, Interview), ::fetcher))
      .toBe("Broadcast + Spoken word + Interview")

    expect(Other.toDisplayString(listOf(AudioDrama, Audiobook, MixtapeStreet), ::fetcher))
      .toBe("Other + Audio drama + Audiobook + Mixtape/Street")
  }

  @Test
  fun `test primary map contents`() {
    val map = ReleaseGroupType.mapOfPrimary()
    expect(map.remove("Album")).toBe(Album)
    expect(map.remove("Single")).toBe(Single)
    expect(map.remove("EP")).toBe(EP)
    expect(map.remove("Broadcast")).toBe(Broadcast)
    expect(map.remove("Other")).toBe(Other)
    expect(map.isEmpty()).toBe(true)  { "map not empty"}
  }

  @Test
  fun `test secondary map contents`() {
    val map = ReleaseGroupType.mapOfSecondary()
    expect(map.remove("Compilation")).toBe(Compilation)
    expect(map.remove("Soundtrack")).toBe(Soundtrack)
    expect(map.remove("Spokenword")).toBe(SpokenWord)
    expect(map.remove("Interview")).toBe(Interview)
    expect(map.remove("Audiobook")).toBe(Audiobook)
    expect(map.remove("Audio drama")).toBe(AudioDrama)
    expect(map.remove("Live")).toBe(Live)
    expect(map.remove("Remix")).toBe(Remix)
    expect(map.remove("DJ-mix")).toBe(DJMix)
    expect(map.remove("Mixtape/Street")).toBe(MixtapeStreet)
    expect(map.remove("Demo")).toBe(Demo)
    expect(map.isEmpty()).toBe(true) { "map not empty"}
  }
}

private fun doTestList(input: List<String>, result: List<ReleaseGroupType.Secondary>) {
  val list = input.toSecondaryReleaseGroupList()
  if (input.isEmpty()) {
    expect(list).toBeEmpty()
  } else {
    result.forEach {
      expect(list).toContain(it)
    }
  }
}

private fun fetcher(stringRes: Int): String {
  return when (stringRes) {
    R.string.Album -> "Album"
    R.string.Single -> "Single"
    R.string.EP -> "EP"
    R.string.Broadcast -> "Broadcast"
    R.string.Other -> "Other"
    R.string.Compilation -> "Compilation"
    R.string.Soundtrack -> "Soundtrack"
    R.string.Spoken_word -> "Spoken word"
    R.string.Interview -> "Interview"
    R.string.Audiobook -> "Audiobook"
    R.string.Audio_drama -> "Audio drama"
    R.string.Live -> "Live"
    R.string.Remix -> "Remix"
    R.string.DJ_mix -> "DJ mix"
    R.string.MixtapeSlashStreet -> "Mixtape/Street"
    R.string.Demo -> "Demo"
    else -> "Error"
  }
}

