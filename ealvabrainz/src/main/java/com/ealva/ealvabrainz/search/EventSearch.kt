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

import com.ealva.ealvabrainz.brainz.data.Event
import com.ealva.ealvabrainz.brainz.data.Event.SearchField
import com.ealva.ealvabrainz.brainz.data.Event.SearchField.Alias
import com.ealva.ealvabrainz.brainz.data.Event.SearchField.Artist
import com.ealva.ealvabrainz.brainz.data.AreaMbid
import com.ealva.ealvabrainz.common.AreaName
import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.common.BrainzMarker
import com.ealva.ealvabrainz.brainz.data.EventMbid
import com.ealva.ealvabrainz.common.EventName
import com.ealva.ealvabrainz.brainz.data.PlaceMbid
import com.ealva.ealvabrainz.common.PlaceName
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.Query
import com.ealva.ealvabrainz.lucene.Term
import java.time.LocalDate
import java.util.Date
import kotlin.experimental.ExperimentalTypeInference

@OptIn(ExperimentalTypeInference::class)
@BrainzMarker
public class EventSearch(query: Query = Query()) : BaseSearch<SearchField>(query) {
  @JvmName("aliasTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * (part of) any [alias](https://musicbrainz.org/doc/Aliases) attached to the artist (diacritics
   * are ignored)
   */
  public inline fun alias(term: () -> Term): Field = add(Alias, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun alias(term: () -> String): Field = alias { Term(term()) }

  @JvmName("areaTerm")
  @OverloadResolutionByLambdaReturnType
  /** the name of an area related to the event */
  public inline fun area(term: () -> Term): Field = add(SearchField.AreaName, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun area(term: () -> AreaName): Field = area { Term(term()) }

  @JvmName("areaIdTerm")
  @OverloadResolutionByLambdaReturnType
  /** the MBID of an area related to the event */
  public inline fun areaId(term: () -> Term): Field = add(SearchField.AreaId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun areaId(mbid: () -> AreaMbid): Field = areaId { Term(mbid()) }

  @JvmName("artistTerm")
  @OverloadResolutionByLambdaReturnType
  /** the name of an artist related to the event */
  public inline fun artist(term: () -> Term): Field = add(Artist, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun artist(name: () -> ArtistName): Field = artist { Term(name()) }

  @JvmName("artistIdTerm")
  @OverloadResolutionByLambdaReturnType
  /** the MBID of an artist related to the event */
  public inline fun artistId(term: () -> Term): Field = add(SearchField.ArtistId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun artistId(mbid: () -> ArtistMbid): Field = artistId { Term(mbid()) }

  @JvmName("beginDateTerm")
  @OverloadResolutionByLambdaReturnType
  /** the event's begin date (e.g. "1980-01-22") */
  public inline fun beginDate(term: () -> Term): Field = add(SearchField.Begin, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun beginDate(term: () -> LocalDate): Field = beginDate { Term(term()) }

  @JvmName("beginDateOld")
  @OverloadResolutionByLambdaReturnType
  public inline fun beginDate(term: () -> Date): Field = beginDate { Term(term()) }

  @JvmName("commentTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the event's disambiguation comment */
  public inline fun comment(term: () -> Term): Field = add(SearchField.Comment, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun comment(term: () -> String): Field = comment { Term(term()) }

  @JvmName("defaultTerm")
  @OverloadResolutionByLambdaReturnType
  /** Default searches [Alias], [Artist], and [Event] */
  public inline fun default(term: () -> Term): Field = add(SearchField.Default, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun default(term: () -> String): Field = default { Term(term()) }

  @JvmName("endDateTerm")
  @OverloadResolutionByLambdaReturnType
  /** the event's end date (e.g. "1980-01-22") */
  public inline fun endDate(term: () -> Term): Field = add(SearchField.End, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun endDate(term: () -> LocalDate): Field = endDate { Term(term()) }

  @JvmName("endDateOld")
  @OverloadResolutionByLambdaReturnType
  public inline fun endDate(term: () -> Date): Field = endDate { Term(term()) }

  @JvmName("endedTerm")
  @OverloadResolutionByLambdaReturnType
  /** A boolean flag (true/false) indicating whether or not the event has an end date set */
  public inline fun ended(term: () -> Term): Field = add(SearchField.Ended, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun ended(term: () -> Boolean): Field = ended { Term(term()) }

  @JvmName("eventTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the name of the artist's main associated event */
  public inline fun event(term: () -> Term): Field = add(SearchField.EventName, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun event(term: () -> EventName): Field = event { Term(term()) }

  @JvmName("eventAccentTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the name of the artist's main associated eventAccent */
  public inline fun eventAccent(term: () -> Term): Field = add(SearchField.EventAccent, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun eventAccent(term: () -> EventName): Field = eventAccent { Term(term()) }

  @JvmName("eventIdTerm")
  @OverloadResolutionByLambdaReturnType
  /** the MBID of an event related to the event */
  public inline fun eventId(term: () -> Term): Field = add(SearchField.EventId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun eventId(mbid: () -> EventMbid): Field = eventId { Term(mbid()) }

  @JvmName("placeTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the name of the artist's main associated place */
  public inline fun place(term: () -> Term): Field = add(SearchField.Place, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun place(term: () -> PlaceName): Field = place { Term(term()) }

  @JvmName("placeIdTerm")
  @OverloadResolutionByLambdaReturnType
  /** the MBID of an place related to the place */
  public inline fun placeId(term: () -> Term): Field = add(SearchField.PlaceId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun placeId(mbid: () -> PlaceMbid): Field = placeId { Term(mbid()) }

  @JvmName("tagTerm")
  @OverloadResolutionByLambdaReturnType
  /** a tag attached to the area */
  public inline fun tag(term: () -> Term): Field = add(SearchField.Tag, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun tag(term: () -> String): Field = tag { Term(term()) }

  @JvmName("typeTerm")
  @OverloadResolutionByLambdaReturnType
  /** the area's [type](https://musicbrainz.org/doc/Area#Type)  */
  public inline fun type(term: () -> Term): Field = add(SearchField.Type, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun type(type: () -> String): Field = type { Term(type()) }

  public companion object {
    public inline operator fun invoke(search: EventSearch.() -> Unit): String {
      return EventSearch().apply(search).toString()
    }
  }
}
