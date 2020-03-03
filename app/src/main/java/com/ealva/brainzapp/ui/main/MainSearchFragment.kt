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

package com.ealva.brainzapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class MainSearchFragment private constructor(val mainPresenter: MainPresenter) : Fragment() {
  private lateinit var ui: MainSearchFragmentUi

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    ui = makeSearchFragmentUi(mainPresenter)
    return ui.root
  }

  companion object {
    val NAME: String = MainSearchFragment::class.java.name

    fun make(mainPresenter: MainPresenter): MainSearchFragment {
      return MainSearchFragment(mainPresenter)
    }

    fun make(fm: FragmentManager): MainSearchFragment {
      return fm.instantiate()
    }

  }
}
