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

package com.ealva.ealvabrainz.common

/**
 * Put this at the end of a when statement to ensure it's exhaustive. Especially sealed classes
 *
 * For example:
 * ```kotlin
 * when (value) {
 *   is ArtistRelation -> ArtistRelation::class.java.adapter.toJson(writer, value)
 *   is PlaceRelation -> PlaceRelation::class.java.adapter.toJson(writer, value)
 *   is EventRelation -> EventRelation::class.java.adapter.toJson(writer, value)
 *   is AreaRelation -> AreaRelation::class.java.adapter.toJson(writer, value)
 *   is LabelRelation -> LabelRelation::class.java.adapter.toJson(writer, value)
 *   is RecordingRelation -> RecordingRelation::class.java.adapter.toJson(writer, value)
 *   is ReleaseRelation -> ReleaseRelation::class.java.adapter.toJson(writer, value)
 *   is ReleaseGroupRelation -> ReleaseGroupRelation::class.java.adapter.toJson(writer, value)
 *   is WorkRelation -> WorkRelation::class.java.adapter.toJson(writer, value)
 *   is InstrumentRelation -> InstrumentRelation::class.java.adapter.toJson(writer, value)
 *   is SeriesRelation -> SeriesRelation::class.java.adapter.toJson(writer, value)
 *   is UrlRelation -> UrlRelation::class.java.adapter.toJson(writer, value)
 * }.ensureExhaustive
 * ```
 */
inline val <T> T.ensureExhaustive: T
  get() = this
