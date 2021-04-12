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

package com.ealva.brainzapp.services

import com.ealva.brainzsvc.service.BuildConfig
import com.ealva.brainzsvc.service.ContextResourceFetcher
import com.ealva.brainzsvc.service.CoverArtService
import com.ealva.brainzsvc.service.Credentials
import com.ealva.brainzsvc.service.CredentialsProvider
import com.ealva.brainzsvc.service.MusicBrainzService
import com.ealva.brainzsvc.service.Password
import com.ealva.brainzsvc.service.ResourceFetcher
import com.ealva.brainzsvc.service.UserName
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

val brainzModule: Module = module {
  single<ResourceFetcher> { ContextResourceFetcher(androidContext()) }
  single {
    CoverArtService(
      androidContext(),
      appName = BuildConfig.BRAINZ_APP_NAME,
      appVersion = BuildConfig.BRAINZ_APP_VERSION,
      contactEmail = BuildConfig.BRAINZ_CONTACT_EMAIL,
      get()
    )
  }
  single {
    MusicBrainzService(
      ctx = androidContext(),
      appName = BuildConfig.BRAINZ_APP_NAME,
      appVersion = BuildConfig.BRAINZ_APP_VERSION,
      contactEmail = BuildConfig.BRAINZ_CONTACT_EMAIL,
      coverArt = get(),
      credentialsProvider = object : CredentialsProvider {
        override val credentials: Credentials =
          Credentials(UserName(BuildConfig.BRAINZ_USERNAME), Password(BuildConfig.BRAINZ_PASSWORD))
      },
    )
  }
}
