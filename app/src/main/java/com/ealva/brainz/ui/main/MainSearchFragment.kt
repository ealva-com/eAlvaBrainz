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

package com.ealva.brainz.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ealva.brainz.ui.fragment.Navigation
import com.ealva.ealvabrainz.service.MusicBrainzService

class MainSearchFragment private constructor(
  private val brainz: MusicBrainzService,
  private val navigation: Navigation
) : Fragment() {
  private lateinit var ui: MainSearchFragmentUi

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    ui = makeSearchFragmentUi()
    return ui.root
  }

//  override fun onStart() {
//    super.onStart()
//    viewLifecycleOwner.lifecycleScope.launch {
//      val recordingName = "Her Majesty".toRecordingName()
//      val artistName = "The Beatles".toArtistName()
//      val albumName = "Abbey Road".toAlbumName()
//      handleResult(brainz.findRecording(recordingName, artistName, albumName))?.let { recordingList ->
//        textView.text = recordingList.toString()
//      }
//    }
//  }
//
//  private fun <T : Any> handleResult(result: MusicBrainzResult<T>): T? {
//    return when (result) {
//      is MusicBrainzResult.Success -> result.value
//      is MusicBrainzResult.Error -> {
//        ui.longSnack(result.error.error)
//        null
//      }
//      is MusicBrainzResult.Exceptional -> {
//        ui.longSnack(result.exception.message ?: "${result.exception}")
//        null
//      }
//    }
//  }

  companion object {
    val NAME: String = MainSearchFragment::class.java.name

    fun make(brainz: MusicBrainzService, navigation: Navigation): MainSearchFragment {
      return MainSearchFragment(brainz, navigation)
    }

    fun make(fm: FragmentManager): MainSearchFragment {
      return fm.instantiate()
    }

  }
}
