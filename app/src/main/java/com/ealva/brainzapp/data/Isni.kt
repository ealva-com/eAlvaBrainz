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

package com.ealva.brainzapp.data

import android.content.Intent
import android.net.Uri
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat

inline class Isni(val value: String) {
  companion object {
    val NullIsni = Isni("")
  }
}

@Suppress("NOTHING_TO_INLINE")
inline fun String.toIsni() = Isni(this)

inline val Isni.appearsValid: Boolean
  get() = value.length == 16

inline val Isni.displayValue: String
  get() {
    check(appearsValid)
    return buildString {
      append(value.substring(0..3))
      append(" ")
      append(value.substring(4..7))
      append(" ")
      append(value.substring(8..11))
      append(" ")
      append(value.substring(12..15))
    }
  }

fun TextView.setAsClickableLink(isni: Isni) {
  isClickable = true
  val spannableString = SpannableString(isni.displayValue)
  val span: ClickableSpan = object : ClickableSpan() {
    override fun onClick(textView: View) {
      ContextCompat.startActivity(
        textView.context,
        Intent(
          Intent.ACTION_VIEW,
          Uri.parse("https://www.isni.org/${isni.value}")
        ),
        null
      )
    }
  }
  spannableString.setSpan(span, 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
  setText(spannableString, TextView.BufferType.SPANNABLE)
  movementMethod = LinkMovementMethod.getInstance()
}
