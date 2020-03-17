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

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ealva.brainzapp.services.lifoSingleThreadExecutor
import com.ealva.brainzapp.ui.fragment.FragmentUiContext
import com.ealva.brainzsvc.service.MusicBrainzService

class ReleaseGroupItemAdapter(
  private val uiContext: FragmentUiContext,
  private val brainz: MusicBrainzService,
  private val selection: (ReleaseGroupItem) -> Unit
) : RecyclerView.Adapter<ReleaseGroupItemAdapter.ViewHolder>() {

  private val itemList: MutableList<ReleaseGroupItem> = mutableListOf()
  private var recycler: RecyclerView? = null
  private val executor = uiContext.lifecycleOwner.lifoSingleThreadExecutor()

  init {
    setHasStableIds(true)
  }

  override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
    recycler = recyclerView
    if (itemList.isNotEmpty()) notifyDataSetChanged()
  }

  override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
    recycler = null
  }

  fun setItems(newList: List<ReleaseGroupItem>) {
    itemList.clear()
    itemList.addAll(newList)
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    ViewHolder(
      ReleaseGroupItemUi(uiContext, executor, brainz, ::itemUpdated) { v ->
        val position: Int = recycler?.getChildAdapterPosition(v) ?: -1
        if (position in itemList.indices) selection(itemList[position])
      }
    )

  class ViewHolder(val ui: ReleaseGroupItemUi) : RecyclerView.ViewHolder(ui.root) {
    fun bind(item: ReleaseGroupItem) = ui.bind(item)
  }

  private fun itemUpdated(releaseGroupItem: ReleaseGroupItem) {
    val index = itemList.indexOfFirst { it.id == releaseGroupItem.id }
    if (index >= 0) notifyItemChanged(index)
  }

  override fun getItemCount(): Int {
    return itemList.size
  }

  private fun getItemAt(position: Int): ReleaseGroupItem {
    return itemList.elementAtOrElse(position) { ReleaseGroupItem.NullDisplayReleaseGroup }
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(getItemAt(position))
  }

  override fun getItemId(position: Int): Long {
    return getItemAt(position).id
  }
}
