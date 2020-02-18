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

package com.ealva.brainz.services

import com.ealva.ealvabrainz.service.CoverArtService
import com.ealva.ealvabrainz.service.MusicBrainzService
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

private const val appName = "My App"
private const val appVersion = "0.1"
private const val contactEmail = "YourName@YourAddress.com"


val brainzModule = Kodein.Module("BrainzModule") {
  bind<CoverArtService>() with singleton {
    CoverArtService.make(instance(), appName, appVersion, contactEmail)
  }
  bind<MusicBrainzService>() with singleton {
    MusicBrainzService.make(instance(), appName, appVersion, contactEmail, instance())
  }
}
