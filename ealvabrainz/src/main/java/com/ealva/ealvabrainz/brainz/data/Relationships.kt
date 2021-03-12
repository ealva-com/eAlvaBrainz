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

@Suppress("unused")
public enum class Relationships(override val value: String) : Include {
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
