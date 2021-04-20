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

package com.ealva.brainzsvc.init

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.StringRes
import androidx.core.content.getSystemService
import java.io.File

@SuppressLint("StaticFieldLeak") // store the app context, not a leak
public object EalvaBrainz {

  internal fun fetch(@StringRes stringRes: Int, vararg formatArgs: Any): String {
    return appCtx.getString(stringRes, *formatArgs)
  }

  internal fun getCacheDir(dirName: String): File = File(appCtx.cacheDir, dirName)

  internal lateinit var appCtx: Context

  /**
   * Must be initialized with an application context, which should be done automatically via
   * [EalvaBrainzInitializer], which is an instance of [androidx.startup.Initializer] loaded
   * via a provider in AndroidManifest.xml. If the app removes this provider it must call this
   * method first in app startup
   */
  public fun init(context: Context): EalvaBrainz = apply {
    appCtx = context.applicationContext
  }
}

internal inline fun <reified T : Any> Context.requireSystemService(): T {
  return requireNotNull(getSystemService()) {
    "Failed to get system service ${T::class.java.simpleName}"
  }
}

@Suppress("UNCHECKED_CAST")
internal inline fun <reified T : Any> requireSystemService(): T =
  EalvaBrainz.appCtx.requireSystemService()
