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

import com.ealva.ealvabrainz.brainz.data.Recording
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup
import com.ealva.ealvabrainz.common.AlbumTitle
import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.brainz.data.RecordingMbid
import com.ealva.ealvabrainz.common.RecordingTitle
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupMbid
import com.ealva.ealvabrainz.brainz.data.ReleaseMbid
import com.ealva.ealvabrainz.brainz.data.TrackMbid
import com.ealva.ealvabrainz.common.toLocalDate
import com.ealva.ealvabrainz.lucene.SingleTerm
import com.ealva.ealvabrainz.matchers.expect
import com.ealva.ealvabrainz.matchers.toBeAsString
import com.nhaarman.expect.expect
import org.junit.Test
import java.util.Date

public class RecordingSearchTest {
  @Test
  public fun `test expected search fields`() {
    /*
    https://musicbrainz.org/doc/Indexed_Search_Syntax#Search_Fields_7
    Field	Description
    alias	(part of) any alias attached to the recording (diacritics are ignored)
    arid	the MBID of any of the recording artists
    artist	(part of) the combined credited artist name for the recording, including join phrases
        (e.g. "Artist X feat.")
    artistname	(part of) the name of any of the recording artists
    comment	(part of) the recording's disambiguation comment
    country	the 2-letter code (ISO 3166-1 alpha-2) for the country any release of this recording was
        released in
    creditname	(part of) the credited name of any of the recording artists on this particular
        recording
    date	the release date of any release including this recording (e.g. "1980-01-22")
    dur	the recording duration in milliseconds
    firstreleasedate	the release date of the earliest release including this recording
        (e.g. "1980-01-22")
    format	the format of any medium including this recording (insensitive to case, spaces, and
        separators)
    isrc	any ISRC associated to the recording
    number	the free-text number of the track on any medium including this recording (e.g. "A4")
    position	the position inside its release of any medium including this recording (starts at 1)
    primarytype	the primary type of any release group including this recording
    qdur	the recording duration, quantized (duration in milliseconds / 2000)
    recording	(part of) the recording's name, or the name of a track connected to this recording
        (diacritics are ignored)
    recordingaccent	(part of) the recordings's name, or the name of a track connected to this
        recording (with the specified diacritics)
    reid	the MBID of any release including this recording
    release	(part of) the name of any release including this recording
    rgid	the MBID of any release group including this recording
    rid	the recording's MBID
    secondarytype	any of the secondary types of any release group including this recording
    status	the status of any release including this recording
    tag	(part of) a tag attached to the recording
    tid	the MBID of a track connected to this recording
    tnum	the position of the track on any medium including this recording (starts at 1,
        pre-gaps at 0)
    tracks	the number of tracks on any medium including this recording
    tracksrelease	the number of tracks on any release (as a whole) including this recording
    DEPRECATED type	legacy release group type field that predates the ability to set multiple types
    video	a boolean flag (true/false) indicating whether or not the recording is a video recording
     */
    val values = Recording.SearchField.values()
    val set = values.mapTo(mutableSetOf()) { it.value }
    expect(set).toHaveSize(31)
    expect(set).toContain("")
    expect(set).toContain("alias")
    expect(set).toContain("arid")
    expect(set).toContain("artist")
    expect(set).toContain("artistname")
    expect(set).toContain("comment")
    expect(set).toContain("country")
    expect(set).toContain("creditname")
    expect(set).toContain("date")
    expect(set).toContain("dur")
    expect(set).toContain("firstreleasedate")
    expect(set).toContain("format")
    expect(set).toContain("isrc")
    expect(set).toContain("number")
    expect(set).toContain("position")
    expect(set).toContain("primarytype")
    expect(set).toContain("qdur")
    expect(set).toContain("recording")
    expect(set).toContain("recordingaccent")
    expect(set).toContain("reid")
    expect(set).toContain("release")
    expect(set).toContain("rgid")
    expect(set).toContain("rid")
    expect(set).toContain("secondarytype")
    expect(set).toContain("status")
    expect(set).toContain("tag")
    expect(set).toContain("tid")
    expect(set).toContain("tnum")
    expect(set).toContain("tracks")
    expect(set).toContain("tracksrelease")
    expect(set).toContain("video")
  }

