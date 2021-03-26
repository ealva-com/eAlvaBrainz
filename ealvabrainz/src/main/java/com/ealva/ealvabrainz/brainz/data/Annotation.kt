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

import com.squareup.moshi.JsonClass

/**
 * Annotations are text fields, functioning like a miniature wiki, that can be added to any existing
 * artists, labels, recordings, releases, release groups, works...
 *
 * Their purpose is to add information that usually doesn't fit into the strict structural data
 * schema of MusicBrainz (be it due to technical limitations that may be addressed later, or because
 * the information in itself has to be free-text).
 *
 * The content of an annotation can be edited by any MusicBrainz user. Like the rest of the
 * database, if something is incorrect or incomplete, you can fix it. All changes are recorded and
 * if someone deletes or defaces the annotation, you can easily restore a previous copy.
 *
 * [Annotation](https://musicbrainz.org/doc/Annotation)
 */
@JsonClass(generateAdapter = true)
public class Annotation(
  /** The annotated entity's MBID */
  public var entity: String = "",
  /** the annotated entity's name or title */
  public var name: String = "",
  /**
   * The annotated entity's entity type; one of artist, release, release-group, recording, work,
   * label, (track is supported but maps to recording)
   */
  public var type: String = "",
  /**
   * The annotation's content (includes
   * [wiki formatting](https://musicbrainz.org/doc/Annotation#Wiki_formatting))
   */
  public var text: String = "",
  /** score ranking used in query results */
  public var score: Int = 0
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Annotation

    if (entity != other.entity) return false
    if (name != other.name) return false
    if (type != other.type) return false
    if (text != other.text) return false

    return true
  }

  override fun hashCode(): Int {
    var result = entity.hashCode()
    result = 31 * result + name.hashCode()
    result = 31 * result + type.hashCode()
    result = 31 * result + text.hashCode()
    return result
  }

  override fun toString(): String = toJson()

  public enum class SearchField(public override val value: String) : EntitySearchField {
    Default(""),
    /** The annotated entity's MBID */
    Entity("entity"),
    /** The numeric ID of the annotation (e.g. 703027) */
    Id("id"),
    /** The annotated entity's name or title (diacritics are ignored) */
    Name("name"),
    /** The annotation's content (includes wiki formatting) */
    Text("text"),
    /** The annotated entity's entity type */
    Type("type")
  }

  public companion object {
    public val NullAnnotation: Annotation =
      Annotation(entity = NullObject.NAME, name = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> = Annotation::class.java.name to NullAnnotation
  }
}

public inline val Annotation.isNullObject: Boolean
  get() = this === Annotation.NullAnnotation
