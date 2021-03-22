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

package com.ealva.brainzsvc.service.search

import com.ealva.ealvabrainz.brainz.data.Work.SearchField
import com.ealva.ealvabrainz.common.ArtistMbid
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.common.Iswc
import com.ealva.ealvabrainz.common.RecordingMbid
import com.ealva.ealvabrainz.common.RecordingTitle
import com.ealva.ealvabrainz.common.WorkMbid
import com.ealva.ealvabrainz.common.WorkName
import com.ealva.ealvabrainz.lucene.BrainzMarker
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.Term
import kotlin.experimental.ExperimentalTypeInference

@OptIn(ExperimentalTypeInference::class)
@BrainzMarker
public class WorkSearch : BaseSearch() {
  @JvmName("aliasTerm")
  @OverloadResolutionByLambdaReturnType
  /** alias	(part of) any alias attached to the work (diacritics are ignored) */
  public inline fun alias(term: () -> Term): Field = add(SearchField.Alias, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun alias(term: () -> String): Field = alias { Term(term()) }

  @JvmName("artistIdTerm")
  @OverloadResolutionByLambdaReturnType
  /** the MBID of an artist related to the work (e.g. a composer or lyricist) */
  public inline fun artistId(term: () -> Term): Field = add(SearchField.ArtistId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun artistId(term: () -> ArtistMbid): Field = artistId { Term(term()) }

  @JvmName("artistTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the name of an artist related to the work (e.g. a composer or lyricist) */
  public inline fun artist(term: () -> Term): Field = add(SearchField.Artist, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun artist(term: () -> ArtistName): Field = artist { Term(term()) }

  @JvmName("commentTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the work's disambiguation comment */
  public inline fun comment(term: () -> Term): Field = add(SearchField.Comment, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun comment(term: () -> String): Field = comment { Term(term()) }

  @JvmName("iswcTerm")
  @OverloadResolutionByLambdaReturnType
  /** any ISWC associated to the work */
  public inline fun iswc(term: () -> Term): Field = add(SearchField.Iswc, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun iswc(term: () -> Iswc): Field = iswc { Term(term().value) }

  @JvmName("languageTerm")
  @OverloadResolutionByLambdaReturnType
  /** the ISO 639-3 code for any of the languages of the work's lyrics */
  public inline fun language(term: () -> Term): Field = add(SearchField.Language, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun language(term: () -> String): Field = language { Term(term()) }

  @JvmName("recordingTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * recording	(part of) the title of a recording related to the work (diacritics are ignored)
   */
  public inline fun recording(term: () -> Term): Field = add(SearchField.Recording, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun recording(term: () -> RecordingTitle): Field = recording { Term(term()) }

  @JvmName("recordingCountTerm")
  @OverloadResolutionByLambdaReturnType
  /** the number of recordings related to the work */
  public inline fun recordingCount(term: () -> Term): Field =
    add(SearchField.RecordingCount, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun recordingCount(term: () -> Int): Field =
    recordingCount { Term(term().toString()) }

  @JvmName("recordingIdTerm")
  @OverloadResolutionByLambdaReturnType
  /** the MBID of a recording related to the work */
  public inline fun recordingId(term: () -> Term): Field = add(SearchField.RecordingId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun recordingId(term: () -> RecordingMbid): Field = recordingId { Term(term()) }

  @JvmName("tagTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) a tag attached to the work */
  public inline fun tag(term: () -> Term): Field = add(SearchField.Tag, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun tag(term: () -> String): Field = tag { Term(term()) }

  @JvmName("typeTerm")
  @OverloadResolutionByLambdaReturnType
  /** the work's type (e.g. "opera", "song", "symphony") */
  public inline fun type(term: () -> Term): Field = add(SearchField.Type, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun type(term: () -> String): Field = type { Term(term()) }

  @JvmName("workAccentTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the work's title (with the specified diacritics) */
  public inline fun workAccent(term: () -> Term): Field = add(SearchField.WorkAccent, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun workAccent(term: () -> String): Field = workAccent { Term(term()) }

  @JvmName("workTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * (part of) the work's title (diacritics are ignored)
   */
  public inline fun work(term: () -> Term): Field = add(SearchField.Work, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun work(term: () -> WorkName): Field = work { Term(term()) }

  @JvmName("workIdTerm")
  @OverloadResolutionByLambdaReturnType
  /** the work's MBID */
  public inline fun workId(term: () -> Term): Field = add(SearchField.WorkId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun workId(term: () -> WorkMbid): Field = workId { Term(term().value) }

  public fun add(field: SearchField, term: Term): Field = makeAndAddField(field.value, term)
}
