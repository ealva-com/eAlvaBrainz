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

package com.ealva.ealvabrainz.search

import com.ealva.ealvabrainz.brainz.data.Work
import com.ealva.ealvabrainz.common.ArtistMbid
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.common.Iswc
import com.ealva.ealvabrainz.common.RecordingMbid
import com.ealva.ealvabrainz.common.RecordingTitle
import com.ealva.ealvabrainz.common.WorkMbid
import com.ealva.ealvabrainz.common.WorkName
import com.ealva.ealvabrainz.lucene.SingleTerm
import com.ealva.ealvabrainz.matchers.expect
import com.ealva.ealvabrainz.matchers.toBeAsString
import com.nhaarman.expect.expect
import org.junit.Test

public class WorkSearchTest {
  @Test
  public fun `test expected search fields`() {
    /*
    https://musicbrainz.org/doc/Indexed_Search_Syntax#Search_Fields_11
    Field	Description
    alias	(part of) any alias attached to the work (diacritics are ignored)
    arid	the MBID of an artist related to the event (e.g. a composer or lyricist)
    artist	(part of) the name of an artist related to the work (e.g. a composer or lyricist)
    comment	(part of) the work's disambiguation comment
    iswc	any ISWC associated to the work
    lang	the ISO 639-3 code for any of the languages of the work's lyrics
    recording	(part of) the title of a recording related to the work
    recording_count	the number of recordings related to the work
    rid	the MBID of a recording related to the work
    tag	(part of) a tag attached to the work
    type	the work's type (e.g. "opera", "song", "symphony")
    wid	the work's MBID
    work	(part of) the work's title (diacritics are ignored)
    workaccent	(part of) the work's title (with the specified diacritics)
     */
    val values = Work.SearchField.values()
    val set = values.mapTo(mutableSetOf()) { it.value }
    expect(set).toHaveSize(15)
    expect(set).toContain("")
    expect(set).toContain("alias")
    expect(set).toContain("arid")
    expect(set).toContain("artist")
    expect(set).toContain("comment")
    expect(set).toContain("iswc")
    expect(set).toContain("lang")
    expect(set).toContain("recording")
    expect(set).toContain("recording_count")
    expect(set).toContain("rid")
    expect(set).toContain("tag")
    expect(set).toContain("type")
    expect(set).toContain("wid")
    expect(set).toContain("work")
    expect(set).toContain("workaccent")
  }

  @Test
  public fun `test all term functions cover all fields`() {
    val value = "a"
    val term = SingleTerm(value)
    val search = WorkSearch()
    search.alias { term }
    search.artist { term }
    search.artistId { term }
    search.comment { term }
    search.default { term }
    search.iswc { term }
    search.language { term }
    search.recording { term }
    search.recordingCount { term }
    search.recordingId { term }
    search.tag { term }
    search.type { term }
    search.work { term }
    search.workAccent { term }
    search.workId { term }
    var result = search.toString()
    Work.SearchField.values()
      .filterNot { it === Work.SearchField.Default }
      .forEach { searchField ->
        val expected = "${searchField.value}:$value"
        expect(result).toContain(expected)
        result = result.replaceFirst(expected, "")
      }
    expect(result.trim()).toBe(value)
  }

  @Test
  public fun `test all non-term functions add expected field`() {
    val value = "a"
    val workMbid = WorkMbid("5b11f4ce-a62d-471e-81fc-a69a8278c7da")
    val recordingMbid = RecordingMbid("5b11f4ce-a62d-471e-81fc-a69a8278c7da")
    val artistMbid = ArtistMbid("5b11f4ce-a62d-471e-81fc-a69a8278c7da")
    expect(WorkSearch().alias { value }).toBeAsString("alias:$value")
    expect(WorkSearch().artist { ArtistName(value) }).toBeAsString("artist:$value")
    expect(WorkSearch().artistId { artistMbid }).toBeAsString("arid:${artistMbid.value}")
    expect(WorkSearch().comment { value }).toBeAsString("comment:$value")
    expect(WorkSearch().default { value }).toBeAsString(value)
    expect(WorkSearch().iswc { Iswc(value) }).toBeAsString("iswc:$value")
    expect(WorkSearch().language { value }).toBeAsString("lang:$value")
    expect(WorkSearch().recording { RecordingTitle(value) }).toBeAsString("recording:$value")
    expect(WorkSearch().recordingCount { 77 }).toBeAsString("recording_count:77")
    expect(WorkSearch().recordingId { recordingMbid }).toBeAsString("rid:${recordingMbid.value}")
    expect(WorkSearch().tag { value }).toBeAsString("tag:$value")
    expect(WorkSearch().type { value }).toBeAsString("type:$value")
    expect(WorkSearch().work { WorkName(value) }).toBeAsString("work:$value")
    expect(WorkSearch().workAccent { value }).toBeAsString("workaccent:$value")
    expect(WorkSearch().workId { workMbid }).toBeAsString("wid:${workMbid.value}")
  }
}
