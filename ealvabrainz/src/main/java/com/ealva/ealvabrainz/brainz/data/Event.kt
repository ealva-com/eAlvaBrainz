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

import com.ealva.ealvabrainz.moshi.FallbackOnNull
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * An event refers to an organised event which people can attend, and is relevant to MusicBrainz.
 * Generally this means live performances, like concerts and festivals.
 */
@JsonClass(generateAdapter = true)
data class Event(
  /** Event MBID */
  var id: String = "",
  /**
   * The name is the official name of the event if it has one, or a descriptive name
   * (like "Main Artist at Place") if not.
   */
  var name: String = "",
  /** The time the event occurred/will occur */
  var time: String = "",
  /**
   * The type describes what kind of event the event is. The possible values are:
   * * **Concert**
   * An individual concert by a single artist or collaboration, often with supporting artists who
   * perform before the main act.
   * * **Festival**
   * An event where a number of different acts perform across the course of the day. Larger
   * festivals may be spread across multiple days.
   * * **Launch event**
   * A party, reception or other event held specifically for the launch of a release.
   * * **Convention/Expo**
   * A convention, expo or trade fair is an event which is not typically orientated around music
   * performances, but can include them as side activities.
   * * **Masterclass/Clinic**
   * A masterclass or clinic is an event where an artist meets with a small to medium-sized
   * audience and instructs them individually and/or takes questions intended to improve the
   * audience members' playing skills.
   */
  var type: String = "",
  /** The cancelled field describes whether or not the event took place. */
  var cancelled: Boolean = false,
  /**
   * The setlist stores a list of songs performed, optionally including links to artists and works.
   * See the [setlist](https://musicbrainz.org/doc/Event/Setlist) documentation for syntax and
   * examples.
   *
   * ### Syntax
   * Each (non-empty) line in the setlist must start with a symbol followed by a space.
   * Those are '@' for artists, '*' for works/songs, and '#' for additional info (such as "Encore").
   *
   * #### Popular Music example:
   * ```
   * @ [e1f1e33e-2e4c-4d43-b91b-7064068d3283|KISS]
   * * [5b5a12c2-46e1-479c-9308-e78a7f908006|Psycho Circus]
   * * [92651d41-fea1-34fb-a1d1-c772ea1b00a4|Shout It Out Loud]
   * * [96d1408f-4780-3ff4-b234-c6c0d633f89e|Let Me Go, Rock ’n Roll]
   * * [b05809ff-d6a9-3b17-9d8d-c15697d77462|I Love It Loud]
   * * [905c8f48-ce28-4369-85d8-75202285757f|Hell or Hallelujah]
   * * [bbd90dfe-265a-3e72-9ce0-99cc4fa0cdf4|War Machine] (Gene breathes fire)
   * * [5dbb6fa5-9332-3890-9021-4241f4f3f4d5|Heaven’s on Fire]
   * * [a35d9cc6-4732-3181-920b-0f1a0d876d56|Calling Dr. Love]
   * * [c9873562-2181-4693-a8e5-358b62d225db|Say Yeah]
   * * [cfc0ad98-7b8c-31ff-a804-d47c8e8b1f12|Shock Me] / [9f9e6ff8-38f4-4f24-9009-24383dadc070|Outta This World]
   * # guitar and drum solos
   * # bass solo (Gene spits blood and flies)
   * * [75a72144-fbcf-3c3f-aabd-e85dec885cad|God of Thunder]
   * * [e93851ad-502b-3549-b187-2ed8bac9c19f|Lick It Up] / [f00616af-fff7-3fe8-ab97-5dda1d34eded|Won’t Get Fooled Again]
   * * [d14dc99a-a45e-394d-8fb7-06dc91670b58|Love Gun] (Paul flies over the crowd)
   * * [52cb581c-5972-3d49-9cdb-bb2b472f99b9|Black Diamond]
   * * [512c4801-5184-3061-934d-e3542921aa66|Detroit Rock City]
   * * [0c309dd6-867a-3998-babe-57ffca970d54|I Was Made for Lovin’ You]
   * * [de4c0fa3-a578-38e6-99fd-3448f7cbd640|Rock and Roll All Nite]
   * ```
   */
  var setList: String = "",
  /** begin date */
  /**
   * Aliases are alternate names for an event, which currently have two main functions: localised
   * names and search hints. Localised names are used to store the official names used in different
   * languages and countries. These use the locale field to identify which language or country the
   * name is for. Search hints are used to help both users and the server when searching and can be
   * a number of things including alternate names, nicknames or even misspellings.
   */
  var aliases: List<Alias> = emptyList(),
  /**
   * Lifespan is begin/end dates and if the event has ended
   */
  @Json(name = "life-span") @field:FallbackOnNull var lifeSpan: LifeSpan = LifeSpan.NullLifeSpan,

  var relations: List<Relation> = emptyList(),
  /**
   * See the [page about disambiguation comments](https://musicbrainz.org/doc/Disambiguation_Comment)
   * for more information
   */
  var disambiguation: String = "",
  /**
   * See the [page about annotations](https://musicbrainz.org/doc/Annotation) for more information.
   */
  var annotation: String = "",
  /** Used when querying a list of events */
  var score: Int = 0
) {

  interface Lookup : Include
  // No Misc or Subquery includes for an Event

  /**
   * Event relationships
   *
   * * [Event-Event](https://musicbrainz.org/relationships/event-event)
   * * [Event-Place](https://musicbrainz.org/relationships/event-place)
   * * [Event-Recording](https://musicbrainz.org/relationships/event-recording)
   * * [Event-Release](https://musicbrainz.org/relationships/event-release)
   * * [Event-ReleaseGroup](https://musicbrainz.org/relationships/event-release_group)
   * * [Event-Series](https://musicbrainz.org/relationships/event-series)
   * * [Event-URL](https://musicbrainz.org/relationships/event-url)
   * * [Event-Work](https://musicbrainz.org/relationships/event-work)
   */
  @Suppress("unused")
  enum class Relations(override val value: String) : Lookup {
    Event("event-rels"),
    Place("place-rels"),
    Recording("recording-rels"),
    Release("release-rels"),
    ReleaseGroup("release-group-rels"),
    Series("series-rels"),
    Url("url-rels"),
    Work("work-rels")
  }

  enum class SearchField(val value: String) {
   /** an alias attached to the event */
   Alias("alias"),
   /** the MBID of an area related to the event */
   AreaId("aid"),
   /** the name of an area related to the event */
   Area("area"),
   /** the MBID of an artist related to the event */
   ArtistId("arid"),
   /** the name of an artist related to the event */
   Artist("artist"),
   /** the disambiguation comment for the event */
   Comment("comment"),
   /** the MBID of the event */
   EventId("eid"),
   /** the name of the event */
   Event("event"),
   /** the MBID of a place related to the event */
   PlaceId("pid"),
   /** the name of a place related to the event */
   Place("place"),
   /** the event's type */
   Type("type"),
   /** a tag attached to the event */
   Tag("tag"),
  }

  companion object {
    val NullEvent = Event()
    val fallbackMapping: Pair<String, Any> = Event::class.java.name to NullEvent
  }
}

inline val Event.isNullObject
  get() = this === Event.NullEvent

