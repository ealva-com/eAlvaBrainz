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

import com.ealva.ealvabrainz.brainz.data.TextRepresentation.Companion.NullTextRepresentation
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TextRepresentation(
  /**
   * The language a release's track list is written in. The possible values are taken from the
   * [ISO 639-3](https://en.wikipedia.org/wiki/ISO_639-3) standard.
   */
  var language: String = "",
  /**
   * The script used to write the release's track list. The possible values are taken from the
   * [ISO 15924](https://en.wikipedia.org/wiki/ISO_15924) standard.
   *
   * Guide to common scripts
   *
   * * **Latin** (also known as Roman or, incorrectly, "English")
   * Latin is the most common script, and usually the correct choice. It is used for all Western
   * European languages, and many others. It is also the most common script used for
   * transliterations.
   * * **Arabic العربية**
   * The Arabic script is used for languages in the Middle East and Central Asia such as Arabic,
   * Persian and Urdu.
   * * **Cyrillic Кириллица**
   * Cyrillic is used for languages in Eastern Europe such as Russian, Ukrainian, Belarusian and
   * Bulgarian.
   * * **Greek Ελληνικά**
   * The Greek script is used for Greek, but several characters have also been adopted for
   * mathematical uses.
   * * **Han 漢字/汉字**
   * Han characters are used by Chinese, Japanese and Korean. Han (simplified), Han (traditional),
   * Japanese, or Korean should be used instead when the variant is known.
   * * **Han (simplified) 简体字**
   * The simplified variant of Han characters is used to write Chinese in mainland China, Malaysia
   * and Singapore.
   * * **Han (traditional) 繁體字/正體字**
   * The traditional variant of Han characters is used to write Chinese in Hong Kong, Macao and
   * Taiwan.
   * * **Korean 한글**
   * This covers any combination of Hangul and Hanja for Korean.
   * * **Hebrew עברית**
   * The Hebrew script is used for Hebrew, but a few characters have also been adopted for
   * mathematical uses.
   * * **Japanese 漢字 & ひらがな & カタカナ**
   * This covers any combination of Kanji, Hiragana and Katakana for Japanese.
   * * **Katakana カタカナ**
   * Katakana should only be used for transliterations into Japanese (example, English->Japanese).
   * Japanese language titles with words written in Katakana should use Japanese.
   * * **Thai ไทย**
   * The Thai script is used for Thai, as well as some minor languages in south-east Asia.
   */
  var script: String = ""
) {
  companion object {
    val NullTextRepresentation = TextRepresentation()
    val fallbackMapping: Pair<String, Any> =
      TextRepresentation::class.java.name to NullTextRepresentation
  }
}

inline val TextRepresentation.isNullObject: Boolean
  get() = this === NullTextRepresentation
