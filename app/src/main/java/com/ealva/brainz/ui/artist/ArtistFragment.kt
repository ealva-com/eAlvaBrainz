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

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ealva.brainz.ui.fragment.FragmentUiContext
import com.ealva.brainz.ui.fragment.Navigation
import com.ealva.brainz.ui.fragment.makeUiContext
import com.ealva.brainz.ui.main.instantiate
import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.brainz.data.appearsValid
import com.ealva.ealvabrainz.brainz.data.toArtistMbid
import com.ealva.ealvabrainz.service.MusicBrainzService

private const val KEY_MBID = "ArtistMbid_ArtistFragment"

val Bundle?.artistMbid: ArtistMbid
  get() {
    check(this != null) { "Null argument bundle" }
    val mbid = getString(KEY_MBID)?.toArtistMbid()
    check(mbid != null && mbid.appearsValid()) { "Bad Artist Id $mbid" }
    return mbid
  }

class ArtistFragment(
  private val brainz: MusicBrainzService,
  private val navigation: Navigation
) : Fragment() {

  private var artistMbid: ArtistMbid = "".toArtistMbid()
  private lateinit var viewModel: ArtistViewModel
  private lateinit var uiContext: FragmentUiContext
  private lateinit var ui: ArtistFragmentUi

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    artistMbid = arguments.artistMbid
  }

  override fun onAttach(context: Context) {
    super.onAttach(context)
    requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
      override fun handleOnBackPressed() {
        navigation.pop()
      }
    })
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    viewModel = getArtistViewModel(brainz)
    uiContext = makeUiContext()
    ui = ArtistFragmentUi(uiContext, viewModel, artistMbid)
    return ui.root
  }

  companion object {
    val NAME: String = ArtistFragment::class.java.name

    fun make(navigation: Navigation, brainz: MusicBrainzService): ArtistFragment =
      ArtistFragment(brainz, navigation)

    fun make(fm: FragmentManager, artistMbid: ArtistMbid): ArtistFragment =
      fm.instantiate(bundleOf(KEY_MBID to artistMbid.value))
  }
}
