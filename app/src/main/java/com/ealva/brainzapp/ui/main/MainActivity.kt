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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.ealva.brainzapp.ui.fragment.Navigation
import com.ealva.brainzsvc.service.ResourceFetcher
import org.koin.android.ext.android.inject
import kotlin.time.ExperimentalTime
import com.ealva.brainzapp.R.id.main_ui_fragment_container as ID_FRAGMENT_CONTAINER

class MainActivity : AppCompatActivity(), MainPresenter {
  private val resourceFetcher: ResourceFetcher by inject()
  private lateinit var ui: MainActivityUi
  private lateinit var navigation: Navigation

  @ExperimentalTime
  override fun onCreate(savedInstanceState: Bundle?) {
    navigation = Navigation.make(supportFragmentManager, ID_FRAGMENT_CONTAINER)
    supportFragmentManager.fragmentFactory = AppFragmentFactory(navigation, this, resourceFetcher)
    super.onCreate(savedInstanceState)
    ui = MainActivityUi(makeUiContext(), navigation)
  }

  override fun setActionBar(toolbar: Toolbar) {
    setSupportActionBar(toolbar)
  }
}
