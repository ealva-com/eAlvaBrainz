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

package com.ealva.ealvabrainz.lucene

import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.common.AreaMbid
import com.ealva.ealvabrainz.common.ArtistMbid
import com.ealva.ealvabrainz.common.EventMbid
import com.ealva.ealvabrainz.common.Formatting
import com.ealva.ealvabrainz.common.GenreMbid
import com.ealva.ealvabrainz.common.InstrumentMbid
import com.ealva.ealvabrainz.common.LabelMbid
import com.ealva.ealvabrainz.common.PackagingMbid
import com.ealva.ealvabrainz.common.PlaceMbid
import com.ealva.ealvabrainz.common.RecordingMbid
import com.ealva.ealvabrainz.common.ReleaseGroupMbid
import com.ealva.ealvabrainz.common.ReleaseMbid
import com.ealva.ealvabrainz.common.SeriesMbid
import com.ealva.ealvabrainz.common.TrackMbid
import com.ealva.ealvabrainz.common.UrlMbid
import com.ealva.ealvabrainz.common.WorkMbid
import com.ealva.ealvabrainz.common.toAlbumTitle
import com.ealva.ealvabrainz.common.toArtistName
import com.ealva.ealvabrainz.common.toLabelName
import com.ealva.ealvabrainz.common.toRecordingTitle
import com.ealva.ealvabrainz.common.toTrackTitle
import com.nhaarman.expect.expect
import com.nhaarman.expect.fail
import org.junit.Test
import java.text.SimpleDateFormat
import java.time.LocalDate

public class TermTest {
  @Test
  public fun `test term appends correctly`() {
    expect(Term("term").toString()).toBe("term")
    expect(Term("ter?m").toString()).toBe("""ter\?m""")
    expect(Term("ter?m*").toString()).toBe("""ter\?m\*""")
    expect(Term(" &&ter?m*").toString()).toBe("""\&&ter\?m\*""")
    expect(Term(" &&ter?m*^ ").toString()).toBe("""\&&ter\?m\*\^""")
    expect(" &&ter?m*^ ".toTerm().toString()).toBe("""\&&ter\?m\*\^""")
  }

