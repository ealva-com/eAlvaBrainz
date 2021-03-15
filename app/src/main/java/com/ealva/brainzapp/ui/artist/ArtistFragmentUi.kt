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
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View.NO_ID
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.lifecycle.whenStarted
import androidx.viewpager2.widget.ViewPager2
import com.ealva.brainzapp.data.appearsValid
import com.ealva.brainzapp.data.setAsClickableLink
import com.ealva.brainzapp.data.toDisplayString
import com.ealva.brainzapp.ui.fragment.FragmentUiContext
import com.ealva.brainzapp.ui.main.MainPresenter
import com.ealva.brainzapp.ui.view.TabSelection
import com.ealva.brainzapp.ui.view.addCircularProgress
import com.ealva.brainzapp.ui.view.clickFlow
import com.ealva.brainzapp.ui.view.setStarRatingDrawable
// import com.ealva.brainzapp.ui.view.snackErrors
import com.ealva.brainzapp.ui.view.tabSelectionFlow
import com.ealva.brainzapp.ui.view.viewPager2
import com.ealva.brainzapp.R
import com.ealva.brainzapp.ui.view.snackErrors
import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.common.ensureExhaustive
import com.ealva.ealvalog.e
import com.ealva.ealvalog.invoke
import com.ealva.ealvalog.lazyLogger
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import fr.castorflex.android.circularprogressbar.CircularProgressBar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import splitties.dimensions.dip
import splitties.experimental.ExperimentalSplittiesApi
import splitties.resources.styledColor
import splitties.resources.styledDimenPxSize
import splitties.views.backgroundColor
import splitties.views.dsl.appcompat.toolbar
import splitties.views.dsl.constraintlayout.constraintLayout
import splitties.views.dsl.constraintlayout.lParams
import splitties.views.dsl.coordinatorlayout.appBarLParams
import splitties.views.dsl.coordinatorlayout.coordinatorLayout
import splitties.views.dsl.coordinatorlayout.defaultLParams
import splitties.views.dsl.core.Ui
import splitties.views.dsl.core.add
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.ratingBar
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.wrapContent
import splitties.views.dsl.material.EXIT_UNTIL_COLLAPSED
import splitties.views.dsl.material.MaterialComponentsStyles
import splitties.views.dsl.material.PIN
import splitties.views.dsl.material.SCROLL
import splitties.views.dsl.material.appBarLayout
import splitties.views.dsl.material.collapsingToolbarLayout
import splitties.views.dsl.material.defaultLParams
import splitties.views.gravityStart
import splitties.views.textAppearance
import splitties.views.textResource
import splitties.views.topPadding
import com.ealva.brainzapp.R.id.artist_ui_app_bar as ID_APP_BAR
import com.ealva.brainzapp.R.id.artist_ui_area as ID_AREA
import com.ealva.brainzapp.R.id.artist_ui_area_label as ID_AREA_LABEL
import com.ealva.brainzapp.R.id.artist_ui_circular_progress as ID_PROGRESS
import com.ealva.brainzapp.R.id.artist_ui_collapsing_toolbar as ID_COLLAPSING
import com.ealva.brainzapp.R.id.artist_ui_constraint as ID_CONSTRAINT
import com.ealva.brainzapp.R.id.artist_ui_coordinator as ID_COORDINATOR
import com.ealva.brainzapp.R.id.artist_ui_end_area as ID_END_AREA
import com.ealva.brainzapp.R.id.artist_ui_end_area_label as ID_END_AREA_LABEL
import com.ealva.brainzapp.R.id.artist_ui_genres as ID_GENRES
import com.ealva.brainzapp.R.id.artist_ui_genres_label as ID_GENRES_LABEL
import com.ealva.brainzapp.R.id.artist_ui_isni as ID_ISNI
import com.ealva.brainzapp.R.id.artist_ui_isni_label as ID_ISNI_LABEL
import com.ealva.brainzapp.R.id.artist_ui_isni_tag_key as KEY_ISNI_VALUE
import com.ealva.brainzapp.R.id.artist_ui_lifespan_begin as ID_LIFESPAN_BEGIN
import com.ealva.brainzapp.R.id.artist_ui_lifespan_begin_label as ID_LIFESPAN_BEGIN_LABEL
import com.ealva.brainzapp.R.id.artist_ui_lifespan_end as ID_LIFESPAN_END
import com.ealva.brainzapp.R.id.artist_ui_lifespan_end_label as ID_LIFESPAN_END_LABEL
import com.ealva.brainzapp.R.id.artist_ui_rating_bar as ID_RATING
import com.ealva.brainzapp.R.id.artist_ui_rating_label as ID_RATING_LABEL
import com.ealva.brainzapp.R.id.artist_ui_start_area as ID_START_AREA
import com.ealva.brainzapp.R.id.artist_ui_start_area_label as ID_START_AREA_LABEL
import com.ealva.brainzapp.R.id.artist_ui_tabbed_layout as ID_TABBED_LAYOUT
import com.ealva.brainzapp.R.id.artist_ui_toolbar as ID_TOOLBAR
import com.ealva.brainzapp.R.id.artist_ui_type as ID_ARTIST_TYPE
import com.ealva.brainzapp.R.id.artist_ui_type_label as ID_TYPE_LABEL
import com.ealva.brainzapp.R.id.artist_ui_view_pager as ID_PAGER

