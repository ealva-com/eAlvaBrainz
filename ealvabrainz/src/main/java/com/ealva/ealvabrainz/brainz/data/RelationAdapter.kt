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

private val LOG by lazyLogger(RelationAdapter::class)

/** Associate the name found in Json with the adapter classes needed to parse */
private val nameClassPairs = listOf(
  "area" to AreaRelation::class.java,
  "artist" to ArtistRelation::class.java,
  "event" to EventRelation::class.java,
  "instrument" to InstrumentRelation::class.java,
  "label" to LabelRelation::class.java,
  "place" to PlaceRelation::class.java,
  "recording" to RecordingRelation::class.java,
  "release_group" to ReleaseGroupRelation::class.java,
  "release" to ReleaseRelation::class.java,
  "series" to SeriesRelation::class.java,
  "url" to UrlRelation::class.java,
  "work" to WorkRelation::class.java
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

internal class RelationAdapter(private val moshi: Moshi) : JsonAdapter<Relation>() {
  /**
   * Peek each name/value looking for the relation type. Skip all name/value which aren't recognized
   * as these are just part of the relation info. When the type of relation if found use the correct
   * Json adapter to parse the relation and return it. If we don't recognize a name common to all
   * Relation subtypes we'll log an error as it would indicate a Relation type of which we are
   * unaware. The peeker doesn't actually consume anything in the original reader so we can just
   * hand it off to the correct adapter. Failing to find an adapter we return null (shouldn't happen
   * which is why we log unrecognized names - pointing us to our missing Relation subtype)
   */
  override fun fromJson(reader: JsonReader): Relation? {
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
          if (!commonRelationNames.contains(skippedName)) {
            println("Unrecognized name:'$skippedName' in Relation.")
            LOG.e { it("Unrecognized name:'%s' in Relation.", skippedName) }
          }
        }
      }
      return null
    }
  }

  /** Moshi caches adapters so we don't have to */
  private val <T> Class<T>.adapter: JsonAdapter<T>
    get() = moshi.adapter(this)

  override fun toJson(writer: JsonWriter, value: Relation?) {
    if (value == null) {
      writer.nullValue()
    } else {
      when (value) {
        is ArtistRelation -> ArtistRelation::class.java.adapter.toJson(writer, value)
        is PlaceRelation -> PlaceRelation::class.java.adapter.toJson(writer, value)
        is EventRelation -> EventRelation::class.java.adapter.toJson(writer, value)
        is AreaRelation -> AreaRelation::class.java.adapter.toJson(writer, value)
        is LabelRelation -> LabelRelation::class.java.adapter.toJson(writer, value)
        is RecordingRelation -> RecordingRelation::class.java.adapter.toJson(writer, value)
        is ReleaseRelation -> ReleaseRelation::class.java.adapter.toJson(writer, value)
        is ReleaseGroupRelation -> ReleaseGroupRelation::class.java.adapter.toJson(writer, value)
        is WorkRelation -> WorkRelation::class.java.adapter.toJson(writer, value)
        is InstrumentRelation -> InstrumentRelation::class.java.adapter.toJson(writer, value)
        is SeriesRelation -> SeriesRelation::class.java.adapter.toJson(writer, value)
        is UrlRelation -> UrlRelation::class.java.adapter.toJson(writer, value)
      }.ensureExhaustive
    }
  }

  companion object {
    val ADAPTER_FACTORY = Factory { type, _, moshi ->
      if (Types.getRawType(type) == Relation::class.java) RelationAdapter(moshi) else null
    }
  }
}

private val commonRelationNames = setOf(
  "type-id",
  "target-type",
  "ordering-key",
  "direction",
  "attributes",
  "attribute-values",
  "attribute-ids",
  "begin",
  "end",
  "ended",
  "source-credit",
  "target-credit"
)
