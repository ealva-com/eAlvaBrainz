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

import com.ealva.ealvabrainz.brainz.data.CdStub.SearchField
import com.ealva.ealvabrainz.brainz.data.CdStub.SearchField.Added
import com.ealva.ealvabrainz.brainz.data.CdStub.SearchField.Artist
import com.ealva.ealvabrainz.brainz.data.CdStub.SearchField.Barcode
import com.ealva.ealvabrainz.brainz.data.CdStub.SearchField.Comment
import com.ealva.ealvabrainz.brainz.data.CdStub.SearchField.Default
import com.ealva.ealvabrainz.brainz.data.CdStub.SearchField.Discid
import com.ealva.ealvabrainz.brainz.data.CdStub.SearchField.Title
import com.ealva.ealvabrainz.brainz.data.CdStub.SearchField.TrackCount
import com.ealva.ealvabrainz.common.AlbumTitle
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.common.BrainzMarker
import com.ealva.ealvabrainz.common.DiscId
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.Query
import com.ealva.ealvabrainz.lucene.Term
import com.ealva.ealvabrainz.search.term.DateTermBuilder
import com.ealva.ealvabrainz.search.term.TermBuilder
import java.time.LocalDate
import kotlin.experimental.ExperimentalTypeInference
import com.ealva.ealvabrainz.search.term.NumberTermBuilder as NumBuilder

@OptIn(ExperimentalTypeInference::class)
@BrainzMarker
public class CdStubSearch(query: Query = Query()) : BaseSearch<SearchField>(query) {
  /** the date the CD stub was added (e.g. "2020-01-22") */
  public fun added(term: LocalDate): Field = added { Term(term) }
  public fun added(build: DateTermBuilder.() -> Term): Field = add(Added, DateTermBuilder().build())

  /** (part of) the artist name set on the CD stub */
  public fun artist(name: ArtistName): Field = artist { Term(name) }
  public fun artist(build: TermBuilder.() -> Term): Field = add(Artist, TermBuilder().build())

  /** the barcode set on the CD stub */
  public fun barcode(term: String): Field = add(Barcode, Term(term))
  public fun barcode(build: TermBuilder.() -> Term): Field = add(Barcode, TermBuilder().build())

  /** (part of) the comment set on the CD stub */
  public fun comment(term: String): Field = comment { Term(term) }
  public fun comment(build: TermBuilder.() -> Term): Field = add(Comment, TermBuilder().build())

  /** Default searches [Artist] and [Title] */
  public fun default(name: String): Field = default { Term(name) }
  public fun default(build: TermBuilder.() -> Term): Field = add(Default, TermBuilder().build())

  /** the CD stub's [Disc ID](https://musicbrainz.org/doc/Disc_ID) */
  public fun discId(term: DiscId): Field = discId { Term(term) }
  public fun discId(build: TermBuilder.() -> Term): Field = add(Discid, TermBuilder().build())

  /** (part of) the release title set on the CD stub */
  public fun title(name: AlbumTitle): Field = title { Term(name) }
  public fun title(term: TermBuilder.() -> Term): Field = add(Title, TermBuilder().term())

  /** the number of tracks on the CD stub */
  public fun trackCount(term: Int): Field = trackCount { Term(term) }
  public fun trackCount(build: NumBuilder.() -> Term): Field = add(TrackCount, NumBuilder().build())

  public companion object {
    public inline operator fun invoke(search: CdStubSearch.() -> Unit): String {
      return CdStubSearch().apply(search).toString()
    }
  }
}
