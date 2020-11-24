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
import com.ealva.brainzapp.ui.fragment.FragmentUiContext

class ReleaseItemAdapter(
  private val uiContext: FragmentUiContext,
  private val selection: (ReleaseItem) -> Unit
) : RecyclerView.Adapter<ReleaseItemAdapter.ViewHolder>() {

  private val itemList: MutableList<ReleaseItem> = mutableListOf()
  private var recycler: RecyclerView? = null

  init {
    setHasStableIds(true)
  }

  override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
    recycler = recyclerView
  }

  override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
    recycler = null
  }

  fun setItems(newList: List<ReleaseItem>) {
    itemList.clear()
    itemList.addAll(newList)
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    ViewHolder(
      ReleaseItemUi(uiContext) { v ->
        val position: Int = recycler?.getChildAdapterPosition(v) ?: -1
        if (position in itemList.indices) selection(itemList[position])
      }
    )

  class ViewHolder(val ui: ReleaseItemUi) : RecyclerView.ViewHolder(ui.root) {
    fun bind(item: ReleaseItem) = ui.bind(item)
  }

  override fun getItemCount(): Int {
    return itemList.size
  }

  private fun getItemAt(position: Int): ReleaseItem {
    return itemList.elementAtOrElse(position) { ReleaseItem.NullDisplayRelease }
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(getItemAt(position))
  }

  override fun getItemId(position: Int): Long {
    return getItemAt(position).id
  }
}