  @Test
  public fun `test all term functions cover all fields`() {
    val value = "a"
    val term = SingleTerm(value)
    val search = RecordingSearch()
    search.alias { term }
    search.artist { term }
    search.artistId { term }
    search.artistName { term }
    search.comment { term }
    search.country { term }
    search.creditName { term }
    search.date { term }
    search.default { term }
    search.duration { term }
    search.firstReleaseDate { term }
    search.format { term }
    search.isrc { term }
    search.number { term }
    search.position { term }
    search.primaryType { term }
    search.quantizedDuration { term }
    search.recording { term }
    search.recordingAccent { term }
    search.recordingId { term }
    search.release { term }
    search.releaseId { term }
    search.releaseGroupId { term }
    search.secondaryType { term }
    search.status { term }
    search.tag { term }
    search.trackId { term }
    search.trackNumber { term }
    search.trackCount { term }
    search.releaseTrackCount { term }
    var result = search.toString()
    Recording.SearchField.values()
      .filterNot { it === Recording.SearchField.Default || it === Recording.SearchField.Video }
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
    val artistId = ArtistMbid("5b11f4ce-a62d-471e-81fc-a69a8278c7da")
    val trackId = TrackMbid("5b11f4ce-a62d-471e-81fc-a69a8278c7da")
    val recordingId = RecordingMbid("5b11f4ce-a62d-471e-81fc-a69a8278c7da")
    val releaseId = ReleaseMbid("5b11f4ce-a62d-471e-81fc-a69a8278c7da")
    val releaseGroupId = ReleaseGroupMbid("5b11f4ce-a62d-471e-81fc-a69a8278c7da")
    expect(RecordingSearch().alias(value)).toBeAsString("alias:$value")
    expect(RecordingSearch().artist(ArtistName(value))).toBeAsString("artist:$value")
    expect(RecordingSearch().artistId(artistId)).toBeAsString("arid:${artistId.value}")
    expect(RecordingSearch().artistName(ArtistName(value))).toBeAsString("artistname:$value")
    expect(RecordingSearch().comment(value)).toBeAsString("comment:$value")
    expect(RecordingSearch().country(value)).toBeAsString("country:$value")
    expect(RecordingSearch().creditName(ArtistName(value))).toBeAsString("creditname:$value")
    expect(RecordingSearch().date(Date(0).toLocalDate())).toBeAsString("date:\"1969-12-31\"")
    expect(RecordingSearch().default(RecordingTitle(value))).toBeAsString(value)
    expect(RecordingSearch().duration(999100)).toBeAsString("dur:999100")
    expect(RecordingSearch().firstReleaseDate(Date(0).toLocalDate()))
      .toBeAsString("firstreleasedate:\"1969-12-31\"")
    expect(RecordingSearch().format(value)).toBeAsString("format:$value")
    expect(RecordingSearch().isrc(value)).toBeAsString("isrc:$value")
    expect(RecordingSearch().number(value)).toBeAsString("number:$value")
    expect(RecordingSearch().position(5)).toBeAsString("position:5")
    expect(RecordingSearch().primaryType(ReleaseGroup.Type.Interview))
      .toBeAsString("primarytype:interview")
    expect(RecordingSearch().quantizedDuration(888555)).toBeAsString("qdur:888555")
    expect(RecordingSearch().recording(RecordingTitle(value))).toBeAsString("recording:$value")
    expect(RecordingSearch().recordingAccent(value)).toBeAsString("recordingaccent:$value")
    expect(RecordingSearch().recordingId(recordingId)).toBeAsString("rid:${recordingId.value}")
    expect(RecordingSearch().release(AlbumTitle(value))).toBeAsString("release:$value")
    expect(RecordingSearch().releaseId(releaseId)).toBeAsString("reid:${releaseId.value}")
    expect(RecordingSearch().releaseGroupId(releaseGroupId))
      .toBeAsString("rgid:${releaseGroupId.value}")
    expect(RecordingSearch().status(Release.Status.Official)).toBeAsString("status:official")
    expect(RecordingSearch().tag(value)).toBeAsString("tag:$value")
    expect(RecordingSearch().trackId(trackId)).toBeAsString("tid:${trackId.value}")
    expect(RecordingSearch().trackNumber(2)).toBeAsString("tnum:2")
    expect(RecordingSearch().trackCount(10)).toBeAsString("tracks:10")
    expect(RecordingSearch().releaseTrackCount(9)).toBeAsString("tracksrelease:9")
    expect(RecordingSearch().video(true)).toBeAsString("video:true")
  }
}
