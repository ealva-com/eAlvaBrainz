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

@file:Suppress("Indentation", "MagicNumber")

package com.ealva.brainzapp.ui.artist

import android.content.Context
import android.text.TextUtils.TruncateAt.END
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import androidx.core.view.isVisible
import com.ealva.brainzapp.ui.fragment.FragmentUiContext
import com.ealva.brainzapp.ui.view.clickFlow
import com.ealva.brainzapp.ui.view.inPortrait
import com.ealva.brainzapp.R
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import splitties.dimensions.dip
import splitties.dimensions.dp
import splitties.toast.toast
import splitties.views.dsl.constraintlayout.constraintLayout
import splitties.views.dsl.constraintlayout.lParams
import splitties.views.dsl.core.Ui
import splitties.views.dsl.core.add
import splitties.views.dsl.core.endMargin
import splitties.views.dsl.core.horizontalMargin
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.startMargin
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.verticalMargin
import splitties.views.dsl.core.wrapContent
import splitties.views.dsl.material.materialCardView
import splitties.views.gravityEnd
import splitties.views.gravityStart
import splitties.views.textAppearance
import splitties.views.textResource
import com.ealva.brainzapp.R.id.release_item_artist as ID_ARTIST
import com.ealva.brainzapp.R.id.release_item_barcode as ID_BARCODE
import com.ealva.brainzapp.R.id.release_item_barcode_label as ID_BARCODE_LABEL
import com.ealva.brainzapp.R.id.release_item_card as ID_CARD
import com.ealva.brainzapp.R.id.release_item_cat_number as ID_CATALOG_NUM
import com.ealva.brainzapp.R.id.release_item_catalog_label as ID_CATALOG_LABEL
import com.ealva.brainzapp.R.id.release_item_constraint as ID_CONSTRAINT
import com.ealva.brainzapp.R.id.release_item_country as ID_COUNTRY
import com.ealva.brainzapp.R.id.release_item_date as ID_DATE
import com.ealva.brainzapp.R.id.release_item_format as ID_FORMAT
import com.ealva.brainzapp.R.id.release_item_name as ID_RELEASE_NAME
import com.ealva.brainzapp.R.id.release_item_record_label as ID_RECORD_LABEL
import com.ealva.brainzapp.R.id.release_item_tracks as ID_TRACKS

class ReleaseItemUi(uiContext: FragmentUiContext, onClick: (v: View) -> Unit) : Ui {

  private val scope = uiContext.scope
  override val ctx: Context = uiContext.context

  private val releaseName: TextView = textView(ID_RELEASE_NAME) {
    ellipsize = END
    maxLines = 1
    textAppearance = R.style.TextAppearance_MaterialComponents_Subtitle1
  }

  private val artist: TextView = textView(ID_ARTIST) {
    textAppearance = R.style.TextAppearance_MaterialComponents_Body2
    movementMethod = LinkMovementMethod.getInstance()
  }

  private val recordLabel: TextView = textView(ID_RECORD_LABEL) {
    textAppearance = R.style.TextAppearance_MaterialComponents_Body2
    movementMethod = LinkMovementMethod.getInstance()
    gravity = gravityStart
  }

  private val format: TextView = textView(ID_FORMAT) {
    textAppearance = R.style.TextAppearance_MaterialComponents_Body2
    gravity = gravityStart
  }

  private val tracks: TextView = textView(ID_TRACKS) {
    textAppearance = R.style.TextAppearance_MaterialComponents_Body2
    gravity = gravityStart
  }

  private val country: TextView = textView(ID_COUNTRY) {
    textAppearance = R.style.TextAppearance_MaterialComponents_Body2
    gravity = gravityEnd
  }

  private val date: TextView = textView(ID_DATE) {
    textAppearance = R.style.TextAppearance_MaterialComponents_Body2
    gravity = gravityStart
  }

  private val catalogNumber: TextView = textView(ID_CATALOG_NUM) {
    textAppearance = R.style.TextAppearance_MaterialComponents_Caption
    gravity = gravityStart
  }

