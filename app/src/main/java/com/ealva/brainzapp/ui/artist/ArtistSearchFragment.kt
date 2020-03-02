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

package com.ealva.brainzapp.ui.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ealva.brainzapp.ui.fragment.FragmentUiContext
import com.ealva.brainzapp.ui.fragment.Navigation
import com.ealva.brainzapp.ui.fragment.makeUiContext
import com.ealva.brainzapp.ui.main.instantiate
import com.ealva.brainzsvc.service.MusicBrainzService

class ArtistSearchFragment private constructor(
  private val brainz: MusicBrainzService,
  private val navigation: Navigation
) : Fragment() {
  private lateinit var viewModel: ArtistSearchViewModel
  private lateinit var uiContext: FragmentUiContext
  private lateinit var ui: ArtistSearchUi

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    viewModel = getArtistSearchViewModel(brainz)
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
