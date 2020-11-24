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
@file:Suppress("unused")

package com.ealva.brainzapp.ui.view

import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Spinner
import android.widget.SpinnerAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import com.ealva.brainzapp.R.id.spinner_position_tag_id as KEY_SPINNER_POS

class SpinnerWrapper(val spinner: Spinner) {
  private var proxyListener: OnItemSelectedListener? = null

  init {
    spinner.setTag(KEY_SPINNER_POS, spinner.selectedItemPosition)
  }

  var adapter: SpinnerAdapter
    get() = spinner.adapter
    set(adapter) {
      spinner.adapter = adapter
    }

  val count: Int
    get() = spinner.count

  val selectedItem: Any?
    get() = spinner.selectedItem

  val selectedItemId: Long
    get() = spinner.selectedItemId

  val selectedItemPosition: Int
    get() = spinner.selectedItemPosition

  var isEnabled: Boolean
    get() = spinner.isEnabled
    set(enabled) {
      spinner.isEnabled = enabled
    }

  @JvmOverloads
  fun setSelection(position: Int, shouldFire: Boolean = false) {
    spinner.setTag(KEY_SPINNER_POS, if (shouldFire) -1 else position)
    spinner.setSelection(position)
  }

  private val itemSelectedListener = object : OnItemSelectedListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {
      proxyListener?.onNothingSelected(parent)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
      if (position != spinner.getTag(KEY_SPINNER_POS)) {
        spinner.setTag(KEY_SPINNER_POS, position)
        proxyListener?.onItemSelected(parent, view, position, id)
      }
    }
  }

  fun setOnItemSelectedListener(listener: OnItemSelectedListener?) {
    proxyListener = listener
    spinner.onItemSelectedListener = if (listener == null) null else itemSelectedListener
  }

  fun getItemAtPosition(position: Int): Any {
    return spinner.getItemAtPosition(position)
  }

  fun getItemIdAtPosition(position: Int): Long {
    return spinner.getItemIdAtPosition(position)
  }

  fun setVisibility(visibility: Int) {
    spinner.visibility = visibility
  }

  val selection: SpinnerSelection
    get() {
      return SpinnerSelection(
        spinner,
        spinner.selectedView ?: spinner,
        spinner.selectedItemPosition,
        spinner.selectedItemId
      )
    }

  fun hideSpinnerDropDown() {
    try {
      val method = Spinner::class.java.getDeclaredMethod("onDetachedFromWindow")
      method.isAccessible = true
      method.invoke(spinner)
    } catch (e: Exception) {
      Timber.e(e, "Exception hiding spinner drop down")
    }
  }
}

data class SpinnerSelection(
  val parent: AdapterView<*>?,
  val view: View?,
  val position: Int,
  val id: Long
)

@OptIn(ExperimentalCoroutinesApi::class)
fun SpinnerWrapper.itemSelectedFlow(): Flow<SpinnerSelection> = callbackFlow<SpinnerSelection> {
  setOnItemSelectedListener(object : OnItemSelectedListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
      offer(SpinnerSelection(parent, view, position, id))
    }
  })
  awaitClose { setOnItemSelectedListener(null) }
}.conflate().flowOn(Dispatchers.Main)
