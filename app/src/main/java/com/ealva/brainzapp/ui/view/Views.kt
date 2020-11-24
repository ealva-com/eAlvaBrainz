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
import android.content.res.Configuration
import android.os.Build
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.ealva.brainzsvc.service.MusicBrainzResult.Unsuccessful
import com.ealva.brainzapp.R
import com.ealva.ealvabrainz.common.ensureExhaustive
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import splitties.snackbar.longSnack

fun View.addOnTouchOvalRipple() {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    isClickable = true
    foreground = ContextCompat.getDrawable(context, R.drawable.oval_ripple)
  }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun View.clickFlow(): Flow<View> = callbackFlow {
  setOnClickListener { v ->
    offer(v)
  }
  awaitClose { setOnClickListener(null) }
}.conflate().flowOn(Dispatchers.Main)

fun LiveData<Unsuccessful>.snackErrors(
  lifecycleOwner: LifecycleOwner,
  root: CoordinatorLayout
) {
  observe(
    lifecycleOwner,
    { result ->
      when (result) {
        is Unsuccessful.ErrorResult -> root.longSnack(result.message)
        is Unsuccessful.Exceptional -> root.longSnack(result.message)
        is Unsuccessful.None -> Any()
      }.ensureExhaustive
    }
  )
}

val Unsuccessful.ErrorResult.message: String
  get() = error.error

val Unsuccessful.Exceptional.message: String
  get() = exception.typeAndMessage

val Throwable.typeAndMessage: String
  get() = "${causeOrSelf.javaClass.simpleName} $messageOrEmpty"

val Throwable.causeOrSelf: Throwable
  get() = cause ?: this

val Throwable.messageOrEmpty: String
  get() = message ?: ""

val Context.inPortrait: Boolean
  get() = Configuration.ORIENTATION_PORTRAIT == resources.configuration.orientation

inline val View.inPortrait: Boolean
  get() = context.inPortrait
