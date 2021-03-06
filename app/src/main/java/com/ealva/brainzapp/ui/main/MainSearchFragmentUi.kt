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

@file:Suppress("Indentation")

package com.ealva.brainzapp.ui.main

import android.content.Context
import android.widget.ArrayAdapter
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.whenStarted
import com.ealva.brainzapp.ui.artist.ArtistSearchFragment
import com.ealva.brainzapp.ui.fragment.FragmentTagPair
import com.ealva.brainzapp.ui.fragment.FragmentUiContext
import com.ealva.brainzapp.ui.fragment.Navigation
import com.ealva.brainzapp.ui.fragment.makeUiContext
import com.ealva.brainzapp.ui.release.ReleaseSearchFragment
import com.ealva.brainzapp.ui.rgroup.ReleaseGroupSearchFragment
import com.ealva.brainzapp.ui.view.SpinnerSelection
import com.ealva.brainzapp.ui.view.SpinnerWrapper
import com.ealva.brainzapp.ui.view.itemSelectedFlow
import com.ealva.brainzapp.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import splitties.dimensions.dip
import splitties.resources.styledColor
import splitties.views.backgroundColor
import splitties.views.dsl.appcompat.toolbar
import splitties.views.dsl.constraintlayout.constraintLayout
import splitties.views.dsl.constraintlayout.lParams
import splitties.views.dsl.coordinatorlayout.coordinatorLayout
import splitties.views.dsl.coordinatorlayout.defaultLParams
import splitties.views.dsl.core.Ui
import splitties.views.dsl.core.add
import splitties.views.dsl.core.frameLayout
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.spinner
import splitties.views.dsl.core.wrapContent
import com.ealva.brainzapp.R.id.search_fragment_constraint as ID_CONSTRAINT
import com.ealva.brainzapp.R.id.search_fragment_container as ID_FRAGMENT_CONTAINER
import com.ealva.brainzapp.R.id.search_fragment_coordinator as ID_COORDINATOR
import com.ealva.brainzapp.R.id.search_fragment_spinner as ID_SPINNER
import com.ealva.brainzapp.R.id.search_fragment_toolbar as ID_TOOLBAR

internal interface MainSearchFragmentUi : Ui

internal fun Fragment.makeSearchFragmentUi(mainPresenter: MainPresenter): MainSearchFragmentUi {
  return MainSearchFragmentUiImpl(makeUiContext(), mainPresenter)
}

@Suppress("unused")
private enum class FragmentMaker(@field:IdRes val value: Int) {
  Artist(R.string.Artist) {
    override fun make(fm: FragmentManager): FragmentTagPair {
      return ArtistSearchFragment.make(fm) to tag
    }

    override val tag = ArtistSearchFragment.NAME
  },
  ReleaseGroup(R.string.ReleaseGroup) {
    override fun make(fm: FragmentManager): FragmentTagPair {
      return ReleaseGroupSearchFragment.make(fm) to tag
    }

    override val tag = ReleaseGroupSearchFragment.NAME
  },
  Release(R.string.Release) {
    override fun make(fm: FragmentManager): FragmentTagPair {
      return ReleaseSearchFragment.make(fm) to tag
    }

    override val tag = ReleaseSearchFragment.NAME
  };

  abstract fun make(fm: FragmentManager): FragmentTagPair
  abstract val tag: String

  companion object {
    private val all = values()

    fun fromIndexMake(index: Int, fm: FragmentManager): FragmentTagPair {
      require(index in all.indices)
      return all[index].make(fm)
    }

    fun tagFromIndex(index: Int): String {
      require(index in all.indices)
      return all[index].tag
    }

    fun displayNames(context: Context): List<String> = all.map { context.getString(it.value) }
  }
}

private const val SPINNER_HEIGHT = 40

private class MainSearchFragmentUiImpl(
  uiContext: FragmentUiContext,
  mainPresenter: MainPresenter
) : MainSearchFragmentUi {
  private val fm = uiContext.fragmentManager
  private val scope = uiContext.scope
  private val lifecycleOwner = uiContext.lifecycleOwner
  private val localNavigation: Navigation = Navigation.make(fm, ID_FRAGMENT_CONTAINER)

  private val toolbar: Toolbar
  private val spinnerWrapper: SpinnerWrapper

  override val ctx = uiContext.fragment.requireContext()

  @OptIn(ExperimentalCoroutinesApi::class)
  override val root = coordinatorLayout(ID_COORDINATOR) {

    add(constraintLayout(ID_CONSTRAINT) {
      toolbar = add(toolbar(ID_TOOLBAR, R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
        backgroundColor = styledColor(R.attr.colorPrimary)
        setTitle(R.string.app_name)
        mainPresenter.setActionBar(this)
      }, lParams(height = wrapContent) {
        startToStart = PARENT_ID
        topToTop = PARENT_ID
        endToEnd = PARENT_ID
        bottomToTop = ID_SPINNER
      })

      spinnerWrapper = SpinnerWrapper(add(spinner(ID_SPINNER) {
      }, lParams(height = dip(SPINNER_HEIGHT)) {
        startToStart = PARENT_ID
        topToBottom = ID_TOOLBAR
        endToEnd = PARENT_ID
        bottomToTop = ID_FRAGMENT_CONTAINER
      })).apply {
        adapter = ArrayAdapter(
          context,
          android.R.layout.simple_spinner_item,
          FragmentMaker.displayNames(context)
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
      }

      add(
        frameLayout(ID_FRAGMENT_CONTAINER) { },
        lParams {
          startToStart = PARENT_ID
          topToBottom = ID_SPINNER
          endToEnd = PARENT_ID
          bottomToBottom = PARENT_ID
        }
      )
    },
      defaultLParams(matchParent, matchParent) { }
    )
  }.also {
    scope.launch {
      lifecycleOwner.whenStarted {
        spinnerWrapper.itemSelectedFlow()
          .onEach { spinnerSelection(it) }
          .launchIn(scope)
      }
    }
  }

  private fun spinnerSelection(selection: SpinnerSelection) {
    if (FragmentMaker.tagFromIndex(selection.position) != localNavigation.currentTag) {
      val (fragment, tag) = FragmentMaker.fromIndexMake(selection.position, fm)
      localNavigation.replace(fragment, tag)
    }
  }
}
