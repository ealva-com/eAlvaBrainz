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

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import androidx.viewpager2.widget.ViewPager2
import splitties.experimental.InternalSplittiesApi
import splitties.views.dsl.core.NO_THEME
import splitties.views.dsl.core.Ui
import splitties.views.dsl.core.view

@OptIn(InternalSplittiesApi::class)
inline fun Context.viewPager2(
  @IdRes id: Int,
  @StyleRes theme: Int = NO_THEME,
  initView: ViewPager2.() -> Unit = {}
): ViewPager2 {
  return view(id, theme, initView)
}

@SuppressLint("ResourceType")
inline fun View.viewPager2(
  @IdRes id: Int,
  @StyleRes theme: Int = NO_THEME,
  initView: ViewPager2.() -> Unit = {}
): ViewPager2 {
  return context.viewPager2(id, theme, initView)
}

@SuppressLint("ResourceType")
inline fun Ui.viewPager2(
  @IdRes id: Int,
  @StyleRes theme: Int = NO_THEME,
  initView: ViewPager2.() -> Unit = {}
): ViewPager2 {
  return ctx.viewPager2(id, theme, initView)
}
