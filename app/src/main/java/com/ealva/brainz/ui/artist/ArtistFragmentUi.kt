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

package com.ealva.brainz.ui.artist

import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.whenStarted
import com.ealva.brainz.ui.fragment.FragmentUiContext
import com.ealva.brainz.ui.view.addCircularProgress
import com.ealva.ealvabrainz.R
import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.common.ensureExhaustive
import com.ealva.ealvabrainz.service.MusicBrainzResult.Unsuccessful
import com.ealva.ealvabrainz.service.MusicBrainzResult.Unsuccessful.ErrorResult
import com.ealva.ealvabrainz.service.MusicBrainzResult.Unsuccessful.Exceptional
import fr.castorflex.android.circularprogressbar.CircularProgressBar
import kotlinx.coroutines.launch
import splitties.dimensions.dip
import splitties.snackbar.longSnack
import splitties.toast.toast
import splitties.views.dsl.constraintlayout.constraintLayout
import splitties.views.dsl.constraintlayout.lParams
import splitties.views.dsl.coordinatorlayout.coordinatorLayout
import splitties.views.dsl.coordinatorlayout.defaultLParams
import splitties.views.dsl.core.Ui
import splitties.views.dsl.core.add
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.textView
import splitties.views.horizontalPadding
import splitties.views.textAppearance
import splitties.views.verticalPadding
import com.ealva.ealvabrainz.R.id.artist_ui_circular_progress as ID_PROGRESS
import com.ealva.ealvabrainz.R.id.artist_ui_constraint as ID_CONSTRAINT
import com.ealva.ealvabrainz.R.id.artist_ui_coordinator as ID_COORDINATOR
import com.ealva.ealvabrainz.R.id.artist_ui_name as ID_ARTIST_NAME

class ArtistFragmentUi(
  private val uiContext: FragmentUiContext,
  private val viewModel: ArtistViewModel,
  private val artistMbid: ArtistMbid
) : Ui {
  private val scope = uiContext.scope
  private val lifecycleOwner = uiContext.lifecycleOwner

  private val artistNameView: TextView
  private val progress: CircularProgressBar

  override val ctx = uiContext.context
  override val root = coordinatorLayout(ID_COORDINATOR) {
    progress = addCircularProgress(ID_PROGRESS)

    add(constraintLayout(ID_CONSTRAINT) {
      horizontalPadding = dip(16)
      verticalPadding = dip(8)

      artistNameView = add(textView(ID_ARTIST_NAME) {
        textAppearance = R.style.TextAppearance_MaterialComponents_Headline6
      }, lParams {
        startToStart = PARENT_ID
        topToTop = PARENT_ID
        endToEnd = PARENT_ID
        bottomToBottom = PARENT_ID
      })
    }, defaultLParams(matchParent, matchParent))
  }.also { root ->
    scope.launch {
      lifecycleOwner.whenStarted {
        viewModel.artist.observe(lifecycleOwner, Observer { artist ->
          toast(artist.name.value)
          artistNameView.text = artist.name.value
        })
        viewModel.unsuccessful.observe(uiContext.lifecycleOwner, Observer { result ->
          when (result) {
            is ErrorResult -> root.longSnack(result.error.error)
            is Exceptional -> root.longSnack(result.exception.message ?: "Exception")
            is Unsuccessful.None -> Any()
          }.ensureExhaustive
        })
        viewModel.isBusy.observe(uiContext.lifecycleOwner, Observer { busy ->
          progress.isVisible = busy == true
        })
        viewModel.lookupArtist(artistMbid)
      }
    }
  }
}
