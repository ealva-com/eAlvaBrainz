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

import com.ealva.ealvabrainz.brainz.data.Place.SearchField
import com.ealva.ealvabrainz.brainz.data.Place.SearchField.Address
import com.ealva.ealvabrainz.brainz.data.Place.SearchField.Alias
import com.ealva.ealvabrainz.brainz.data.Place.SearchField.Area
import com.ealva.ealvabrainz.brainz.data.Place.SearchField.Begin
import com.ealva.ealvabrainz.brainz.data.Place.SearchField.Comment
import com.ealva.ealvabrainz.brainz.data.Place.SearchField.Default
import com.ealva.ealvabrainz.brainz.data.Place.SearchField.End
import com.ealva.ealvabrainz.brainz.data.Place.SearchField.Ended
import com.ealva.ealvabrainz.brainz.data.Place.SearchField.Latitude
import com.ealva.ealvabrainz.brainz.data.Place.SearchField.Longitude
import com.ealva.ealvabrainz.brainz.data.Place.SearchField.Place
import com.ealva.ealvabrainz.brainz.data.Place.SearchField.PlaceAccent
import com.ealva.ealvabrainz.brainz.data.Place.SearchField.Type
import com.ealva.ealvabrainz.brainz.data.PlaceMbid
import com.ealva.ealvabrainz.common.AreaName
import com.ealva.ealvabrainz.common.BrainzMarker
import com.ealva.ealvabrainz.common.PlaceName
import com.ealva.ealvabrainz.common.Year
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.Query
import com.ealva.ealvabrainz.lucene.Term
import com.ealva.ealvabrainz.search.term.DateTermBuilder
import com.ealva.ealvabrainz.search.term.MbidTermBuilder
import com.ealva.ealvabrainz.search.term.NumberTermBuilder
import com.ealva.ealvabrainz.search.term.TermBuilder
import java.time.LocalDate

@BrainzMarker
public class PlaceSearch(query: Query = Query()) : BaseSearch<SearchField>(query) {
  /** (part of) the physical address for this place  */
  public fun address(term: String): Field = address { Term(term) }
  public fun address(build: TermBuilder.() -> Term): Field = add(Address, TermBuilder().build())

  /**
   * (part of) any [alias](https://musicbrainz.org/doc/Aliases) attached to the place (diacritics
   * are ignored)
   */
  public fun alias(term: String): Field = alias { Term(term) }
  public fun alias(build: TermBuilder.() -> Term): Field = add(Alias, TermBuilder().build())

  /** (part of) the name of the place's main associated area */
  public fun area(term: AreaName): Field = area { Term(term) }
  public fun area(build: TermBuilder.() -> Term): Field = add(Area, TermBuilder().build())

  /** the place's begin date (e.g. "1980-01-22") */
  public fun beginDate(term: LocalDate): Field = beginDate { Term(term) }
  public fun beginDate(term: Year): Field = beginDate { Term(term) }
  public fun beginDate(build: DateTermBuilder.() -> Term): Field =
    add(Begin, DateTermBuilder().build())

  /** (part of) the place's disambiguation comment */
  public fun comment(term: String): Field = comment { Term(term) }
  public fun comment(build: TermBuilder.() -> Term): Field = add(Comment, TermBuilder().build())

  /** Default searches [Address], [Alias], [Area], and [Place] */
  public fun default(name: String): Field = default { Term(name) }
  public fun default(build: TermBuilder.() -> Term): Field = add(Default, TermBuilder().build())

  /** the place's end date (e.g. "1980-01-22")  */
  public fun endDate(term: LocalDate): Field = endDate { Term(term) }
  public fun endDate(term: Year): Field = endDate { Term(term) }
  public fun endDate(build: DateTermBuilder.() -> Term): Field = add(End, DateTermBuilder().build())

  /** a boolean flag (true/false) indicating whether or not the place has ended (is closed) */
  public fun ended(term: Boolean): Field = add(Ended, Term(term))

  /**
   * the [(WGS 84)](https://en.wikipedia.org/wiki/World_Geodetic_System) latitude of the place's
   * coordinates (e.g. "58.388226")
   */
  public fun latitude(term: Double): Field = latitude { Term(term) }
  public fun latitude(build: NumberTermBuilder.() -> Term): Field =
    add(Latitude, NumberTermBuilder().build())

  /**
   * the [(WGS 84)](https://en.wikipedia.org/wiki/World_Geodetic_System) longitude of the place's
   * coordinates (e.g. "58.388226")
   */
  public fun longitude(term: Double): Field = longitude { Term(term) }
  public fun longitude(build: NumberTermBuilder.() -> Term): Field =
    add(Longitude, NumberTermBuilder().build())

  /** (part of) the place's name (diacritics are ignored) */
  public fun place(term: PlaceName): Field = place { Term(term) }
  public fun place(build: TermBuilder.() -> Term): Field = add(Place, TermBuilder().build())

  /** (part of) the place's name (with the specified diacritics) */
  public fun placeAccent(term: PlaceName): Field = placeAccent { Term(term) }
  public fun placeAccent(build: TermBuilder.() -> Term): Field =
    add(PlaceAccent, TermBuilder().build())

  /** the place's MBID */
  public fun placeId(mbid: PlaceMbid): Field = placeId { Term(mbid) }
  public fun placeId(build: MbidTermBuilder<PlaceMbid>.() -> Term): Field =
    add(SearchField.PlaceId, MbidTermBuilder<PlaceMbid>().build())

  /** the place's [type](https://musicbrainz.org/doc/Place#Type) */
  public fun type(type: String): Field = type { Term(type) }
  public fun type(build: TermBuilder.() -> Term): Field = add(Type, TermBuilder().build())

  public companion object {
    public inline operator fun invoke(search: PlaceSearch.() -> Unit): String {
      return PlaceSearch().apply(search).toString()
    }
  }
}
