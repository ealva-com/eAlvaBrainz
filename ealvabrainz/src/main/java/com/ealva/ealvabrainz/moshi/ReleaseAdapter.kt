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

import com.ealva.ealvabrainz.brainz.data.Alias
import com.ealva.ealvabrainz.brainz.data.ArtistCredit
import com.ealva.ealvabrainz.brainz.data.CoverArtArchive
import com.ealva.ealvabrainz.brainz.data.Genre
import com.ealva.ealvabrainz.brainz.data.LabelInfo
import com.ealva.ealvabrainz.brainz.data.Medium
import com.ealva.ealvabrainz.brainz.data.Packaging
import com.ealva.ealvabrainz.brainz.data.Rating
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.ReleaseEvent
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup
import com.ealva.ealvabrainz.brainz.data.Tag
import com.ealva.ealvabrainz.brainz.data.TextRepresentation
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonAdapter.Factory
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.internal.Util
import java.lang.reflect.Constructor

/**
 * Custom JsonAdapter<Release>() copied from Moshi generated adapter and modified to handle
 * "packaging" as either a string or a [Packaging] object. If an object, it's parsed and broken
 * into the separate strings [Release.packaging] and [Release.packagingId]
 *
 * When a Release is contained in an Artist, the "packaging" is a string, eg.
 * ```
 *   "packaging": "Box",
 * ```
 * When a Release appears in a [com.ealva.ealvabrainz.brainz.data.ReleaseList] it is an object, eg.
 * ```
 * "packaging": {
 *   "id": "ec27701a-4a22-37f4-bfac-6616e0f9750a",
 *   "name": "Jewel Case"
 * },
 * ```
 */
class ReleaseAdapter(moshi: Moshi) : JsonAdapter<Release>() {

  private val options: JsonReader.Options = JsonReader.Options.of(
    "id", "title", "date",
    "release-group", "release-events", "artist-credit", "status", "packaging", "packaging-id",
    "barcode", "cover-art-archive", "disambiguation", "quality", "country", "asin", "media",
    "label-info", "track-count", "status-id", "text-representation", "aliases", "annotation",
    "genres", "tags", "primary-type-id", "secondary-types", "secondary-type-ids",
    "first-release-date", "rating", "primary-type", "releases", "score"
  )

  private val stringAdapter: JsonAdapter<String> = moshi.adapter(
    String::class.java, emptySet(),
    "id"
  )

  private val packagingAdapter: JsonAdapter<Packaging> = moshi.adapter(
    Packaging::class.java,
    emptySet(), "packaging"
  )

  @field:FallbackOnNull
  private val releaseGroupAtFallbackOnNullAdapter: JsonAdapter<ReleaseGroup> =
    moshi.adapter(
      ReleaseGroup::class.java, Types.getFieldJsonQualifierAnnotations(
        javaClass,
        "releaseGroupAtFallbackOnNullAdapter"
      ), "releaseGroup"
    )

  private val listOfReleaseEventAdapter: JsonAdapter<List<ReleaseEvent>> =
    moshi.adapter(
      Types.newParameterizedType(List::class.java, ReleaseEvent::class.java),
      emptySet(), "releaseEvents"
    )

  private val listOfArtistCreditAdapter: JsonAdapter<List<ArtistCredit>> =
    moshi.adapter(
      Types.newParameterizedType(List::class.java, ArtistCredit::class.java),
      emptySet(), "artistCredit"
    )

  @field:FallbackOnNull
  private val coverArtArchiveAtFallbackOnNullAdapter: JsonAdapter<CoverArtArchive> =
    moshi.adapter(
      CoverArtArchive::class.java, Types.getFieldJsonQualifierAnnotations(
        javaClass,
        "coverArtArchiveAtFallbackOnNullAdapter"
      ), "coverArtArchive"
    )

  private val listOfMediumAdapter: JsonAdapter<List<Medium>> =
    moshi.adapter(
      Types.newParameterizedType(List::class.java, Medium::class.java), emptySet(),
      "media"
    )

  private val listOfLabelInfoAdapter: JsonAdapter<List<LabelInfo>> =
    moshi.adapter(
      Types.newParameterizedType(List::class.java, LabelInfo::class.java), emptySet(),
      "labelInfo"
    )

  private val intAdapter: JsonAdapter<Int> = moshi.adapter(
    Int::class.java, emptySet(),
    "trackCount"
  )

