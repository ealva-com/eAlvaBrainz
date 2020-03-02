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

import android.content.Context
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.StateListDrawable
import android.view.Gravity
import android.widget.RatingBar
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.materialdesigniconic.MaterialDesignIconic
import com.mikepenz.iconics.utils.colorInt
import com.mikepenz.iconics.utils.sizePx

fun RatingBar.setStarRatingDrawable(
  @ColorInt notPressedColor: Int,
  @ColorInt pressedColor: Int,
  size: Int,
  padding: Int,
  contourWidth: Int
) {
  val backgroundDrawable =
    context.makeRatingBarBackgroundDrawable(notPressedColor, size, padding)
  val layerDrawable =
    LayerDrawable(
      arrayOf<Drawable>(
        backgroundDrawable,
        backgroundDrawable,
        context.makeRatingBarProgressDrawable(
          notPressedColor,
          pressedColor,
          size,
          padding,
          contourWidth
        )
      )
    )
  layerDrawable.setId(0, android.R.id.background)
  layerDrawable.setId(1, android.R.id.secondaryProgress)
  layerDrawable.setId(2, android.R.id.progress)
  progressDrawable = layerDrawable
}

private fun Context.makeRatingBarProgressDrawable(
  @ColorInt notPressedColor: Int,
  @ColorInt pressedColor: Int,
  size: Int,
  padding: Int,
  contourWidth: Int
): StateListDrawable {
  val res = StateListDrawable()
  res.addState(
    intArrayOf(android.R.attr.state_pressed, android.R.attr.state_window_focused),
    makeTiledBitmapDrawable(
      MaterialDesignIconic.Icon.gmi_star,
      pressedColor,
      size,
      padding,
      contourWidth,
      true
    )
  )
  val notPressedStar = makeTiledBitmapDrawable(
    MaterialDesignIconic.Icon.gmi_star,
    notPressedColor,
    size,
    padding,
    contourWidth,
    true
  )
  res.addState(
    intArrayOf(android.R.attr.state_focused, android.R.attr.state_window_focused),
    notPressedStar
  )
  res.addState(
    intArrayOf(android.R.attr.state_selected, android.R.attr.state_window_focused),
    notPressedStar
  )
  res.addState(intArrayOf(), notPressedStar)
  return res
}

private fun Context.makeTiledBitmapDrawable(
  icon: MaterialDesignIconic.Icon,
  @ColorInt color: Int,
  size: Int,
  padding: Int,
  contour: Int,
  clip: Boolean
): Drawable {
  val bitmapDrawable = BitmapDrawable(
    resources,
    IconicsDrawable(this, icon).apply {
      colorInt = color
      sizePx = size
      paddingPx = padding
      contourWidthPx = contour
    }.toBitmap()
  ).apply {
    setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.CLAMP)
  }
  return if (clip) {
    ClipDrawable(bitmapDrawable, Gravity.START, ClipDrawable.HORIZONTAL)
  } else {
    bitmapDrawable
  }
}

private fun Context.makeRatingBarBackgroundDrawable(
  @ColorInt color: Int,
  @DimenRes sizeRes: Int,
  @DimenRes paddingRes: Int
): StateListDrawable {
  return StateListDrawable().apply {
    val outlineStar = makeTiledBitmapDrawable(
      MaterialDesignIconic.Icon.gmi_star_outline,
      color,
      sizeRes,
      paddingRes,
      0,
      false
    )
    addStateDrawable(outlineStar, android.R.attr.state_pressed)
    addStateDrawable(outlineStar, android.R.attr.state_focused)
    addStateDrawable(outlineStar, android.R.attr.state_selected)
    addState(intArrayOf(), outlineStar)
  }
}

private fun StateListDrawable.addStateDrawable(drawable: Drawable, state: Int) {
  addState(intArrayOf(state, android.R.attr.state_window_focused), drawable)
}
