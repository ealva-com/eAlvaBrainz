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
import com.ealva.ealvabrainz.common.AlbumTitle
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.common.DiscId
import com.ealva.ealvabrainz.common.BrainzMarker
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.Term
import java.time.LocalDate
import java.util.Date
import kotlin.experimental.ExperimentalTypeInference

@OptIn(ExperimentalTypeInference::class)
@BrainzMarker
public class CdStubSearch : BaseSearch<SearchField>() {
  @JvmName("addedTerm")
  @OverloadResolutionByLambdaReturnType
  /** the date the CD stub was added (e.g. "2020-01-22") */
  public inline fun added(term: () -> Term): Field = add(SearchField.Added, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun added(term: () -> LocalDate): Field = added { Term(term()) }

  @JvmName("addedOld")
  @OverloadResolutionByLambdaReturnType
  public inline fun added(term: () -> Date): Field = added { Term(term()) }

  @JvmName("artistTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the artist name set on the CD stub */
  public inline fun artist(term: () -> Term): Field = add(SearchField.Artist, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun artist(name: () -> ArtistName): Field = artist { Term(name()) }

  @JvmName("barcodeTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun barcode(term: () -> Term): Field = add(SearchField.Barcode, term())

  @OverloadResolutionByLambdaReturnType
  /** the barcode set on the CD stub */
  public inline fun barcode(term: () -> String): Field =
    add(SearchField.Barcode, Term(term()))

  @JvmName("commentTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the comment set on the CD stub */
  public inline fun comment(term: () -> Term): Field = add(SearchField.Comment, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun comment(term: () -> String): Field = comment { Term(term()) }

  @JvmName("discIdTerm")
  @OverloadResolutionByLambdaReturnType
  /** the CD stub's Disc ID */
  public inline fun discId(term: () -> Term): Field = add(SearchField.Comment, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun discId(term: () -> DiscId): Field = discId { Term(term()) }

  @JvmName("defaultTerm")
  @OverloadResolutionByLambdaReturnType
  /** the title (diacritics are ignored) */
  public fun default(term: () -> Term): Field = add(SearchField.Default, term())

  @OverloadResolutionByLambdaReturnType
  public fun default(name: () -> AlbumTitle): Field = default { Term(name()) }

  @JvmName("titleTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the release title set on the CD stub */
  public fun title(term: () -> Term): Field = add(SearchField.Title, term())

  @OverloadResolutionByLambdaReturnType
  public fun title(name: () -> AlbumTitle): Field = title { Term(name()) }

  /** the number of tracks on the CD stub */
  @JvmName("tracksTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun trackCount(term: () -> Term): Field = add(SearchField.TrackCount, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun trackCount(term: () -> Int): Field = trackCount { Term(term()) }

  public companion object {
    public inline operator fun invoke(search: CdStubSearch.() -> Unit): String {
      return CdStubSearch().apply(search).toString()
    }
  }
}
