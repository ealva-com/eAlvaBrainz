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

package com.ealva.ealvabrainz.common

import android.os.Parcelable
import com.ealva.ealvabrainz.common.Formatting.toYear
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.util.Date

/**
 * Represents the year part of a date.
 */
@Parcelize
@JvmInline
public value class Year(public val value: String) : Parcelable {
  public companion object {
    /**
     * Extracts year from [localDate]
     */
    public operator fun invoke(localDate: LocalDate): Year = Year(localDate.format(toYear))

    /**
     * Extracts year from [date]
     */
    public operator fun invoke(date: Date): Year = invoke(date.toLocalDate())

    /**
     * Treats [year] as year only and converts it to a string
     */
    public operator fun invoke(year: Long): Year = Year(year.toString())

    /**
     * Treats [year] as year only and converts it to a string
     */
    public operator fun invoke(year: Int): Year = Year(year.toString())
  }
}
