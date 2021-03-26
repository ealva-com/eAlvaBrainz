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

import com.ealva.ealvalog.e
import com.ealva.ealvalog.invoke
import com.ealva.ealvalog.lazyLogger
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonAdapter.Factory
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

private val LOG by lazyLogger(CollectionAdapter::class)

/** Associate the name found in Json with the adapter classes needed to parse */
private val nameClassPairs = listOf(
  "area-count" to AreaCollection::class.java,
  "artist-count" to ArtistCollection::class.java,
  "event-count" to EventCollection::class.java,
  "label-count" to LabelCollection::class.java,
  "place-count" to PlaceCollection::class.java,
  "recording-count" to RecordingCollection::class.java,
  "release_group-count" to ReleaseGroupCollection::class.java,
  "release-count" to ReleaseCollection::class.java,
  "work-count" to WorkCollection::class.java
)

/** Let's not construct the range in the loop every time we need it*/
private val indices = nameClassPairs.indices

/**
 * Build options with the same order as [nameClassPairs] so we can use selectName to
 * index into it and find the appropriate adapter class
 */
private val options = JsonReader.Options.of(
  *(nameClassPairs.map { it.first }.toTypedArray())
)

internal class CollectionAdapter(private val moshi: Moshi) : JsonAdapter<Collection>() {
  /**
   * Peek each name/value looking for the relation type. Skip all name/value which aren't recognized
   * as these are just part of the collection info. When the type of relation if found use the
   * correct Json adapter to parse the relation and return it. If we don't recognize a name common
   * to all Collection subtypes we'll log an error as it would indicate a Collection type of which
   * we are unaware. The peeker doesn't actually consume anything in the original reader so we can
   * just hand it off to the correct adapter. Failing to find an adapter we return null (shouldn't
   * happen which is why we log unrecognized names - pointing us to our missing Collection subtype)
   */
  override fun fromJson(reader: JsonReader): Collection? {
    reader.peekJson().use { peeker ->
      peeker.setFailOnUnknown(false) // peeker will be skipping values looking for relation type
      peeker.beginObject()
      while (peeker.hasNext()) {
        val index = peeker.selectName(options)
        if (index != -1) {
          check(index in indices) { "Name adapter map misconfigured index:$index indices:$indices" }
          return nameClassPairs[index].second.adapter.fromJson(reader)
        } else {
          val skippedName = peeker.nextName()
          peeker.skipValue()
          if (!commonCollectionNames.contains(skippedName)) {
            println("Unrecognized name:'$skippedName' in Collection.")
            LOG.e { it("Unrecognized name:'%s' in Collection.", skippedName) }
          }
        }
      }
      return null
    }
  }

  /** Moshi caches adapters so we don't have to */
  private val <T> Class<T>.adapter: JsonAdapter<T>
    get() = moshi.adapter(this)

  override fun toJson(writer: JsonWriter, value: Collection?) {
    if (value == null) {
      writer.nullValue()
    } else {
      when (value) {
        is ArtistCollection -> ArtistCollection::class.java.adapter.toJson(writer, value)
        is PlaceCollection -> PlaceCollection::class.java.adapter.toJson(writer, value)
        is EventCollection -> EventCollection::class.java.adapter.toJson(writer, value)
        is AreaCollection -> AreaCollection::class.java.adapter.toJson(writer, value)
        is LabelCollection -> LabelCollection::class.java.adapter.toJson(writer, value)
        is RecordingCollection -> RecordingCollection::class.java.adapter.toJson(writer, value)
        is ReleaseCollection -> ReleaseCollection::class.java.adapter.toJson(writer, value)
        is ReleaseGroupCollection ->
          ReleaseGroupCollection::class.java.adapter.toJson(writer, value)
        is WorkCollection -> WorkCollection::class.java.adapter.toJson(writer, value)
      }.ensureExhaustive
    }
  }

  companion object {
    val ADAPTER_FACTORY = Factory { type, _, moshi ->
      if (Types.getRawType(type) == Collection::class.java) CollectionAdapter(moshi) else null
    }
  }
}

private val commonCollectionNames = setOf(
  "editor",
  "name",
  "entity-type",
  "type-id",
  "id",
  "type"
)
