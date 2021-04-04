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
import com.ealva.ealvabrainz.brainz.data.Place.SearchField.Place
import com.ealva.ealvabrainz.common.BrainzMarker
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
public class PlaceSearch(query: Query = Query()) : BaseSearch<SearchField>(query) {
  @JvmName("addressTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the physical address for this place  */
  public inline fun address(term: () -> Term): Field = add(Address, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun address(crossinline term: () -> String): Field = address { Term(term()) }

  @JvmName("aliasTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * (part of) any [alias](https://musicbrainz.org/doc/Aliases) attached to the place (diacritics
   * are ignored)
   */
  public inline fun alias(term: () -> Term): Field = add(Alias, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun alias(term: () -> String): Field = alias { Term(term()) }

  @JvmName("areaTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the name of the place's main associated area */
  public inline fun area(term: () -> Term): Field = add(Area, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun area(term: () -> String): Field = area { Term(term()) }

  @JvmName("beginDateTerm")
  @OverloadResolutionByLambdaReturnType
  /** the place's begin date (e.g. "1980-01-22") */
  public inline fun beginDate(term: () -> Term): Field = add(SearchField.Begin, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun beginDate(term: () -> LocalDate): Field = beginDate { Term(term()) }

  @JvmName("beginDateOld")
  @OverloadResolutionByLambdaReturnType
  public inline fun beginDate(term: () -> Date): Field = beginDate { Term(term()) }

  /** the artist's disambiguation comment */
  @JvmName("commentTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the place's disambiguation comment */
  public inline fun comment(term: () -> Term): Field = add(SearchField.Comment, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun comment(term: () -> String): Field = comment { Term(term()) }

  @JvmName("defaultTerm")
  @OverloadResolutionByLambdaReturnType
  /** Default searches [Address], [Alias], [Area], and [Place] */
  public inline fun default(term: () -> Term): Field = add(SearchField.Default, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun default(crossinline term: () -> String): Field = default { Term(term()) }

  @JvmName("endDateTerm")
  @OverloadResolutionByLambdaReturnType
  /** the place's end date (e.g. "1980-01-22")  */
  public inline fun endDate(term: () -> Term): Field = add(SearchField.End, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun endDate(term: () -> LocalDate): Field = endDate { Term(term()) }

  @JvmName("endDateOld")
  @OverloadResolutionByLambdaReturnType
  public inline fun endDate(term: () -> Date): Field = endDate { Term(term()) }

  @JvmName("endedTerm")
  @OverloadResolutionByLambdaReturnType
  /** a boolean flag (true/false) indicating whether or not the place has ended (is closed) */
  public inline fun ended(term: () -> Term): Field = add(SearchField.Ended, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun ended(term: () -> Boolean): Field = ended { Term(term()) }

  @JvmName("latitudeTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * the [(WGS 84)](https://en.wikipedia.org/wiki/World_Geodetic_System) latitude of the place's
   * coordinates (e.g. "58.388226")
   */
  public inline fun latitude(term: () -> Term): Field = add(SearchField.Latitude, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun latitude(crossinline term: () -> Double): Field = latitude { Term(term()) }

  @JvmName("longitudeTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * the [(WGS 84)](https://en.wikipedia.org/wiki/World_Geodetic_System) longitude of the place's
   * coordinates (e.g. "58.388226")
   */
  public inline fun longitude(term: () -> Term): Field = add(SearchField.Longitude, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun longitude(crossinline term: () -> Double): Field = longitude { Term(term()) }

  @JvmName("placeTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the place's name (diacritics are ignored) */
  public inline fun place(term: () -> Term): Field = add(Place, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun place(term: () -> PlaceName): Field = place { Term(term()) }

  @JvmName("placeAccentTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the place's name (with the specified diacritics) */
  public inline fun placeAccent(term: () -> Term): Field = add(SearchField.PlaceAccent, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun placeAccent(term: () -> PlaceName): Field = placeAccent { Term(term()) }

  @JvmName("placeIdTerm")
  @OverloadResolutionByLambdaReturnType
  /** the place's MBID */
  public inline fun placeId(term: () -> Term): Field = add(SearchField.PlaceId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun placeId(term: () -> PlaceMbid): Field = placeId { Term(term()) }

  @JvmName("typeTerm")
  @OverloadResolutionByLambdaReturnType
  /** the place's [type](https://musicbrainz.org/doc/Place#Type) */
  public inline fun type(term: () -> Term): Field = add(SearchField.Type, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun type(type: () -> String): Field = type { Term(type()) }

  public companion object {
    public inline operator fun invoke(search: PlaceSearch.() -> Unit): String {
      return PlaceSearch().apply(search).toString()
    }
  }
}
