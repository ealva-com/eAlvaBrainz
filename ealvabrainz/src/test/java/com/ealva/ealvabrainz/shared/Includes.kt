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

package com.ealva.ealvabrainz.shared

public object Includes {
  public val ALL_RELEASE_RECORDING_INCLUDES: Set<String> = setOf(
    "discids",
    "media",
    "isrcs",
    "artist-credits"
  )

  public val ALL_MISC_INCLUDES: Set<String> = setOf(
    "aliases",
    "annotation",
    "tags",
    "user-tags",
    "ratings",
    "user-ratings",
    "genres",
    "user-genres",
  )

  public val ALL_RELS: Set<String> = setOf(
    "area-rels",
    "artist-rels",
    "event-rels",
    "instrument-rels",
    "label-rels",
    "place-rels",
    "recording-rels",
    "release-rels",
    "release-group-rels",
    "series-rels",
    "url-rels",
    "work-rels",
    "recording-level-rels",
    "work-level-rels",
  )

  public val ALL_TYPES: Set<String> = setOf(
    "album",
    "audio drama",
    "audiobook",
    "broadcast",
    "compilation",
    "demo",
    "dj-mix",
    "ep",
    "interview",
    "live",
    "mixtape/street",
    "other",
    "remix",
    "single",
    "soundtrack",
    "spokenword"
  )

  public val ALL_STATUS: Set<String> = setOf(
    "official",
    "promotion",
    "bootleg",
    "pseudo-release"
  )
}
