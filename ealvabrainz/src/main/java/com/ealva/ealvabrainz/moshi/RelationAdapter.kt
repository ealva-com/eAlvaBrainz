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

package com.ealva.ealvabrainz.moshi

import com.ealva.ealvabrainz.brainz.data.AreaRelation
import com.ealva.ealvabrainz.brainz.data.ArtistRelation
import com.ealva.ealvabrainz.brainz.data.EventRelation
import com.ealva.ealvabrainz.brainz.data.InstrumentRelation
import com.ealva.ealvabrainz.brainz.data.LabelRelation
import com.ealva.ealvabrainz.brainz.data.PlaceRelation
import com.ealva.ealvabrainz.brainz.data.RecordingRelation
import com.ealva.ealvabrainz.brainz.data.Relation
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupRelation
import com.ealva.ealvabrainz.brainz.data.ReleaseRelation
import com.ealva.ealvabrainz.brainz.data.SeriesRelation
import com.ealva.ealvabrainz.brainz.data.UrlRelation
import com.ealva.ealvabrainz.brainz.data.WorkRelation
import com.ealva.ealvabrainz.common.ensureExhaustive
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonAdapter.Factory
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

/** Associate the name found in Json with the adapter classes needed to parse */
private val nameClassPairs = listOf(
  "artist" to ArtistRelation::class.java,
  "place" to PlaceRelation::class.java,
  "event" to EventRelation::class.java,
  "release_group" to ReleaseGroupRelation::class.java,
  "instrument" to InstrumentRelation::class.java,
  "series" to SeriesRelation::class.java,
  "url" to UrlRelation::class.java
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

class RelationAdapter(private val moshi: Moshi) : JsonAdapter<Relation>() {
  override fun fromJson(reader: JsonReader): Relation? {
    reader.peekJson().run {
      setFailOnUnknown(false)
      beginObject()
      while (hasNext()) {
        val index = selectName(options)
        if (index != -1) {
          check(index in indices) {"Name adapter map misconfigured index:$index indices:$indices" }
          return nameClassPairs[index].second.adapter.fromJson(reader)
        } else {
          skipName()
          skipValue()
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
