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

package com.ealva.ealvabrainz.common

@JvmInline
/**
 * The name is the official name of the event if it has one, or a descriptive name (like "Main
 * Artist at Place") if not. May be a partial name if used in a query.
 */
public value class EventName(public val value: String) {
  public companion object {
    public val UNKNOWN: EventName = EventName("Unknown")
  }
}

/**
 * Convert this String to an [EventName] or [EventName.UNKNOWN] if this is null.
 */
@Suppress("unused")
public fun String?.toEventName(): EventName {
  return if (this != null) EventName(trim()) else EventName.UNKNOWN
}
