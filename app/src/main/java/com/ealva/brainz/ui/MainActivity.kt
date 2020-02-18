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

package com.ealva.brainz.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.lifecycleScope
import com.ealva.ealvabrainz.R
import com.ealva.ealvabrainz.common.toAlbumName
import com.ealva.ealvabrainz.common.toArtistName
import com.ealva.ealvabrainz.common.toRecordingName
import com.ealva.ealvabrainz.service.MusicBrainzResult
import com.ealva.ealvabrainz.service.MusicBrainzService
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import splitties.dimensions.dip
import splitties.snackbar.longSnack
import splitties.views.dsl.coordinatorlayout.coordinatorLayout
import splitties.views.dsl.coordinatorlayout.defaultLParams
import splitties.views.dsl.core.add
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.wrapInScrollView
import splitties.views.verticalPadding
import com.ealva.ealvabrainz.R.id.main_text_center as ID_MAIN_TEXT

class MainActivity : AppCompatActivity(), KodeinAware {
  override val kodein: Kodein by closestKodein()
  private val brainz: MusicBrainzService by instance()
  private lateinit var ui: CoordinatorLayout
  private lateinit var mainText: TextView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    mainText = textView(ID_MAIN_TEXT)
    val scrollView = mainText.wrapInScrollView(R.id.main_scroll_view) {
      isFillViewport = true
      verticalPadding = dip(4)
      clipToPadding = false
    }
    ui = coordinatorLayout {
      fitsSystemWindows = true
      add(scrollView, defaultLParams(matchParent, matchParent))
    }

    setContentView(ui)
  }

  override fun onStart() {
    super.onStart()
    lifecycleScope.launch {
      val recordingName = "Her Majesty".toRecordingName()
      val artistName = "The Beatles".toArtistName()
      val albumName = "Abbey Road".toAlbumName()
      handleResult(brainz.findRecording(recordingName, artistName, albumName))?.let { recordingList ->
        mainText.text = recordingList.toString()
      }
    }
  }

  private fun <T : Any> handleResult(result: MusicBrainzResult<T>): T? {
    return when (result) {
      is MusicBrainzResult.Success -> result.value
      is MusicBrainzResult.Error -> {
        ui.longSnack(result.error.error)
        null
      }
      is MusicBrainzResult.Unknown -> {
        ui.longSnack("Unknown error. Code:${result.response.httpStatusCode}")
        null
      }
      is MusicBrainzResult.Exceptional -> {
        ui.longSnack(result.exception.message ?: "${result.exception}")
        null
      }
    }
  }
}
