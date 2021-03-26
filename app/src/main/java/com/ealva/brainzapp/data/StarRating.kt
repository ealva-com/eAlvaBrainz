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

package com.ealva.brainzapp.data

@Suppress("MagicNumber")
private val VALID_STAR_RANGE = 0.0F..5.0F

@JvmInline
value class StarRating(val value: Float) {
  init {
    require(value in VALID_STAR_RANGE)
  }

  companion object {
    /** Prefer this for error correction instead of calling ctor directly */
    fun make(value: Float): StarRating = StarRating(value.coerceIn(VALID_STAR_RANGE))
  }
}

fun Float.toStarRating(): StarRating = StarRating.make(this)
