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

package com.ealva.brainz.ui.view

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentActivity

fun Activity.hideKeyboard() {
  val inputMethodManager =
    getSystemService(FragmentActivity.INPUT_METHOD_SERVICE) as InputMethodManager
  //Find the currently focused view, so we can grab the correct window token from it.
  var view = currentFocus
  //If no view currently has focus, create a new one, just so we can grab a window token from it
  if (view == null) {
    view = View(this)
  }
  inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}
