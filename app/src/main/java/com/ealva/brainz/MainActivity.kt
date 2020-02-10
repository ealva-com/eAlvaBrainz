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

package com.ealva.brainz

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import splitties.views.backgroundColor
import splitties.views.dsl.constraintlayout.centerInParent
import splitties.views.dsl.constraintlayout.constraintLayout
import splitties.views.dsl.constraintlayout.lParams
import splitties.views.dsl.core.add
import splitties.views.dsl.core.textView
import splitties.views.gravityCenter
import com.ealva.ealvabrainz.R.id.main_constraint as ID_MAIN_CONSTRAINT
import com.ealva.ealvabrainz.R.id.main_text_center as ID_MAIN_TEXT

class MainActivity : AppCompatActivity() {

//  private lateinit var coverArt: CoverArtService
//  private lateinit var brainz: MusicBrainzService

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    /*
    For anyone seeing initial commits - these go in a ViewModel or similar
    coverArt = CoverArtService.make(
      this,
      getString(R.string.app_name),
      BuildConfig.VERSION_NAME,
      "YourEmail@YourAddress.com"
    )

    brainz = MusicBrainzService.make(
      this,
      getString(R.string.app_name),
      BuildConfig.VERSION_NAME,
      "YourEmail@YourAddress.com",
      coverArt
    )
    */

    setContentView(constraintLayout(ID_MAIN_CONSTRAINT) {
      backgroundColor = Color.YELLOW

      add(textView(ID_MAIN_TEXT) {
        text = "TBD"
        gravity = gravityCenter
      }, lParams {
        centerInParent()
      })

    })
  }
}
