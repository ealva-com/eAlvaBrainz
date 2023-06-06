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

import com.ealva.ealvabrainz.brainz.data.AreaMbid
import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.brainz.data.Event
import com.ealva.ealvabrainz.brainz.data.Event.SearchField
import com.ealva.ealvabrainz.brainz.data.Event.SearchField.Alias
import com.ealva.ealvabrainz.brainz.data.Event.SearchField.AreaId
import com.ealva.ealvabrainz.brainz.data.Event.SearchField.Artist
import com.ealva.ealvabrainz.brainz.data.Event.SearchField.ArtistId
import com.ealva.ealvabrainz.brainz.data.Event.SearchField.Begin
import com.ealva.ealvabrainz.brainz.data.Event.SearchField.Comment
import com.ealva.ealvabrainz.brainz.data.Event.SearchField.Default
import com.ealva.ealvabrainz.brainz.data.Event.SearchField.End
import com.ealva.ealvabrainz.brainz.data.Event.SearchField.Ended
import com.ealva.ealvabrainz.brainz.data.Event.SearchField.EventAccent
import com.ealva.ealvabrainz.brainz.data.Event.SearchField.EventId
import com.ealva.ealvabrainz.brainz.data.Event.SearchField.Place
import com.ealva.ealvabrainz.brainz.data.Event.SearchField.Tag
import com.ealva.ealvabrainz.brainz.data.Event.SearchField.Type
import com.ealva.ealvabrainz.brainz.data.EventMbid
import com.ealva.ealvabrainz.brainz.data.PlaceMbid
import com.ealva.ealvabrainz.common.AreaName
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.common.BrainzMarker
import com.ealva.ealvabrainz.common.EventName
import com.ealva.ealvabrainz.common.PlaceName
import com.ealva.ealvabrainz.common.Year
import com.ealva.ealvabrainz.lucene.CompoundTerm
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.Query
import com.ealva.ealvabrainz.lucene.Term
import com.ealva.ealvabrainz.search.term.DateTermBuilder
import com.ealva.ealvabrainz.search.term.MbidTermBuilder
import com.ealva.ealvabrainz.search.term.TermBuilder
import java.time.LocalDate

@BrainzMarker
public class EventSearch(query: Query = Query()) : BaseSearch<SearchField>(query) {
  /**
   * (part of) any [alias](https://musicbrainz.org/doc/Aliases) attached to the artist (diacritics
   * are ignored)
   */
  public fun alias(term: String): Field = alias { Term(term) }
  public fun alias(build: TermBuilder.() -> Term): Field = add(Alias, TermBuilder().build())

  /** the name of an area related to the event */
  public fun area(term: AreaName): Field = area { Term(term) }
  public fun area(build: TermBuilder.() -> Term): Field =
    add(SearchField.AreaName, TermBuilder().build())

  /** the MBID of an area related to the event */
  public fun areaId(mbid: AreaMbid): Field = areaId { Term(mbid) }
  public fun areaId(build: MbidTermBuilder<AreaMbid>.() -> Term): Field =
    add(AreaId, MbidTermBuilder<AreaMbid>().build())

  /** the name of an artist related to the event */
  public fun artist(name: ArtistName): Field = artist { Term(name) }
  public fun artist(build: TermBuilder.() -> Term): Field = add(Artist, TermBuilder().build())

  /** the MBID of an artist related to the event */
  public fun artistId(mbid: ArtistMbid): Field = artistId { Term(mbid) }
  public fun artistId(build: MbidTermBuilder<ArtistMbid>.() -> Term): Field =
    add(ArtistId, MbidTermBuilder<ArtistMbid>().build())
  public fun artistIds(operator: String, mbIds: List<ArtistMbid>): Field =
    add(ArtistId, CompoundTerm(operator, mbIds.map { Term(it) }))

  /** the event's begin date (e.g. "1980-01-22") */
  public fun beginDate(term: LocalDate): Field = beginDate { Term(term) }
  public fun beginDate(term: Year): Field = beginDate { Term(term) }
  public fun beginDate(build: DateTermBuilder.() -> Term): Field =
    add(Begin, DateTermBuilder().build())

  /** (part of) the event's disambiguation comment */
  public fun comment(term: String): Field = comment { Term(term) }
  public fun comment(build: TermBuilder.() -> Term): Field = add(Comment, TermBuilder().build())

  /** Default searches [Alias], [Artist], and [Event] */
  public fun default(name: String): Field = default { Term(name) }
  public fun default(build: TermBuilder.() -> Term): Field = add(Default, TermBuilder().build())

  /** the event's end date (e.g. "1980-01-22") */
  public fun endDate(term: LocalDate): Field = endDate { Term(term) }
  public fun endDate(term: Year): Field = endDate { Term(term) }
  public fun endDate(build: DateTermBuilder.() -> Term): Field = add(End, DateTermBuilder().build())

  /** A boolean flag (true/false) indicating whether or not the event has an end date set */
  public fun ended(term: Boolean): Field = add(Ended, Term(term))

  /** (part of) the name of the artist's main associated event */
  public fun event(term: EventName): Field = event { Term(term) }
  public fun event(build: TermBuilder.() -> Term): Field =
    add(SearchField.EventName, TermBuilder().build())

  /** (part of) the name of the artist's main associated eventAccent */
  public fun eventAccent(term: EventName): Field = eventAccent { Term(term) }
  public fun eventAccent(term: TermBuilder.() -> Term): Field =
    add(EventAccent, TermBuilder().term())

  /** the MBID of an event related to the event */
  public fun eventId(mbid: EventMbid): Field = eventId { Term(mbid) }
  public fun eventId(build: MbidTermBuilder<EventMbid>.() -> Term): Field =
    add(EventId, MbidTermBuilder<EventMbid>().build())

  /** (part of) the name of the artist's main associated place */
  public fun place(term: PlaceName): Field = place { Term(term) }
  public fun place(term: TermBuilder.() -> Term): Field = add(Place, TermBuilder().term())

  /** the MBID of an place related to the place */
  public fun placeId(mbid: PlaceMbid): Field = placeId { Term(mbid) }
  public fun placeId(build: MbidTermBuilder<PlaceMbid>.() -> Term): Field =
    add(SearchField.PlaceId, MbidTermBuilder<PlaceMbid>().build())

  /** a tag attached to the area */
  public fun tag(term: String): Field = tag { Term(term) }
  public fun tag(build: TermBuilder.() -> Term): Field = add(Tag, TermBuilder().build())

  /** the area's [type](https://musicbrainz.org/doc/Area#Type)  */
  public fun type(type: String): Field = type { Term(type) }
  public fun type(build: TermBuilder.() -> Term): Field = add(Type, TermBuilder().build())

  public companion object {
    public inline operator fun invoke(search: EventSearch.() -> Unit): String {
      return EventSearch().apply(search).toString()
    }
  }
}
