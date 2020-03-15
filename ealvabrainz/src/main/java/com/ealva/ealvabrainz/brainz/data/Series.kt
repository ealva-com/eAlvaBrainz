/*
 * Copyright (c) 2020  Eric A. Snell
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

package com.ealva.ealvabrainz.brainz.data

import com.squareup.moshi.JsonClass
import timber.log.Timber

/**
 * ```json
 * "series": {
 *     "name": "Rolling Stone: 500 Greatest Albums of All Time: 2012 edition",
 *     "id": "8668518f-4a1e-4802-8b0d-81703ced6418",
 *     "disambiguation": ""
 * },
 * ```
 */
@JsonClass(generateAdapter = true)
class Series(
  var id: String = "",
  var name: String = "",
  var disambiguation: String = ""
) {
  interface Lookup : Include

  @Suppress("unused")
  enum class SearchField(val value: String) {
   /** an alias attached to the series */
   Alias("alias"),
   /** the disambiguation comment for the series */
   Comment("comment"),
   /** the name of the series */
   Series("series"),
   /** the MBID of the series */
   SeriesId("sid"),
   /** the series' type */
   Type("type"),
   /** a tag attached to the series */
   Tag("tag"),
  }

  /**
   * Series relationships
   *
   * * [Series-Series](https://musicbrainz.org/relationships/series-series)
   * * [Series-URL](https://musicbrainz.org/relationships/series-url)
   * * [Series-Work](https://musicbrainz.org/relationships/series-work)
   */
  enum class Relations(override val value: String) : Lookup {
    Series("series-rels"),
    Url("url-rels"),
    Work("work-rels")
  }

  companion object {
    val NullSeries = Series(NullObject.NAME, NullObject.ID)
    val fallbackMapping: Pair<String, Any> = Series::class.java.name to NullSeries
  }
}

inline val Series.isNullObject: Boolean
  get() = this === Series.NullSeries

inline class SeriesMbid(override val value: String) : Mbid

inline val Series.mbid: SeriesMbid
  get() = id.toSeriesMbid()

@Suppress("NOTHING_TO_INLINE")
inline fun String.toSeriesMbid(): SeriesMbid {
  if (Mbid.logInvalidMbid && isInvalidMbid()) Timber.w("Invalid SeriesMbid")
  return SeriesMbid(this)
}


