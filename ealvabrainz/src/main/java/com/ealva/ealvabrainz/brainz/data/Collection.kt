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

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * In the Json sent from the server the different in Collection "types" can be found in 2 locations.
 * First is the value of the entity-type name. Possible values are:
 * * area
 * * artist
 * * editor
 * * event
 * * label
 * * place
 * * recording
 * * release
 * * release-group
 * * work
 *
 * The second location is in the count name. It is named 'type'-count and the value is the number of
 * items in the list. The adapter to read and differentiate these types will peek ahead to determine
 * the true Collection type and then select the appropriate adapter.
 */
public sealed class Collection(
  /** Collection MBID */
  public val id: String,
  /** Name of the collection */
  public val name: String,
  /** Username of the owner of the collection */
  public val editor: String,
  /** The type of entity in the collection */
  @field:Json(name = "entity-type") public val entityType: String,
  /** Descriptive type of the collection. eg. "Attending", "Maybe attending", "Artist" */
  public val type: String,
  @field:Json(name = "type-id") public val typeId: String,
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Collection

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int {
    return id.hashCode()
  }

  public enum class Misc(override val value: String) : Inc {
    UserCollections("user-collections")
  }

  public enum class Browse(override val value: String) : Inc {
    UserCollections("user-collections")
  }
}

@JsonClass(generateAdapter = true)
public class AreaCollection(
  id: String = "",
  name: String = "",
  editor: String = "",
  entityType: String = "",
  type: String = "",
  typeId: String = "",
  @field:Json(name = "area-count") public val areaCount: Int = 0
) : Collection(id, name, editor, entityType, type, typeId) {
  public companion object {
    public val NullAreaCollection: AreaCollection = AreaCollection(name = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> =
      AreaCollection::class.java.name to AreaCollection
  }
}

public val AreaCollection.isNullObject: Boolean
  get() = this === AreaCollection.NullAreaCollection

@JsonClass(generateAdapter = true)
public class ArtistCollection(
  id: String = "",
  name: String = "",
  editor: String = "",
  entityType: String = "",
  type: String = "",
  typeId: String = "",
  @field:Json(name = "artist-count") public val artistCount: Int = 0
) : Collection(id, name, editor, entityType, type, typeId) {
  public companion object {
    public val NullArtistCollection: ArtistCollection = ArtistCollection(name = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> =
      ArtistCollection::class.java.name to ArtistCollection
  }
}

public val ArtistCollection.isNullObject: Boolean
  get() = this === ArtistCollection.NullArtistCollection

@JsonClass(generateAdapter = true)
public class EventCollection(
  id: String = "",
  name: String = "",
  editor: String = "",
  entityType: String = "",
  type: String = "",
  typeId: String = "",
  @field:Json(name = "event-count") public val eventCount: Int = 0
) : Collection(id, name, editor, entityType, type, typeId) {
  public companion object {
    public val NullEventCollection: EventCollection = EventCollection(name = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> =
      EventCollection::class.java.name to EventCollection
  }
}

public val EventCollection.isNullObject: Boolean
  get() = this === EventCollection.NullEventCollection

@JsonClass(generateAdapter = true)
public class LabelCollection(
  id: String = "",
  name: String = "",
  editor: String = "",
  entityType: String = "",
  type: String = "",
  typeId: String = "",
  @field:Json(name = "label-count") public val labelCount: Int = 0
) : Collection(id, name, editor, entityType, type, typeId) {
  public companion object {
    public val NullLabelCollection: LabelCollection = LabelCollection(name = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> =
      LabelCollection::class.java.name to LabelCollection
  }
}

public val LabelCollection.isNullObject: Boolean
  get() = this === LabelCollection.NullLabelCollection

@JsonClass(generateAdapter = true)
public class PlaceCollection(
  id: String = "",
  name: String = "",
  editor: String = "",
  entityType: String = "",
  type: String = "",
  typeId: String = "",
  @field:Json(name = "place-count") public val placeCount: Int = 0
) : Collection(id, name, editor, entityType, type, typeId) {
  public companion object {
    public val NullPlaceCollection: PlaceCollection = PlaceCollection(name = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> =
      PlaceCollection::class.java.name to PlaceCollection
  }
}

public val PlaceCollection.isNullObject: Boolean
  get() = this === PlaceCollection.NullPlaceCollection

@JsonClass(generateAdapter = true)
public class RecordingCollection(
  id: String = "",
  name: String = "",
  editor: String = "",
  entityType: String = "",
  type: String = "",
  typeId: String = "",
  @field:Json(name = "recording-count") public val recordingCount: Int = 0
) : Collection(id, name, editor, entityType, type, typeId) {
  public companion object {
    public val NullRecordingCollection: RecordingCollection =
      RecordingCollection(name = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> =
      RecordingCollection::class.java.name to RecordingCollection
  }
}

public val RecordingCollection.isNullObject: Boolean
  get() = this === RecordingCollection.NullRecordingCollection

@JsonClass(generateAdapter = true)
public class ReleaseCollection(
  id: String = "",
  name: String = "",
  editor: String = "",
  entityType: String = "",
  type: String = "",
  typeId: String = "",
  @field:Json(name = "release-count") public val releaseCount: Int = 0
) : Collection(id, name, editor, entityType, type, typeId) {
  public companion object {
    public val NullReleaseCollection: ReleaseCollection = ReleaseCollection(name = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> =
      ReleaseCollection::class.java.name to ReleaseCollection
  }
}

public val ReleaseCollection.isNullObject: Boolean
  get() = this === ReleaseCollection.NullReleaseCollection

@JsonClass(generateAdapter = true)
public class ReleaseGroupCollection(
  id: String = "",
  name: String = "",
  editor: String = "",
  entityType: String = "",
  type: String = "",
  typeId: String = "",
  @field:Json(name = "release_group-count") public val releaseGroupCount: Int = 0
) : Collection(id, name, editor, entityType, type, typeId) {
  public companion object {
    public val NullReleaseGroupCollection: ReleaseGroupCollection =
      ReleaseGroupCollection(name = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> =
      ReleaseGroupCollection::class.java.name to ReleaseGroupCollection
  }
}

public val ReleaseGroupCollection.isNullObject: Boolean
  get() = this === ReleaseGroupCollection.NullReleaseGroupCollection

@JsonClass(generateAdapter = true)
public class WorkCollection(
  id: String = "",
  name: String = "",
  editor: String = "",
  entityType: String = "",
  type: String = "",
  typeId: String = "",
  @field:Json(name = "work-count") public val workCount: Int = 0
) : Collection(id, name, editor, entityType, type, typeId) {
  public companion object {
    public val NullWorkCollection: WorkCollection = WorkCollection(name = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> =
      WorkCollection::class.java.name to WorkCollection
  }
}

public val WorkCollection.isNullObject: Boolean
  get() = this === WorkCollection.NullWorkCollection
