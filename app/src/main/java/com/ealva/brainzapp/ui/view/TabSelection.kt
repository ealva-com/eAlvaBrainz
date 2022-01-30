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

package com.ealva.brainzapp.ui.view

import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn

sealed class TabSelection(@Suppress("unused") val tab: TabLayout.Tab) {
  class Selected(tab: TabLayout.Tab) : TabSelection(tab)
  class Reselected(tab: TabLayout.Tab) : TabSelection(tab)
  class Unselected(tab: TabLayout.Tab) : TabSelection(tab)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun TabLayout.tabSelectionFlow(): Flow<TabSelection> = callbackFlow<TabSelection> {
  val listener = object : TabLayout.OnTabSelectedListener {
    override fun onTabReselected(tab: TabLayout.Tab) {
      trySend(TabSelection.Reselected(tab))
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
      trySend(TabSelection.Unselected(tab))
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
      trySend(TabSelection.Selected(tab))
    }
  }
  addOnTabSelectedListener(listener)
  awaitClose { removeOnTabSelectedListener(listener) }
}.flowOn(Dispatchers.Main)
