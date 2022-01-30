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

package com.ealva.brainzapp.app

import android.app.Application
import com.ealva.brainzapp.BuildConfig
import com.ealva.brainzapp.services.brainzModule
import com.ealva.ealvabrainz.log.BrainzLog
import com.ealva.ealvalog.Loggers
import com.ealva.ealvalog.android.AndroidLogger
import com.ealva.ealvalog.android.AndroidLoggerFactory
import com.ealva.ealvalog.android.DebugLogHandler
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

@Suppress("unused") // It's in the manifest
class App : Application() {
  override fun onCreate() {
    super.onCreate()
    setupLogging()
    startKoin {
      androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
      androidContext(this@App)
      modules(brainzModule)
    }
  }

  private fun setupLogging() {
    AndroidLogger.setHandler(DebugLogHandler())
    Loggers.setFactory(AndroidLoggerFactory)
    BrainzLog.logBrainzErrors = true
  }
}
