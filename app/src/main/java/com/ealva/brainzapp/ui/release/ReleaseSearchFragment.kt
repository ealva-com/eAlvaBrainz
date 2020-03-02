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

package com.ealva.brainzapp.ui.release

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

class ReleaseSearchFragment private constructor(
  private val brainz: MusicBrainzService,
  private val navigation: Navigation
) : Fragment() {
  private lateinit var uiContext: FragmentUiContext
  private lateinit var ui: ReleaseSearchUi

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    uiContext = makeUiContext()
    ui = ReleaseSearchUi.make(uiContext, brainz, navigation)
    return ui.root
  }

  companion object {
    val NAME: String = ReleaseSearchFragment::class.java.name

    fun make(
      brainz: MusicBrainzService,
      navigation: Navigation
    ): ReleaseSearchFragment {
      return ReleaseSearchFragment(
        brainz,
        navigation
      )
    }

    fun make(fm: FragmentManager): ReleaseSearchFragment {
      return fm.instantiate()
    }

  }
}
