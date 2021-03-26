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

package com.ealva.ealvabrainz.brainz.data

/**
 * You can request relationships with the appropriate includes. These will load relationships
 * between the requested entity and the specific entity type. For example, if you request
 * "work-rels" when looking up an artist, you'll get all the relationships between this artist and
 * any works, and if you request "artist-rels" you'll get the relationships between this artist and
 * any other artists. As such, keep in mind requesting "artist-rels" for an artist, "release-rels"
 * for a release, etc. will not load all the relationships for the entity, just the ones to other
 * entities of the same type.
 *
 * If you request work-level-rels for a recording, you will still need to request work-rels (to get
 * the relationship from the recording to the work in the first place) and any other relationship
 * types you want to see (for example, artist-rels if you want to see work-artist relationships).
 *
 * With relationships included, entities will include a list of [Relation]s
 *
 * [MusicBrainz Relationships](https://musicbrainz.org/doc/MusicBrainz_API#Relationships)
 */
public enum class Relationships(override val value: String) : Inc {
  Area("area-rels"),
  Artist("artist-rels"),
  Event("event-rels"),
  Instrument("instrument-rels"),
  Label("label-rels"),
  Place("place-rels"),
  Recording("recording-rels"),
  Release("release-rels"),
  ReleaseGroup("release-group-rels"),
  Series("series-rels"),
  Url("url-rels"),
  Work("work-rels"),
  RecordingLevel("recording-level-rels"),
  WorkLevel("work-level-rels")
}
