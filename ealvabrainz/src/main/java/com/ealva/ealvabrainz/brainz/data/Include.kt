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

/**
 * Represents a single part of a MusicBrainz inc= parameter. An Include allows you to request
 * more information to be included about an entity
 */
public interface Include {
  public val value: String
}

/**
 * Join each [Include] instance into an inc= value for MusicBrainz lookup.
 *
 * @return list entries concatenated together separated by "+" as required by MusicBrainz Lucene
 * query, or null if the list isEmpty
 */
public fun List<Include>.join(): String? {
  return if (isEmpty()) null else joinToString("+") { it.value }
}

public fun Set<Include>.join(): String? {
  return if (isEmpty()) null else joinToString("+") { it.value }
}

// All possible includes, some do not apply to all entities
// discids+media+isrcs+artist-credits+various-artists+aliases+annotation+tags+ratings+genres
