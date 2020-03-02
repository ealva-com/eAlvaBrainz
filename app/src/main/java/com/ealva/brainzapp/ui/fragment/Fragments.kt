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

package com.ealva.brainzapp.ui.fragment

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope

interface FragmentUiContext {
  val fragment: Fragment
  val context: Context
  val lifecycleOwner: LifecycleOwner
  val scope: CoroutineScope
  val fragmentManager: FragmentManager
}

fun Fragment.makeUiContext(): FragmentUiContext {
  return FragmentUiContextImpl(this)
}

private class FragmentUiContextImpl(override val fragment: Fragment) : FragmentUiContext {
  override val context = fragment.requireContext()
  override val lifecycleOwner = fragment.viewLifecycleOwner
  override val scope = lifecycleOwner.lifecycleScope
  override val fragmentManager = fragment.childFragmentManager
}
