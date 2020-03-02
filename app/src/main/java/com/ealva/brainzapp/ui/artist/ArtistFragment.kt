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

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ealva.brainzapp.ui.fragment.FragmentUiContext
import com.ealva.brainzapp.ui.fragment.Navigation
import com.ealva.brainzapp.ui.fragment.makeUiContext
import com.ealva.brainzapp.ui.main.instantiate
import com.ealva.brainzsvc.common.ArtistName
import com.ealva.brainzsvc.common.toArtistName
import com.ealva.brainzsvc.service.MusicBrainzService
import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.brainz.data.appearsValid
import com.ealva.ealvabrainz.brainz.data.toArtistMbid

private const val KEY_MBID = "ArtistMbid_ArtistFragment"
private const val KEY_ARTIST_NAME = "ArtistName_ArtistFragment"

var Bundle?.artistMbid: ArtistMbid
  get() {
    check(this != null) { "Null argument bundle" }
    val mbid = getString(KEY_MBID)?.toArtistMbid()
    check(mbid != null && mbid.appearsValid()) { "Bad Artist Id $mbid" }
    return mbid
  }
  set(value) {
    check(this != null) { "Null argument bundle" }
    putString(KEY_MBID, value.value)
  }

var Bundle?.artistName: ArtistName
  get() {
    check(this != null) { "Null argumentBundle" }
    return getString(KEY_ARTIST_NAME)?.toArtistName() ?: "Unknown".toArtistName()
  }
  set(value) {
    check(this != null) { "Null argumentBundle" }
    putString(KEY_ARTIST_NAME, value.value)
  }

class ArtistFragment(
  private val brainz: MusicBrainzService,
  private val navigation: Navigation
) : Fragment() {

  private var artistMbid: ArtistMbid = "".toArtistMbid()
  private var artistName: ArtistName = "".toArtistName()
  private lateinit var viewModel: ArtistViewModel
  private lateinit var uiContext: FragmentUiContext
  private lateinit var ui: ArtistFragmentUi

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    artistMbid = arguments.artistMbid
    artistName = arguments.artistName
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
    ui = ArtistFragmentUi(uiContext, viewModel, artistMbid, artistName)
    return ui.root
  }

  companion object {
    val NAME: String = ArtistFragment::class.java.name

    fun make(navigation: Navigation, brainz: MusicBrainzService): ArtistFragment =
      ArtistFragment(brainz, navigation)

    fun make(fm: FragmentManager, mbid: ArtistMbid, name: ArtistName): ArtistFragment =
      fm.instantiate(
        Bundle().apply {
          artistMbid = mbid
          artistName = name
        }
      )
  }
}
