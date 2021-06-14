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

@file:Suppress("unused", "Indentation", "MagicNumber")

package com.ealva.brainzapp.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import fr.castorflex.android.circularprogressbar.CircularProgressBar
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable
import splitties.dimensions.dip
import splitties.dimensions.dp
import splitties.experimental.InternalSplittiesApi
import splitties.views.dsl.coordinatorlayout.defaultLParams
import splitties.views.dsl.core.NO_THEME
import splitties.views.dsl.core.Ui
import splitties.views.dsl.core.add
import splitties.views.dsl.core.view
import splitties.views.gravityCenter

@OptIn(InternalSplittiesApi::class)
inline fun Context.circularProgress(
  @IdRes id: Int,
  @StyleRes theme: Int = NO_THEME,
  initView: CircularProgressBar.() -> Unit = {}
): CircularProgressBar {
  return view(id, theme, initView)
}

@SuppressLint("ResourceType")
inline fun View.circularProgress(
  @IdRes id: Int,
  @StyleRes theme: Int = NO_THEME,
  initView: CircularProgressBar.() -> Unit = {}
): CircularProgressBar {
  return context.circularProgress(id, theme, initView)
}

@SuppressLint("ResourceType")
inline fun Ui.circularProgress(
  @IdRes id: Int,
  @StyleRes theme: Int = NO_THEME,
  initView: CircularProgressBar.() -> Unit = {}
): CircularProgressBar {
  return ctx.circularProgress(id, theme, initView)
}

private const val GPLUS_GREEN = 0xFF_3E_80_2F.toInt()
private const val GPLUS_YELLOW = 0xFF_F4_B4_00.toInt()
private const val GPLUS_BLUE = 0xFF_42_7F_ED.toInt()
private const val GPLUS_RED = 0xFF_B2_34_24.toInt()

fun CoordinatorLayout.addCircularProgress(viewId: Int): CircularProgressBar {
  return add(circularProgress(viewId) {
    elevation = 4f
    isVisible = false
    isIndeterminate = true
    indeterminateDrawable = CircularProgressDrawable
      .Builder(context)
      .colors(intArrayOf(GPLUS_GREEN, GPLUS_YELLOW, GPLUS_BLUE, GPLUS_RED))
      .rotationSpeed(1.0F)
      .sweepSpeed(1.0F)
      .strokeWidth(dp(4f))
      .minSweepAngle(10)
      .maxSweepAngle(300)
      .build()
  }, defaultLParams(dip(40), dip(40)) {
    gravity = gravityCenter
  })
}
