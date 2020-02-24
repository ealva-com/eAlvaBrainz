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

package com.ealva.brainz.ui.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ealva.brainz.ui.fragment.FragmentUiContext
import com.ealva.brainz.ui.fragment.Navigation
import com.ealva.brainz.ui.fragment.makeUiContext
import com.ealva.brainz.ui.main.instantiate
import com.ealva.ealvabrainz.service.MusicBrainzService
import splitties.toast.toast

class ArtistSearchFragment private constructor(
  private val brainz: MusicBrainzService,
  private val navigation: Navigation
) : Fragment() {
  private lateinit var viewModel: ArtistSearchViewModel
  private lateinit var uiContext: FragmentUiContext
  private lateinit var ui: ArtistSearchUi

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    toast("savedInstanceState is null: ${savedInstanceState == null}")
    viewModel = getArtistSearchViewModel(brainz)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    uiContext = makeUiContext()
    ui = ArtistSearchUi.make(uiContext, viewModel, navigation)
    return ui.root
  }

  companion object {
    val NAME: String = ArtistSearchFragment::class.java.name

    fun make(
      brainz: MusicBrainzService,
      navigation: Navigation
    ): ArtistSearchFragment {
      return ArtistSearchFragment(brainz, navigation)
    }

    fun make(fm: FragmentManager): ArtistSearchFragment {
      return fm.instantiate()
    }

  }
}