  @field:FallbackOnNull
  private val textRepresentationAtFallbackOnNullAdapter: JsonAdapter<TextRepresentation> =
    moshi.adapter(
      TextRepresentation::class.java,
      Types.getFieldJsonQualifierAnnotations(
        javaClass,
        "textRepresentationAtFallbackOnNullAdapter"
      ), "textRepresentation"
    )

  private val listOfAliasAdapter: JsonAdapter<List<Alias>> =
    moshi.adapter(
      Types.newParameterizedType(List::class.java, Alias::class.java), emptySet(),
      "aliases"
    )

  private val listOfGenreAdapter: JsonAdapter<List<Genre>> =
    moshi.adapter(
      Types.newParameterizedType(List::class.java, Genre::class.java), emptySet(),
      "genres"
    )

  private val listOfTagAdapter: JsonAdapter<List<Tag>> =
    moshi.adapter(
      Types.newParameterizedType(List::class.java, Tag::class.java), emptySet(),
      "tags"
    )

  private val listOfStringAdapter: JsonAdapter<List<String>> =
    moshi.adapter(
      Types.newParameterizedType(List::class.java, String::class.java), emptySet(),
      "secondaryTypes"
    )

  @field:FallbackOnNull
  private val ratingAtFallbackOnNullAdapter: JsonAdapter<Rating> = moshi.adapter(
    Rating::class.java,
    Types.getFieldJsonQualifierAnnotations(javaClass, "ratingAtFallbackOnNullAdapter"), "rating"
  )

  private val listOfReleaseAdapter: JsonAdapter<List<Release>> =
    moshi.adapter(
      Types.newParameterizedType(List::class.java, Release::class.java), emptySet(),
      "releases"
    )

  @Volatile
  private var constructorRef: Constructor<Release>? = null

  override fun toString(): String = buildString(29) {
    append("GeneratedJsonAdapter(").append("Release").append(')')
  }