private val LOG by lazyLogger(ArtistFragmentUi::class)

class ArtistFragmentUi(
  uiContext: FragmentUiContext,
  private val mainPresenter: MainPresenter,
  private val viewModel: ArtistViewModel,
  private val artistMbid: ArtistMbid,
  private val artistName: ArtistName
) : Ui {
  private val scope = uiContext.scope
  private val lifecycleOwner = uiContext.lifecycleOwner

  private val progress: CircularProgressBar
  private val appBarLayout: AppBarLayout
  private val tabLayout: TabLayout
  private val viewPager: ViewPager2
  private val artistTypeView: TextView
  private val lifespanBeginLabel: TextView
  private val lifespanBegin: TextView
  private val startAreaLabel: TextView
  private val startArea: TextView
  private val lifespanEndLabel: TextView
  private val lifespanEnd: TextView
  private val endAreaLabel: TextView
  private val endArea: TextView
  private val areaLabel: TextView
  private val area: TextView
  private val isniLabel: TextView
  private val isni: TextView
  private val ratingBar: RatingBar
  private val genresLabel: TextView
  private val genres: TextView
  private val collapsing: CollapsingToolbarLayout
  private val toolbar: Toolbar

  override val ctx: Context = uiContext.context

  @OptIn(ExperimentalCoroutinesApi::class, ExperimentalSplittiesApi::class)
  override val root: CoordinatorLayout = coordinatorLayout(ID_COORDINATOR) {
    val materialStyles = MaterialComponentsStyles(ctx)

    progress = addCircularProgress(ID_PROGRESS)

    val actionBarSize = styledDimenPxSize(android.R.attr.actionBarSize)

    appBarLayout = add(appBarLayout(ID_APP_BAR, R.style.ThemeOverlay_MaterialComponents_ActionBar) {

      collapsing = add(collapsingToolbarLayout(ID_COLLAPSING) {
        isTitleEnabled = false

        add(constraintLayout(ID_CONSTRAINT) {
          topPadding = actionBarSize
          backgroundColor = styledColor(android.R.attr.colorBackground)
          fitsSystemWindows = true

          addLabel(
            viewId = ID_TYPE_LABEL,
            textRes = R.string.TypeLabel,
            belowView = NO_ID,
            valueView = ID_ARTIST_TYPE,
            aboveView = ID_LIFESPAN_BEGIN_LABEL
          )

          artistTypeView = addValueView(
            viewId = ID_ARTIST_TYPE,
            labelView = ID_TYPE_LABEL
          )

          lifespanBeginLabel = addLabel(
            viewId = ID_LIFESPAN_BEGIN_LABEL,
            textRes = R.string.empty,
            belowView = ID_TYPE_LABEL,
            valueView = ID_LIFESPAN_BEGIN,
            aboveView = ID_START_AREA_LABEL
          )

          lifespanBegin = addValueView(
            viewId = ID_LIFESPAN_BEGIN,
            labelView = ID_LIFESPAN_BEGIN_LABEL
          )

          startAreaLabel = addLabel(
            viewId = ID_START_AREA_LABEL,
            textRes = R.string.empty,
            belowView = ID_LIFESPAN_BEGIN_LABEL,
            valueView = ID_START_AREA,
            aboveView = ID_LIFESPAN_END_LABEL
          )

          startArea = addValueView(
            viewId = ID_START_AREA,
            labelView = ID_START_AREA_LABEL
          )

          lifespanEndLabel = addLabel(
            viewId = ID_LIFESPAN_END_LABEL,
            textRes = R.string.empty,
            belowView = ID_START_AREA_LABEL,
            valueView = ID_LIFESPAN_END,
            aboveView = ID_END_AREA_LABEL
          )

          lifespanEnd = addValueView(
            viewId = ID_LIFESPAN_END,
            labelView = ID_LIFESPAN_END_LABEL
          )

          endAreaLabel = addLabel(
            viewId = ID_END_AREA_LABEL,
            textRes = R.string.empty,
            belowView = ID_LIFESPAN_END_LABEL,
            valueView = ID_END_AREA,
            aboveView = ID_AREA_LABEL
          )

          endArea = addValueView(
            viewId = ID_END_AREA,
            labelView = ID_END_AREA_LABEL
          )

          areaLabel = addLabel(
            viewId = ID_AREA_LABEL,
            textRes = R.string.AreaLabel,
            belowView = ID_END_AREA_LABEL,
            valueView = ID_AREA,
            aboveView = ID_ISNI_LABEL
          )

          area = addValueView(
            viewId = ID_AREA,
            labelView = ID_AREA_LABEL
          )

          isniLabel = addLabel(
            viewId = ID_ISNI_LABEL,
            textRes = R.string.IsniCodeLabel,
            belowView = ID_AREA_LABEL,
            valueView = ID_ISNI,
            aboveView = ID_RATING_LABEL
          )

          isni = addValueView(
            viewId = ID_ISNI,
            labelView = ID_ISNI_LABEL
          )

          addLabel(
            viewId = ID_RATING_LABEL,
            textRes = R.string.RatingLabel,
            belowView = ID_ISNI_LABEL,
            valueView = ID_RATING,
            aboveView = ID_GENRES_LABEL
          )

          ratingBar = add(
            ratingBar(ID_RATING) {
              setIsIndicator(true)
              numStars = 5
              stepSize = 0.5f
              rating = 0f
              minimumHeight = dip(16)
              setStarRatingDrawable(Color.BLUE, Color.BLUE, dip(16), dip(1), 0)
            }, lParams(width = dip(80), height = dip(16)) {
              leftMargin = dip(8)
              startToEnd = ID_RATING_LABEL
              topToTop = ID_RATING_LABEL
              bottomToBottom = ID_RATING_LABEL
            })

          genresLabel = addLabel(
            viewId = ID_GENRES_LABEL,
            textRes = R.string.GenresLabel,
            belowView = ID_RATING_LABEL,
            valueView = ID_GENRES,
            aboveView = NO_ID
          )

          genres = addValueView(
            viewId = ID_GENRES,
            labelView = ID_GENRES_LABEL
          )
        }, defaultLParams(collapseMode = PIN))

        toolbar = add(toolbar(ID_TOOLBAR, R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
          title = artistName.value
          backgroundColor = styledColor(R.attr.colorPrimary)
          mainPresenter.setActionBar(this)
        }, defaultLParams(height = actionBarSize, collapseMode = PIN))
      }, defaultLParams(scrollFlags = SCROLL or EXIT_UNTIL_COLLAPSED))

      tabLayout = add(materialStyles.tabLayout.default(ID_TABBED_LAYOUT), defaultLParams())
    }, appBarLParams())

    viewPager = add(viewPager2(ID_PAGER) {
      adapter = ArtistFragmentPagerAdapter(uiContext)
    }, defaultLParams(width = matchParent) {
      behavior = AppBarLayout.ScrollingViewBehavior()
    })

    TabLayoutMediator(tabLayout, viewPager, true) { tab, position ->
      when (position) {
        0 -> tab.setText(R.string.Discography)
        1 -> tab.setText(R.string.Releases)
      }
    }.attach()
  }.also { root ->
    scope.launch {
      lifecycleOwner.whenStarted {
        viewModel.artist.observe(lifecycleOwner, { artist -> updateArtistInfo(artist) })
        viewModel.unsuccessful.snackErrors(lifecycleOwner, root)
        viewModel.isBusy.observe(lifecycleOwner, { busy -> progress.isVisible = busy == true })
        isni.clickFlow().onEach {
          startActivity(
            isni.context,
            Intent(
              Intent.ACTION_VIEW,
              Uri.parse("https://www.isni.org/${isni.getTag(KEY_ISNI_VALUE)}")
            ),
            null
          )
        }.launchIn(scope)
        tabLayout.tabSelectionFlow().onEach { selection ->
          when (selection) {
            is TabSelection.Reselected -> appBarLayout.setExpanded(true)
            is TabSelection.Unselected, is TabSelection.Selected -> {
            }
          }.ensureExhaustive
        }.launchIn(scope)
      }
    }
  }

  private fun ConstraintLayout.addValueView(viewId: Int, labelView: Int): TextView {
    return add(textView(viewId) {
      textAppearance = R.style.TextAppearance_MaterialComponents_Body2
      gravity = gravityStart
    }, lParams(height = wrapContent) {
      leftMargin = dip(8)
      startToEnd = labelView
      endToEnd = PARENT_ID
      baselineToBaseline = labelView
    })
  }

  private fun ConstraintLayout.addLabel(
    viewId: Int,
    textRes: Int,
    belowView: Int,
    valueView: Int,
    aboveView: Int
  ): TextView {
    return add(textView(viewId) {
      textResource = textRes
      textAppearance = R.style.EaLabelTextAppearance
    }, lParams(width = wrapContent, height = wrapContent) {
      leftMargin = dip(16)
      startToStart = PARENT_ID
      if (belowView > 0) topToBottom = belowView else topToTop = PARENT_ID
      endToStart = valueView
      if (aboveView > 0) bottomToTop = aboveView else bottomToBottom = PARENT_ID
    })
  }

  private fun updateArtistInfo(artist: DisplayArtist) {
    if (artistMbid.value != artist.mbid.value) {
      LOG.e { it("Model mbid %s != requested mbid %s", artist.mbid, artistMbid) }
    }
    toolbar.title = artist.name.value

    artistTypeView.text = artist.type.value

    val labels = artist.type.labels
    lifespanBeginLabel.textResource = labels.lifespanBeginLabel
    lifespanBegin.text = artist.lifespanBegin
    startAreaLabel.textResource = labels.startAreaLabel
    startArea.text = artist.startArea

    val lifespanEnded = artist.lifespanEnded
    lifespanEndLabel.isVisible = lifespanEnded
    lifespanEnd.isVisible = lifespanEnded
    endAreaLabel.isVisible = lifespanEnded
    endArea.isVisible = lifespanEnded

    if (lifespanEnded) {
      lifespanEndLabel.textResource = labels.lifespanEndLabel
      lifespanEnd.text = artist.lifespanEnd
      if (artist.endArea.isNotBlank()) {
        endAreaLabel.textResource = labels.endAreaLabel
        endArea.text = artist.endArea
      } else {
        endAreaLabel.isVisible = false
        endArea.isVisible = false
      }
    }

    area.text = artist.area
    val isniIsValid = artist.isni.appearsValid
    isniLabel.isVisible = isniIsValid
    isni.isVisible = isniIsValid
    if (isniIsValid) {
      isni.setAsClickableLink(artist.isni)
    }

    ratingBar.rating = artist.rating.value
    genres.text = artist.genres.toDisplayString(ctx::getString)
  }
}
