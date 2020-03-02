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

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.ealva.brainzapp.ui.fragment.Navigation
import kotlinx.coroutines.CoroutineScope
import splitties.views.dsl.core.Ui
import splitties.views.dsl.core.frameLayout

interface ActivityUiContext {
  val activity: AppCompatActivity
  val lifecycleOwner: LifecycleOwner
  val scope: CoroutineScope
}

fun AppCompatActivity.makeUiContext(): ActivityUiContext {
  val theActivity = this
  return object : ActivityUiContext {
    override val activity: AppCompatActivity
      get() = theActivity
    override val lifecycleOwner: LifecycleOwner
      get() = theActivity
    override val scope: CoroutineScope
      get() = lifecycleOwner.lifecycleScope
  }
}

class MainActivityUi(
  private val uiCtx: ActivityUiContext,
  private val navigation: Navigation,
  override val ctx: Context = uiCtx.activity
) : Ui {
  override val root: View = frameLayout(navigation.containerViewId) {

  }.apply {
    uiCtx.activity.setContentView(this)
    maybeSetPrimaryFragment()
  }

  private fun maybeSetPrimaryFragment() {
    navigation.addIfNoFragmentPresent {
      MainSearchFragment.make(uiCtx.activity.supportFragmentManager) to MainSearchFragment.NAME
    }
  }

}
