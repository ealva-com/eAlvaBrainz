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

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ealva.brainzapp.ui.fragment.FragmentUiContext
import splitties.toast.toast
import splitties.views.dsl.core.Ui
import splitties.views.dsl.recyclerview.recyclerView
import com.ealva.brainzapp.R.id.artist_releases_recycler as ID_RECYCLER

class ArtistReleasesUi(
  private val uiContext: FragmentUiContext,
  private val viewModel: ArtistViewModel
) : Ui {
  private val lifecycleOwner = uiContext.lifecycleOwner
  override val ctx: Context = uiContext.context

  private val itemAdapter: ReleaseItemAdapter

  override val root: RecyclerView = recyclerView(ID_RECYCLER) {
    layoutManager = LinearLayoutManager(context)
    adapter = ReleaseItemAdapter(uiContext) { displayRelease ->
      ctx.toast("Selected: ${displayRelease.title}")
    }.also { itemAdapter = it }
  }.also {
    lifecycleOwner.lifecycle.addObserver(
      object : DefaultLifecycleObserver {
        override fun onResume(owner: LifecycleOwner) {
          viewModel.releases.observe(
            lifecycleOwner,
            { list ->
              itemAdapter.setItems(list ?: emptyList())
            }
          )
        }

        override fun onPause(owner: LifecycleOwner) {
          viewModel.releases.removeObservers(lifecycleOwner)
        }
      }
    )
  }
}
