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

import com.neovisionaries.i18n.CountryCode

@Suppress("MaxLineLength")
/**
 * Represents a country typically found via the 2 letter ISO code [alpha2]
 */
public interface Country {
  /**
   * Full name or empty string if [isUnknown]
   */
  public val name: String

  /**
   * The 2 letter [ISO 3166-1 alpha-2](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2) code
   *  or empty string if [isUnknown]
   */
  public val alpha2: String

  /**
   * The 3 letter [ISO 3166-1 alpha-3](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-3) code
   *  or empty string if [isUnknown]
   */
  public val alpha3: String

  /**
   * The [ISO 3166-1 numeric](http://en.wikipedia.org/wiki/ISO_3166-1_numeric) code or -1 if
   * [isUnknown] or the code is
   * [exceptionally reserved](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#Exceptional_reservations)
   */
  public val code: Int

  /** The code is unknown/undefined */
  public val isUnknown: Boolean
}

public fun String.toCountry(): Country {
  return CountryImpl(CountryCode.getByCode(this, false) ?: CountryCode.UNDEFINED)
}

private class CountryImpl(private val countryCode: CountryCode) : Country {
  override val name: String
    get() = if (countryCode === CountryCode.UNDEFINED) "" else countryCode.getName()
  override val alpha2: String
    get() = if (countryCode === CountryCode.UNDEFINED) "" else countryCode.name
  override val alpha3: String
    get() = if (countryCode === CountryCode.UNDEFINED) "" else countryCode.alpha3
  override val code: Int
    get() = countryCode.numeric
  override val isUnknown: Boolean
    get() = countryCode === CountryCode.UNDEFINED
}
