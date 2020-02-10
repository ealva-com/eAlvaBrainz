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

import com.ealva.ealvabrainz.moshi.FallbackOnNull
import com.ealva.ealvabrainz.moshi.NullPrimitiveAdapter
import com.ealva.ealvabrainz.moshi.ReleaseAdapter
import com.ealva.ealvabrainz.moshi.StringJsonAdapter
import com.squareup.moshi.Moshi

internal fun Moshi.Builder.addRequired(): Moshi.Builder {
  add(ReleaseAdapter.ADAPTER_FACTORY)
  add(FallbackOnNull.ADAPTER_FACTORY)
  add(NullPrimitiveAdapter())
  add(StringJsonAdapter())
  return this
}

val theMoshi: Moshi = Moshi.Builder().addRequired().build()

fun <T : Any> T.toJSon(): String {
  return theMoshi
    .adapter<T>(this::class.java)
    .indent("  ")
    .toJson(this)
}