  private val barcode: TextView = textView(ID_BARCODE) {
    textAppearance = R.style.TextAppearance_MaterialComponents_Caption
    gravity = gravityStart
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  override val root: MaterialCardView = materialCardView(ID_CARD) {
    radius = dp(10)
    cardElevation = dp(4)

    val catalogLabel = textView(ID_CATALOG_LABEL) {
      textResource = R.string.CatNumberLabel
      textAppearance = R.style.TextAppearance_MaterialComponents_Caption
      gravity = gravityStart
    }

    val barcodeLabel = textView(ID_BARCODE_LABEL) {
      textResource = R.string.BarcodeLabel
      textAppearance = R.style.TextAppearance_MaterialComponents_Caption
      gravity = gravityEnd
    }

    add(constraintLayout(ID_CONSTRAINT) {
      add(releaseName, lParams(height = wrapContent) {
        startToStart = PARENT_ID
        topToTop = PARENT_ID
        endToEnd = PARENT_ID
        bottomToTop = ID_ARTIST
      })

      add(artist, lParams(height = wrapContent) {
        startToStart = PARENT_ID
        topToBottom = ID_RELEASE_NAME
        endToEnd = PARENT_ID
        bottomToTop = ID_RECORD_LABEL
      })

      add(recordLabel, lParams(height = wrapContent) {
        startToStart = PARENT_ID
        topToBottom = ID_ARTIST
        endToEnd = PARENT_ID
        bottomToTop = ID_FORMAT
      })

      if (inPortrait) {
        constrainInPortrait(catalogLabel, barcodeLabel)
      } else {
        constrainInLandscape(catalogLabel, barcodeLabel)
      }
    }, lParams(matchParent, wrapContent) {
      verticalMargin = dip(8)
      horizontalMargin = dip(8)
    })

    layoutParams = ViewGroup.MarginLayoutParams(matchParent, wrapContent).apply {
      verticalMargin = dip(4)
      horizontalMargin = dip(8)
    }
  }.also { card ->
    card.clickFlow()
      .onEach { onClick(card) }
      .launchIn(scope)
  }

  private fun ConstraintLayout.constrainInPortrait(
    catalogLabel: TextView,
    barcodeLabel: TextView
  ) {
    add(format, lParams(wrapContent, wrapContent) {
      startToStart = PARENT_ID
      topToBottom = ID_RECORD_LABEL
      endToStart = ID_TRACKS
      bottomToTop = ID_CATALOG_LABEL
    })

    add(tracks, lParams(height = wrapContent) {
      startToEnd = ID_FORMAT
      endToStart = ID_COUNTRY
      baselineToBaseline = ID_FORMAT
      startMargin = dip(6)
    })

    add(country, lParams(wrapContent, wrapContent) {
      startToEnd = ID_TRACKS
      endToStart = ID_DATE
      baselineToBaseline = ID_FORMAT
      horizontalMargin = dip(4)
    })

    add(date, lParams(wrapContent, wrapContent) {
      startToEnd = ID_COUNTRY
      endToEnd = PARENT_ID
      baselineToBaseline = ID_FORMAT
      horizontalMargin = dip(4)
    })

    add(catalogLabel, lParams(wrapContent, wrapContent) {
      startToStart = PARENT_ID
      topToBottom = ID_FORMAT
      endToStart = ID_CATALOG_NUM
      bottomToBottom = PARENT_ID
    })

    add(catalogNumber, lParams(height = wrapContent) {
      startToEnd = ID_CATALOG_LABEL
      endToEnd = ID_BARCODE_LABEL
      baselineToBaseline = ID_CATALOG_LABEL
    })

    add(barcodeLabel, lParams(wrapContent, wrapContent) {
      startToEnd = ID_CATALOG_NUM
      endToStart = ID_BARCODE
      baselineToBaseline = ID_CATALOG_LABEL
    })

    add(barcode, lParams(wrapContent, wrapContent) {
      startToEnd = ID_BARCODE_LABEL
      endToEnd = PARENT_ID
      baselineToBaseline = ID_CATALOG_LABEL
    })
  }

  private fun ConstraintLayout.constrainInLandscape(
    catalogLabel: TextView,
    barcodeLabel: TextView
  ) {
    add(format, lParams(wrapContent, wrapContent) {
      startToStart = PARENT_ID
      topToBottom = ID_RECORD_LABEL
      bottomToBottom = PARENT_ID
    })

    add(tracks, lParams(wrapContent, wrapContent) {
      startToEnd = ID_FORMAT
      baselineToBaseline = ID_FORMAT
      startMargin = dip(4)
    })

    add(country, lParams(wrapContent, wrapContent) {
      startToEnd = ID_TRACKS
      baselineToBaseline = ID_FORMAT
      startMargin = dip(8)
    })

    add(date, lParams(height = wrapContent) {
      startToEnd = ID_COUNTRY
      baselineToBaseline = ID_FORMAT
      startMargin = dip(4)
    })

    add(catalogLabel, lParams(height = wrapContent) {
      endToStart = ID_CATALOG_NUM
      baselineToBaseline = ID_FORMAT
    })

    add(catalogNumber, lParams(wrapContent, wrapContent) {
      endToStart = ID_BARCODE_LABEL
      baselineToBaseline = ID_FORMAT
      endMargin = dip(6)
    })

    add(barcodeLabel, lParams(wrapContent, wrapContent) {
      endToStart = ID_BARCODE
      baselineToBaseline = ID_FORMAT
    })

    add(barcode, lParams(wrapContent, wrapContent) {
      endToEnd = PARENT_ID
      baselineToBaseline = ID_FORMAT
    })
  }

  fun bind(release: ReleaseItem) {
    val none = ctx.getString(R.string.NoneInBrackets)

    releaseName.text = release.title.value

    artist.setText(
      release.artistCredits.toSpannable { credit -> toast("clicked ${credit.artistName}") },
      TextView.BufferType.SPANNABLE
    )

    recordLabel.isVisible = release.labels.isNotEmpty()
    recordLabel.setText(
      release.labels.toSpannable(ctx) { label -> toast("clicked ${label.name}") },
      TextView.BufferType.SPANNABLE
    )

    format.text = release.format
    tracks.text = release.tracks
    date.text = release.date
    country.text = release.country

    catalogNumber.text = if (release.catalogNumber.isNotBlank()) release.catalogNumber else none
    barcode.text = if (release.barcode.isNotBlank()) release.barcode else none
  }
}
