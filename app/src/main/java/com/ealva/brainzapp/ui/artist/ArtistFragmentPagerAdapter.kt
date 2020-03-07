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

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ealva.brainzapp.ui.fragment.FragmentUiContext

class ArtistFragmentPagerAdapter(
  private val uiContext: FragmentUiContext
) : FragmentStateAdapter(uiContext.fragmentManager, uiContext.lifecycleOwner.lifecycle) {

  private val factoryList = listOf(
    { ArtistReleaseGroupsFragment.make(uiContext.fragmentManager) },
    { ArtistReleasesFragment.make(uiContext.fragmentManager) }
  )

  override fun getItemCount(): Int {
    return factoryList.size
  }

  override fun createFragment(position: Int): Fragment {
    if (position in factoryList.indices) return factoryList[position]()
    throw IllegalStateException("No fragment for position=$position")
  }
}