  @Test
  public fun `test phrase appends correctly`() {
    expect(Term(" a phrase ").toString()).toBe(""""a phrase"""")
    expect(Term(" a+phrase else").toString()).toBe(""""a\+phrase else"""")
    expect(" a+phrase else".toTerm()).toBeInstanceOf<Phrase>()
    expect(" a+phrase else".toTerm().toString()).toBe(""""a\+phrase else"""")
  }

  @Test
  public fun `test term and op`() {
    expect((Term("test") and Term("again")).toString()).toBe("(test AND again)")
    expect((Term("test") and Term("again") and Term("again")).toString())
      .toBe("(test AND again AND again)")
    val term = Term("test") and Term("again")
    expect((term and Term("again")).toString()).toBe("(test AND again AND again)")
  }

  @Test
  public fun `test term or op`() {
    expect((Term("test") or Term("again")).toString()).toBe("(test OR again)")
    expect((Term("test") or Term("again") or Term("again")).toString())
      .toBe("(test OR again OR again)")
    val term = Term("test") or Term("again")
    expect((term or Term("again")).toString()).toBe("(test OR again OR again)")
  }

  @Test
  public fun `test term and-or combination`() {
    expect((Term("test") or Term("again") and Term("again")).toString())
      .toBe("((test OR again) AND again)")
    expect((Term("test") and Term("again") or Term("again")).toString())
      .toBe("((test AND again) OR again)")
  }

  @Test
  public fun `test regEx term`() {
    expect("a?b".toRegExTerm().toString()).toBe("""\/a\?b\/""")
    expect("a\\?b".toRegExTerm().toString()).toBe("""\/a\\\?b\/""")
  }

  @Test
  public fun `test require term`() {
    val term = Term("Alice")
    expect((+term).toString()).toBe("""\+Alice""")
    val phrase = Term("One more")
    expect((+phrase).toString()).toBe("""\+"One more"""")
  }

  @Test
  public fun `test prohibit term`() {
    val term = Term("Alice")
    expect((-term).toString()).toBe("""\-Alice""")
    expect((!term).toString()).toBe("""\-Alice""")
    val phrase = Term("One more")
    expect((-phrase).toString()).toBe("""\-"One more"""")
    expect((!phrase).toString()).toBe("""\-"One more"""")
  }

  @Test
  public fun `test fuzzy term`() {
    val term = Term("Bob")
    expect(term.fuzzy().toString()).toBe("""Bob\~2""")
    expect(term.fuzzy(1).toString()).toBe("""Bob\~1""")
  }

  @Test(expected = IllegalArgumentException::class)
  public fun `test phrase fuzzy not allowed`() {
    Term("Bob and Alice").fuzzy(2).also {
      fail("Call to fuzzy should have thrown")
    }
  }

  @Test(expected = IllegalArgumentException::class)
  public fun `test fuzzy max edits`() {
    Term("Bob and Alice").fuzzy(3).also {
      fail("Call to fuzzy should have thrown")
    }
  }

  @Test
  public fun `test proximity phrase`() {
    val term = Term("Bob and Alice")
    expect(term.proximity(1).toString()).toBe(""""Bob and Alice"\~1""")
    expect(term.proximity(10).toString()).toBe(""""Bob and Alice"\~10""")
  }

  @Test(expected = IllegalArgumentException::class)
  public fun `test single term proximity not allowed`() {
    Term("Bob").proximity(3).also {
      fail("Call to proximity should have thrown")
    }
  }

  @Test
  public fun `test boost term`() {
    expect(Term("jakarta").boost(4).toString()).toBe("""jakarta\^4""")
    expect(Term("jakarta apache").boost(4).toString()).toBe(""""jakarta apache"\^4""")
  }

  @Test
  public fun `test inclusive range`() {
    val alice = "Alice".toTerm()
    val bob = "Bob".toTerm()
    expect((alice inclusive bob).toString()).toBe("""\[Alice TO Bob\]""")
    expect((alice to bob).inclusive().toString()).toBe("""\[Alice TO Bob\]""")
  }

  @Test
  public fun `test exclusive range`() {
    val alice = "Alice".toTerm()
    val bob = "Bob".toTerm()
    expect((alice exclusive bob).toString()).toBe("""\{Alice TO Bob\}""")
    expect((alice to bob).exclusive().toString()).toBe("""\{Alice TO Bob\}""")
  }

  @Test
  public fun `test type to Term functions`() {
    val aMbid = "5b11f4ce-a62d-471e-81fc-a69a8278c7da"
    val escapedMbid = aMbid.luceneEscape()
    expect(Term(AreaMbid(aMbid)).toString()).toBe(escapedMbid)
    expect(Term(ArtistMbid(aMbid)).toString()).toBe(escapedMbid)
    expect(Term(EventMbid(aMbid)).toString()).toBe(escapedMbid)
    expect(Term(GenreMbid(aMbid)).toString()).toBe(escapedMbid)
    expect(Term(InstrumentMbid(aMbid)).toString()).toBe(escapedMbid)
    expect(Term(LabelMbid(aMbid)).toString()).toBe(escapedMbid)
    expect(Term(PlaceMbid(aMbid)).toString()).toBe(escapedMbid)
    expect(Term(RecordingMbid(aMbid)).toString()).toBe(escapedMbid)
    expect(Term(ReleaseMbid(aMbid)).toString()).toBe(escapedMbid)
    expect(Term(ReleaseGroupMbid(aMbid)).toString()).toBe(escapedMbid)
    expect(Term(SeriesMbid(aMbid)).toString()).toBe(escapedMbid)
    expect(Term(WorkMbid(aMbid)).toString()).toBe(escapedMbid)
    expect(Term(UrlMbid(aMbid)).toString()).toBe(escapedMbid)
    expect(Term(TrackMbid(aMbid)).toString()).toBe(escapedMbid)
    expect(Term(PackagingMbid(aMbid)).toString()).toBe(escapedMbid)

    val aTitle = "Short (Subtitle)"
    val escapedTitle = """"${aTitle.luceneEscape()}""""
    expect(Term(aTitle.toAlbumTitle()).toString()).toBe(escapedTitle)
    expect(Term(aTitle.toArtistName()).toString()).toBe(escapedTitle)
    expect(Term(aTitle.toLabelName()).toString()).toBe(escapedTitle)
    expect(Term(aTitle.toRecordingTitle()).toString()).toBe(escapedTitle)
    expect(Term(aTitle.toTrackTitle()).toString()).toBe(escapedTitle)

    expect(Term(Release.Type.Album).toString()).toBe(Release.Type.Album.value)
    expect(Term(Release.Status.Official).toString()).toBe(Release.Status.Official.value)

    expect(Term(100).toString()).toBe("100")
    expect(Term(-100).toString()).toBe("\\-100")
    expect(Term(200L).toString()).toBe("200")
    expect(Term(-200L).toString()).toBe("\\-200")
    expect(Term(true).toString()).toBe("true")
    expect(Term(false).toString()).toBe("false")

    val formatter = Formatting.date
    val dateStr = "1963-10-04"
    val localDate = LocalDate.parse(dateStr, formatter)
    expect(Term(localDate).toString()).toBe(dateStr.luceneEscape())

    val simple = SimpleDateFormat("yyyy-MM-dd")
    val date = simple.parse(dateStr)!!
    expect(Term(date).toString()).toBe(dateStr.luceneEscape())
  }
}
