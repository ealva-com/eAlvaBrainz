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
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import com.ealva.brainzapp.ui.fragment.FragmentUiContext
import com.ealva.brainzapp.ui.view.clickFlow
import com.ealva.ealvabrainz.R
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import splitties.dimensions.dip
import splitties.dimensions.dp
import splitties.views.dsl.constraintlayout.constraintLayout
import splitties.views.dsl.constraintlayout.lParams
import splitties.views.dsl.core.Ui
import splitties.views.dsl.core.add
import splitties.views.dsl.core.horizontalMargin
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.verticalMargin
import splitties.views.dsl.core.wrapContent
import splitties.views.dsl.material.materialCardView
import splitties.views.gravityCenter
import splitties.views.textAppearance
import com.ealva.ealvabrainz.R.id.artist_search_item_card as ID_CARD
import com.ealva.ealvabrainz.R.id.artist_search_item_constraint as ID_CONSTRAINT
import com.ealva.ealvabrainz.R.id.artist_search_item_country as ID_COUNTRY
import com.ealva.ealvabrainz.R.id.artist_search_item_disambiguation as ID_DISAMBIGUATION
import com.ealva.ealvabrainz.R.id.artist_search_item_name as ID_ARTIST_NAME
import com.ealva.ealvabrainz.R.id.artist_search_item_score as ID_SCORE
import com.ealva.ealvabrainz.R.id.artist_search_item_type as ID_ARTIST_TYPE

public class ArtistSearchItemUi(
  uiContext: FragmentUiContext,
  onClick: (v: View) -> Unit
) : Ui {
  private val artistName: TextView
  private val disambiguation: TextView
  private val type: TextView
  private val score: TextView
  private val country: TextView

  private val scope = uiContext.scope
  override val ctx: Context = uiContext.context

  @OptIn(ExperimentalCoroutinesApi::class)
  override val root: MaterialCardView = materialCardView(ID_CARD) {
    radius = dp(10)
    cardElevation = dp(4)
    val totalHeight = dip(88)

    add(constraintLayout(ID_CONSTRAINT) {

      artistName = add(textView(ID_ARTIST_NAME) {
        ellipsize = END
        maxLines = 1
        textAppearance = R.style.TextAppearance_MaterialComponents_Subtitle1
      }, lParams(height = wrapContent) {
        startToStart = PARENT_ID
        endToStart = ID_SCORE
        bottomToTop = ID_DISAMBIGUATION
      })

      score = add(textView(ID_SCORE) {
        ellipsize = END
        maxLines = 1
        textAppearance = R.style.TextAppearance_MaterialComponents_Body2
      }, lParams(wrapContent, wrapContent) {
        startToEnd = ID_ARTIST_NAME
        endToEnd = PARENT_ID
        bottomToTop = ID_DISAMBIGUATION
      })

      disambiguation = add(textView(ID_DISAMBIGUATION) {
        ellipsize = END
        maxLines = 1
        textAppearance = R.style.TextAppearance_MaterialComponents_Body2
      }, lParams(height = wrapContent) {
        startToStart = PARENT_ID
        topToTop = PARENT_ID
        endToEnd = PARENT_ID
        bottomToBottom = PARENT_ID
      })

      type = add(textView(ID_ARTIST_TYPE) {
        ellipsize = END
        maxLines = 1
        textAppearance = R.style.TextAppearance_MaterialComponents_Body2
      }, lParams(height = wrapContent) {
        startToStart = PARENT_ID
        endToStart = ID_COUNTRY
        topToBottom = ID_DISAMBIGUATION
      })

      country = add(textView(ID_COUNTRY) {
        ellipsize = END
        maxLines = 1
        textAppearance = R.style.TextAppearance_MaterialComponents_Body2
      }, lParams(wrapContent, wrapContent) {
        startToEnd = ID_ARTIST_TYPE
        endToEnd = PARENT_ID
        bottomToBottom = ID_ARTIST_TYPE
      })
    }, lParams(matchParent, totalHeight, gravityCenter) {
      horizontalMargin = dip(8)
    })

    layoutParams = ViewGroup.MarginLayoutParams(matchParent, totalHeight).apply {
      verticalMargin = dip(4)
      horizontalMargin = dip(8)
    }
  }.also { card ->
    card.clickFlow()
      .onEach { onClick(card) }
      .launchIn(scope)
  }

  public fun bind(artist: ArtistSearchResult) {
    artistName.text = artist.name.value
    disambiguation.text = artist.disambiguation
    type.text = artist.type.value
    score.text = artist.score.toString()
    country.text = artist.country.alpha2
  }
}