  override fun fromJson(reader: JsonReader): Release {
    var id: String? = null
    var title: String? = null
    var date: String? = null
    var releaseGroup: ReleaseGroup? = null
    var releaseEvents: List<ReleaseEvent>? = null
    var artistCredit: List<ArtistCredit>? = null
    var status: String? = null
    var packaging: String? = null
    var packagingId: String? = null
    var barcode: String? = null
    var coverArtArchive: CoverArtArchive? = null
    var disambiguation: String? = null
    var quality: String? = null
    var country: String? = null
    var asin: String? = null
    var media: List<Medium>? = null
    var labelInfo: List<LabelInfo>? = null
    var trackCount: Int? = 0
    var statusId: String? = null
    var textRepresentation: TextRepresentation? = null
    var aliases: List<Alias>? = null
    var annotation: String? = null
    var genres: List<Genre>? = null
    var tags: List<Tag>? = null
    var primaryTypeId: String? = null
    var secondaryTypes: List<String>? = null
    var secondaryTypeIds: List<String>? = null
    var firstReleaseDate: String? = null
    var rating: Rating? = null
    var primaryType: String? = null
    var releases: List<Release>? = null
    var score: Int? = 0
    var mask0 = -1
    reader.beginObject()
    while (reader.hasNext()) {
      when (reader.selectName(options)) {
        0 -> {
          id = stringAdapter.fromJson(reader) ?: throw Util.unexpectedNull("id", "id", reader)
          // $mask = $mask and (1 shl 0).inv()
          mask0 = mask0 and 0xfffffffe.toInt()
        }
        1 -> {
          title = stringAdapter.fromJson(reader) ?: throw Util.unexpectedNull(
            "title", "title",
            reader
          )
          // $mask = $mask and (1 shl 1).inv()
          mask0 = mask0 and 0xfffffffd.toInt()
        }
        2 -> {
          date = stringAdapter.fromJson(reader) ?: throw Util.unexpectedNull("date", "date", reader)
          // $mask = $mask and (1 shl 2).inv()
          mask0 = mask0 and 0xfffffffb.toInt()
        }
        3 -> {
          releaseGroup = releaseGroupAtFallbackOnNullAdapter.fromJson(reader)
            ?: throw Util.unexpectedNull("releaseGroup", "release-group", reader)
          // $mask = $mask and (1 shl 3).inv()
          mask0 = mask0 and 0xfffffff7.toInt()
        }
        4 -> {
          releaseEvents = listOfReleaseEventAdapter.fromJson(reader) ?: throw Util.unexpectedNull(
            "releaseEvents",
            "release-events",
            reader
          )
          // $mask = $mask and (1 shl 4).inv()
          mask0 = mask0 and 0xffffffef.toInt()
        }
        5 -> {
          artistCredit = listOfArtistCreditAdapter.fromJson(reader) ?: throw Util.unexpectedNull(
            "artistCredit",
            "artist-credit",
            reader
          )
          // $mask = $mask and (1 shl 5).inv()
          mask0 = mask0 and 0xffffffdf.toInt()
        }
        6 -> {
          status = stringAdapter.fromJson(reader) ?: throw Util.unexpectedNull(
            "status", "status",
            reader
          )
          // $mask = $mask and (1 shl 6).inv()
          mask0 = mask0 and 0xffffffbf.toInt()
        }
        7 -> {
          if (reader.peek() == JsonReader.Token.BEGIN_OBJECT) {
            val packagingObj = packagingAdapter.fromJson(reader)
            packaging = packagingObj?.name ?: ""
            packagingId = packagingObj?.id ?: ""
            mask0 = mask0 and 0xffffff7f.toInt()
            mask0 = mask0 and 0xfffffeff.toInt()
          } else {
            packaging = stringAdapter.fromJson(reader) ?: throw Util.unexpectedNull(
              "packaging",
              "packaging", reader
            )
            // $mask = $mask and (1 shl 7).inv()
            mask0 = mask0 and 0xffffff7f.toInt()
          }
        }
        8 -> {
          packagingId = stringAdapter.fromJson(reader) ?: throw Util.unexpectedNull(
            "packagingId",
            "packaging-id", reader
          )
          // $mask = $mask and (1 shl 8).inv()
          mask0 = mask0 and 0xfffffeff.toInt()
        }
        9 -> {
          barcode = stringAdapter.fromJson(reader) ?: throw Util.unexpectedNull(
            "barcode",
            "barcode", reader
          )
          // $mask = $mask and (1 shl 9).inv()
          mask0 = mask0 and 0xfffffdff.toInt()
        }
        10 -> {
          coverArtArchive = coverArtArchiveAtFallbackOnNullAdapter.fromJson(reader)
            ?: throw Util.unexpectedNull("coverArtArchive", "cover-art-archive", reader)
          // $mask = $mask and (1 shl 10).inv()
          mask0 = mask0 and 0xfffffbff.toInt()
        }
        11 -> {
          disambiguation = stringAdapter.fromJson(reader) ?: throw Util.unexpectedNull(
            "disambiguation",
            "disambiguation",
            reader
          )
          // $mask = $mask and (1 shl 11).inv()
          mask0 = mask0 and 0xfffff7ff.toInt()
        }
        12 -> {
          quality = stringAdapter.fromJson(reader) ?: throw Util.unexpectedNull(
            "quality",
            "quality", reader
          )
          // $mask = $mask and (1 shl 12).inv()
          mask0 = mask0 and 0xffffefff.toInt()
        }
        13 -> {
          country = stringAdapter.fromJson(reader) ?: throw Util.unexpectedNull(
            "country",
            "country", reader
          )
          // $mask = $mask and (1 shl 13).inv()
          mask0 = mask0 and 0xffffdfff.toInt()
        }
        14 -> {
          asin = stringAdapter.fromJson(reader) ?: throw Util.unexpectedNull("asin", "asin", reader)
          // $mask = $mask and (1 shl 14).inv()
          mask0 = mask0 and 0xffffbfff.toInt()
        }
        15 -> {
          media = listOfMediumAdapter.fromJson(reader) ?: throw Util.unexpectedNull(
            "media",
            "media", reader
          )
          // $mask = $mask and (1 shl 15).inv()
          mask0 = mask0 and 0xffff7fff.toInt()
        }
        16 -> {
          labelInfo = listOfLabelInfoAdapter.fromJson(reader) ?: throw Util.unexpectedNull(
            "labelInfo",
            "label-info",
            reader
          )
          // $mask = $mask and (1 shl 16).inv()
          mask0 = mask0 and 0xfffeffff.toInt()
        }
        17 -> {
          trackCount = intAdapter.fromJson(reader) ?: throw Util.unexpectedNull(
            "trackCount",
            "track-count", reader
          )
          // $mask = $mask and (1 shl 17).inv()
          mask0 = mask0 and 0xfffdffff.toInt()
        }
        18 -> {
          statusId = stringAdapter.fromJson(reader) ?: throw Util.unexpectedNull(
            "statusId",
            "status-id", reader
          )
          // $mask = $mask and (1 shl 18).inv()
          mask0 = mask0 and 0xfffbffff.toInt()
        }
        19 -> {
          textRepresentation =
            textRepresentationAtFallbackOnNullAdapter.fromJson(reader) ?: throw Util.unexpectedNull(
              "textRepresentation",
              "text-representation",
              reader
            )
          // $mask = $mask and (1 shl 19).inv()
          mask0 = mask0 and 0xfff7ffff.toInt()
        }
        20 -> {
          aliases = listOfAliasAdapter.fromJson(reader) ?: throw Util.unexpectedNull(
            "aliases",
            "aliases", reader
          )
          // $mask = $mask and (1 shl 20).inv()
          mask0 = mask0 and 0xffefffff.toInt()
        }
        21 -> {
          annotation = stringAdapter.fromJson(reader) ?: throw Util.unexpectedNull(
            "annotation",
            "annotation", reader
          )
          // $mask = $mask and (1 shl 21).inv()
          mask0 = mask0 and 0xffdfffff.toInt()
        }
        22 -> {
          genres = listOfGenreAdapter.fromJson(reader) ?: throw Util.unexpectedNull(
            "genres",
            "genres", reader
          )
          // $mask = $mask and (1 shl 22).inv()
          mask0 = mask0 and 0xffbfffff.toInt()
        }
        23 -> {
          tags = listOfTagAdapter.fromJson(reader) ?: throw Util.unexpectedNull(
            "tags", "tags",
            reader
          )
          // $mask = $mask and (1 shl 23).inv()
          mask0 = mask0 and 0xff7fffff.toInt()
        }
        24 -> {
          primaryTypeId = stringAdapter.fromJson(reader) ?: throw Util.unexpectedNull(
            "primaryTypeId",
            "primary-type-id",
            reader
          )
          // $mask = $mask and (1 shl 24).inv()
          mask0 = mask0 and 0xfeffffff.toInt()
        }
        25 -> {
          secondaryTypes = listOfStringAdapter.fromJson(reader) ?: throw Util.unexpectedNull(
            "secondaryTypes",
            "secondary-types",
            reader
          )
          // $mask = $mask and (1 shl 25).inv()
          mask0 = mask0 and 0xfdffffff.toInt()
        }
        26 -> {
          secondaryTypeIds = listOfStringAdapter.fromJson(reader) ?: throw Util.unexpectedNull(
            "secondaryTypeIds",
            "secondary-type-ids",
            reader
          )
          // $mask = $mask and (1 shl 26).inv()
          mask0 = mask0 and 0xfbffffff.toInt()
        }
        27 -> {
          firstReleaseDate = stringAdapter.fromJson(reader) ?: throw Util.unexpectedNull(
            "firstReleaseDate",
            "first-release-date",
            reader
          )
          // $mask = $mask and (1 shl 27).inv()
          mask0 = mask0 and 0xf7ffffff.toInt()
        }
        28 -> {
          rating = ratingAtFallbackOnNullAdapter.fromJson(reader) ?: throw Util.unexpectedNull(
            "rating",
            "rating",
            reader
          )
          // $mask = $mask and (1 shl 28).inv()
          mask0 = mask0 and 0xefffffff.toInt()
        }
        29 -> {
          primaryType = stringAdapter.fromJson(reader) ?: throw Util.unexpectedNull(
            "primaryType",
            "primary-type", reader
          )
          // $mask = $mask and (1 shl 29).inv()
          mask0 = mask0 and 0xdfffffff.toInt()
        }
        30 -> {
          releases = listOfReleaseAdapter.fromJson(reader) ?: throw Util.unexpectedNull(
            "releases",
            "releases", reader
          )
          // $mask = $mask and (1 shl 30).inv()
          mask0 = mask0 and 0xbfffffff.toInt()
        }
        31 -> {
          score = intAdapter.fromJson(reader) ?: throw Util.unexpectedNull("score", "score", reader)
          // $mask = $mask and (1 shl 31).inv()
          mask0 = mask0 and 0x7fffffff
        }
        -1 -> {
          // Unknown name, skip it.
          reader.skipName()
          reader.skipValue()
        }
      }
    }
    reader.endObject()
    @Suppress("UNCHECKED_CAST")
    val localConstructor: Constructor<Release> =
      this.constructorRef ?: Release::class.java.getDeclaredConstructor(
        String::class.java, String::class.java,
        String::class.java, ReleaseGroup::class.java, List::class.java, List::class.java,
        String::class.java, String::class.java, String::class.java, String::class.java,
        CoverArtArchive::class.java, String::class.java, String::class.java, String::class.java,
        String::class.java, List::class.java, List::class.java, Int::class.javaPrimitiveType,
        String::class.java, TextRepresentation::class.java, List::class.java, String::class.java,
        List::class.java, List::class.java, String::class.java, List::class.java, List::class.java,
        String::class.java, Rating::class.java, String::class.java, List::class.java,
        Int::class.javaPrimitiveType, Int::class.javaPrimitiveType,
        Util.DEFAULT_CONSTRUCTOR_MARKER
      ).also { this.constructorRef = it }
    return localConstructor.newInstance(
      id,
      title,
      date,
      releaseGroup,
      releaseEvents,
      artistCredit,
      status,
      packaging,
      packagingId,
      barcode,
      coverArtArchive,
      disambiguation,
      quality,
      country,
      asin,
      media,
      labelInfo,
      trackCount,
      statusId,
      textRepresentation,
      aliases,
      annotation,
      genres,
      tags,
      primaryTypeId,
      secondaryTypes,
      secondaryTypeIds,
      firstReleaseDate,
      rating,
      primaryType,
      releases,
      score,
      mask0,
      null
    )
  }

