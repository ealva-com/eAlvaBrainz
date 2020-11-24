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

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.ealva.brainzapp.R

typealias FragmentTagPair = Pair<Fragment, String?>

interface Navigation {
  @get:IdRes
  val containerViewId: Int
  val fragmentPresent: Boolean

  fun addIfNoFragmentPresent(factory: () -> FragmentTagPair)
  fun replace(fragment: Fragment, tag: String?)
  fun push(fragment: Fragment, tag: String?)
  fun pop()
  val currentTag: String?

  companion object {
    fun make(fragmentManager: FragmentManager, @IdRes containerViewId: Int): Navigation {
      return NavigationImpl(fragmentManager, containerViewId)
    }
  }
}

inline val Navigation.noFragmentPresent
  get() = !fragmentPresent

private class NavigationImpl(
  private val fm: FragmentManager,
  @field:IdRes override val containerViewId: Int
) : Navigation {
  override val fragmentPresent: Boolean
    get() = fm.findFragmentById(R.id.main_ui_fragment_container) != null

  override fun addIfNoFragmentPresent(factory: () -> FragmentTagPair) {
    if (noFragmentPresent) {
      val (fragment, tag) = factory()
      fm.commit { add(containerViewId, fragment, tag) }
    }
  }

  override fun replace(fragment: Fragment, tag: String?) =
    fm.commit { replace(containerViewId, fragment, tag) }

  override fun push(fragment: Fragment, tag: String?) {
    fm.commit {
      replace(containerViewId, fragment, tag)
      addToBackStack(null)
    }
  }

  override fun pop() = fm.popBackStack()

  override val currentTag: String?
    get() {
      return fm.findFragmentById(containerViewId)?.tag
    }
}
