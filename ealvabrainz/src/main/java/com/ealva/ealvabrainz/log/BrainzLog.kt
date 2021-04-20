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

package com.ealva.ealvabrainz.log

import com.ealva.ealvabrainz.BuildConfig
import com.ealva.ealvalog.LogEntry
import com.ealva.ealvalog.Logger
import com.ealva.ealvalog.Marker
import com.ealva.ealvalog.Markers
import com.ealva.ealvalog.e
import com.ealva.ealvalog.filter.MarkerFilter
import com.ealva.ealvalog.i
import com.ealva.ealvalog.lazyLogger
import com.ealva.ealvalog.w
import kotlin.reflect.KClass

public object BrainzLog {
  public var logBrainzErrors: Boolean = true

  @Suppress("MemberVisibilityCanBePrivate")
  public const val markerName: String = "eAlvaBrainz"

  /**
   * Loggers in eAlvaBrainz use this [Marker] so all logging can be filtered.
   */
  public val marker: Marker by lazy { Markers[markerName] }

  /**
   * Clients can use this [MarkerFilter] in a logger handler to direct associated logging where
   * desired (file, Android log, ...). See [eAlvaLog](https://github.com/ealva-com/ealvalog) for
   * information on configuring logging.
   */
  @Suppress("unused")
  public val markerFilter: MarkerFilter by lazy { MarkerFilter(marker) }
}

@Suppress("NOTHING_TO_INLINE")
internal inline fun <T : Any> brainzLogger(forClass: KClass<T>): Lazy<Logger> =
  lazyLogger(forClass, BrainzLog.marker)

@Suppress("FunctionName", "unused")
internal inline fun Logger._i(
  throwable: Throwable? = null,
  marker: Marker? = null,
  block: (LogEntry) -> Unit
) {
  if (BuildConfig.DEBUG) i(throwable, marker, null, block)
}

@Suppress("FunctionName", "unused")
internal inline fun Logger._w(
  throwable: Throwable? = null,
  marker: Marker? = null,
  block: (LogEntry) -> Unit
) {
  if (BuildConfig.DEBUG) w(throwable, marker, null, block)
}

@Suppress("FunctionName", "unused")
internal inline fun Logger._e(
  throwable: Throwable? = null,
  marker: Marker? = null,
  block: (LogEntry) -> Unit
) {
  if (BuildConfig.DEBUG) e(throwable, marker, null, block)
}