  override fun toJson(writer: JsonWriter, value: Release?) {
    if (value == null) {
      throw NullPointerException("value was null! Wrap in .nullSafe() to write nullable values.")
    }
    writer.beginObject()
    writer.name("id")
    stringAdapter.toJson(writer, value.id)
    writer.name("title")
    stringAdapter.toJson(writer, value.title)
    writer.name("date")
    stringAdapter.toJson(writer, value.date)
    writer.name("release-group")
    releaseGroupAtFallbackOnNullAdapter.toJson(writer, value.releaseGroup)
    writer.name("release-events")
    listOfReleaseEventAdapter.toJson(writer, value.releaseEvents)
    writer.name("artist-credit")
    listOfArtistCreditAdapter.toJson(writer, value.artistCredit)
    writer.name("status")
    stringAdapter.toJson(writer, value.status)
    writer.name("packaging")
    stringAdapter.toJson(writer, value.packaging)
    writer.name("packaging-id")
    stringAdapter.toJson(writer, value.packagingId)
    writer.name("barcode")
    stringAdapter.toJson(writer, value.barcode)
    writer.name("cover-art-archive")
    coverArtArchiveAtFallbackOnNullAdapter.toJson(writer, value.coverArtArchive)
    writer.name("disambiguation")
    stringAdapter.toJson(writer, value.disambiguation)
    writer.name("quality")
    stringAdapter.toJson(writer, value.quality)
    writer.name("country")
    stringAdapter.toJson(writer, value.country)
    writer.name("asin")
    stringAdapter.toJson(writer, value.asin)
    writer.name("media")
    listOfMediumAdapter.toJson(writer, value.media)
    writer.name("label-info")
    listOfLabelInfoAdapter.toJson(writer, value.labelInfo)
    writer.name("track-count")
    intAdapter.toJson(writer, value.trackCount)
    writer.name("status-id")
    stringAdapter.toJson(writer, value.statusId)
    writer.name("text-representation")
    textRepresentationAtFallbackOnNullAdapter.toJson(writer, value.textRepresentation)
    writer.name("aliases")
    listOfAliasAdapter.toJson(writer, value.aliases)
    writer.name("annotation")
    stringAdapter.toJson(writer, value.annotation)
    writer.name("genres")
    listOfGenreAdapter.toJson(writer, value.genres)
    writer.name("tags")
    listOfTagAdapter.toJson(writer, value.tags)
    writer.name("primary-type-id")
    stringAdapter.toJson(writer, value.primaryTypeId)
    writer.name("secondary-types")
    listOfStringAdapter.toJson(writer, value.secondaryTypes)
    writer.name("secondary-type-ids")
    listOfStringAdapter.toJson(writer, value.secondaryTypeIds)
    writer.name("first-release-date")
    stringAdapter.toJson(writer, value.firstReleaseDate)
    writer.name("rating")
    ratingAtFallbackOnNullAdapter.toJson(writer, value.rating)
    writer.name("primary-type")
    stringAdapter.toJson(writer, value.primaryType)
    writer.name("releases")
    listOfReleaseAdapter.toJson(writer, value.releases)
    writer.name("score")
    intAdapter.toJson(writer, value.score)
    writer.endObject()
  }

  companion object {
    val ADAPTER_FACTORY = Factory { type, _, moshi ->
      if (Types.getRawType(type) == Release::class.java) {
        ReleaseAdapter(moshi)
      } else null
    }
  }
}
