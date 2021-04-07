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

@file:Suppress("unused")

package com.ealva.ealvabrainz.search

import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.brainz.data.RecordingMbid
import com.ealva.ealvabrainz.brainz.data.Work.SearchField
import com.ealva.ealvabrainz.brainz.data.Work.SearchField.Alias
import com.ealva.ealvabrainz.brainz.data.Work.SearchField.Work
import com.ealva.ealvabrainz.brainz.data.WorkMbid
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.common.BrainzMarker
import com.ealva.ealvabrainz.common.Iswc
import com.ealva.ealvabrainz.common.RecordingTitle
import com.ealva.ealvabrainz.common.WorkName
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.Query
import com.ealva.ealvabrainz.lucene.Term
import com.ealva.ealvabrainz.search.term.MbidTermBuilder
import com.ealva.ealvabrainz.search.term.NumberTermBuilder
import com.ealva.ealvabrainz.search.term.TermBuilder

@BrainzMarker
public class WorkSearch(query: Query = Query()) : BaseSearch<SearchField>(query) {
  /** alias	(part of) any alias attached to the work (diacritics are ignored) */
  public fun alias(term: String): Field = alias { Term(term) }
  public fun alias(build: TermBuilder.() -> Term): Field =
    add(Alias, TermBuilder().build())

  /** (part of) the name of an artist related to the work (e.g. a composer or lyricist) */
  public fun artist(term: ArtistName): Field = artist { Term(term) }
  public fun artist(build: TermBuilder.() -> Term): Field =
    add(SearchField.Artist, TermBuilder().build())

  /** the MBID of an artist related to the work (e.g. a composer or lyricist) */
  public fun artistId(term: ArtistMbid): Field = artistId { Term(term) }
  public fun artistId(build: MbidTermBuilder<ArtistMbid>.() -> Term): Field =
    add(SearchField.ArtistId, MbidTermBuilder<ArtistMbid>().build())

  /** (part of) the work's disambiguation comment */
  public fun comment(term: String): Field = comment { Term(term) }
  public fun comment(build: TermBuilder.() -> Term): Field =
    add(SearchField.Comment, TermBuilder().build())

  /** Default searches for [Alias] and [Work] */
  public fun default(term: String): Field = default { Term(term) }
  public fun default(build: TermBuilder.() -> Term): Field =
    add(SearchField.Default, TermBuilder().build())

  /** any ISWC associated to the work */
  public fun iswc(term: Iswc): Field = iswc { Term(term) }
  public fun iswc(build: TermBuilder.() -> Term): Field =
    add(SearchField.Iswc, TermBuilder().build())

  /** the ISO 639-3 code for any of the languages of the work's lyrics */
  public fun language(term: String): Field = language { Term(term) }
  public fun language(build: TermBuilder.() -> Term): Field =
    add(SearchField.Language, TermBuilder().build())

  /**
   * (part of) the title of a recording related to the work (diacritics are ignored)
   */
  public fun recording(term: RecordingTitle): Field = recording { Term(term) }
  public fun recording(build: TermBuilder.() -> Term): Field =
    add(SearchField.Recording, TermBuilder().build())

  /** the number of recordings related to the work */
  public fun recordingCount(term: Int): Field = recordingCount { Term(term) }
  public fun recordingCount(build: NumberTermBuilder.() -> Term): Field =
    add(SearchField.RecordingCount, NumberTermBuilder().build())

  /** the MBID of a recording related to the work */
  public fun recordingId(term: RecordingMbid): Field = recordingId { Term(term) }
  public fun recordingId(build: MbidTermBuilder<RecordingMbid>.() -> Term): Field =
    add(SearchField.RecordingId, MbidTermBuilder<RecordingMbid>().build())

  /** (part of) a tag attached to the work */
  public fun tag(build: TermBuilder.() -> Term): Field = add(SearchField.Tag, TermBuilder().build())
  public fun tag(term: String): Field = tag { Term(term) }

  /** the work's type (e.g. "opera", "song", "symphony") */
  public fun type(term: String): Field = type { Term(term) }
  public fun type(build: TermBuilder.() -> Term): Field =
    add(SearchField.Type, TermBuilder().build())

  /**
   * (part of) the work's title (diacritics are ignored)
   */
  public fun work(term: WorkName): Field = work { Term(term) }
  public fun work(term: TermBuilder.() -> Term): Field = add(Work, TermBuilder().term())

  /** (part of) the work's title (with the specified diacritics) */
  public fun workAccent(term: String): Field = workAccent { Term(term) }
  public fun workAccent(build: TermBuilder.() -> Term): Field =
    add(SearchField.WorkAccent, TermBuilder().build())

  /** the work's MBID */
  public fun workId(term: WorkMbid): Field = workId { Term(term) }
  public fun workId(build: MbidTermBuilder<WorkMbid>.() -> Term): Field =
    add(SearchField.WorkId, MbidTermBuilder<WorkMbid>().build())

  public companion object {
    public inline operator fun invoke(search: WorkSearch.() -> Unit): String {
      return WorkSearch().apply(search).toString()
    }
  }
}
