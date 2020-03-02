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

package com.ealva.brainzapp.ui.rgroup

import android.graphics.Color
import com.ealva.brainzapp.ui.fragment.FragmentUiContext
import com.ealva.brainzapp.ui.fragment.Navigation
import com.ealva.brainzsvc.service.MusicBrainzService
import splitties.views.backgroundColor
import splitties.views.dsl.coordinatorlayout.coordinatorLayout
import splitties.views.dsl.core.Ui

interface ReleaseGroupSearchUi : Ui {

  companion object {
    fun make(
      uiContext: FragmentUiContext,
      brainz: MusicBrainzService,
      navigation: Navigation
    ): ReleaseGroupSearchUi {
      return ReleaseGroupSearchUiImpl(uiContext, brainz, navigation)
    }
  }
}

internal class ReleaseGroupSearchUiImpl(
  uiContext: FragmentUiContext,
  private val brainz: MusicBrainzService,
  private val navigation: Navigation
) : ReleaseGroupSearchUi {
  override val ctx = uiContext.context
  override val root = coordinatorLayout {
    backgroundColor = Color.GREEN
  }

}
