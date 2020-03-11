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

import android.graphics.Color
import android.text.TextUtils.TruncateAt.END
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import com.ealva.brainzapp.data.toDisplayString
import com.ealva.brainzapp.ui.fragment.FragmentUiContext
import com.ealva.brainzapp.ui.view.clickFlow
import com.ealva.brainzapp.ui.view.setStarRatingDrawable
import com.ealva.ealvabrainz.R
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
import splitties.views.dsl.core.ratingBar
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.verticalMargin
import splitties.views.dsl.core.wrapContent
import splitties.views.dsl.material.materialCardView
import splitties.views.gravityCenter
import splitties.views.gravityStartCenter
import splitties.views.textAppearance
import com.ealva.ealvabrainz.R.id.release_group_item_card as ID_CARD
import com.ealva.ealvabrainz.R.id.release_group_item_constraint as ID_CONSTRAINT
import com.ealva.ealvabrainz.R.id.release_group_item_date as ID_RELEASE_DATE
import com.ealva.ealvabrainz.R.id.release_group_item_name as ID_GROUP_NAME
import com.ealva.ealvabrainz.R.id.release_group_item_rating as ID_RATING_BAR
import com.ealva.ealvabrainz.R.id.release_group_item_release_count as ID_RELEASE_COUNT
import com.ealva.ealvabrainz.R.id.release_group_item_type as ID_TYPE

class ReleaseGroupItemUi(
  uiContext: FragmentUiContext,
  onClick: (v: View) -> Unit
) : Ui {
  private val groupName: TextView
  private val type: TextView
  private val releaseCount: TextView
  private val firstDate: TextView
  private val ratingBar: RatingBar

  private val scope = uiContext.scope
  override val ctx = uiContext.context

  @OptIn(ExperimentalCoroutinesApi::class)
  override val root = materialCardView(ID_CARD) {
    radius = dp(10)
    cardElevation = dp(4)
    val totalHeight = dip(88)

    add(constraintLayout(ID_CONSTRAINT) {

      groupName = add(textView(ID_GROUP_NAME) {
        ellipsize = END
        maxLines = 1
        textAppearance = R.style.TextAppearance_MaterialComponents_Subtitle1
      }, lParams(height = wrapContent) {
        startToStart = PARENT_ID
        endToEnd = PARENT_ID
        bottomToTop = ID_TYPE
      })

      type = add(textView(ID_TYPE) {
        ellipsize = END
        maxLines = 1
        textAppearance = R.style.TextAppearance_MaterialComponents_Body2
        gravity = gravityStartCenter
      }, lParams(height = wrapContent) {
        startToStart = PARENT_ID
        topToTop = PARENT_ID
        endToStart = ID_RELEASE_COUNT
        bottomToBottom = PARENT_ID
      })

      releaseCount = add(textView(ID_RELEASE_COUNT) {
        maxLines = 1
        textAppearance = R.style.TextAppearance_MaterialComponents_Body2
      }, lParams(wrapContent, wrapContent) {
        startToEnd = ID_TYPE
        topToTop = PARENT_ID
        endToEnd = PARENT_ID
        bottomToBottom = PARENT_ID
      })

      firstDate = add(textView(ID_RELEASE_DATE) {
        ellipsize = END
        maxLines = 1
        textAppearance = R.style.TextAppearance_MaterialComponents_Body2
      }, lParams(height = wrapContent) {
        startToStart = PARENT_ID
        topToBottom = ID_TYPE
        endToStart = ID_RATING_BAR
      })

      ratingBar = add(
        ratingBar(ID_RATING_BAR) {
          setIsIndicator(true)
          numStars = 5
          stepSize = 0.5f
          rating = 0f
          minimumHeight = dip(16)
          setStarRatingDrawable(Color.BLUE, Color.BLUE, dip(16), dip(1), 0)
        }, lParams(width = dip(80), height = dip(16)) {
          startToEnd = ID_RELEASE_DATE
          topToBottom = ID_TYPE
          endToEnd = PARENT_ID
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

  fun bind(releaseGroup: ReleaseGroupItem) {
    groupName.text = releaseGroup.name.value
    type.text = releaseGroup.type.toDisplayString(releaseGroup.secondaryTypes) { ctx.getString(it)}
    firstDate.text = releaseGroup.date
    ratingBar.rating = releaseGroup.rating.value
    releaseCount.text = ctx.getString(R.string.ReleaseCount, releaseGroup.releaseCount)
  }
}
