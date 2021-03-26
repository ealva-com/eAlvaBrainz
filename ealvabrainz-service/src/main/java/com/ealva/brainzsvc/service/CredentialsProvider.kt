/*
 * Copyright (c) 2021  Eric A. Snell
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

package com.ealva.brainzsvc.service

@JvmInline
public value class UserName(public val value: String)

@JvmInline
public value class Password(public val value: String)

public data class Credentials(public val userName: UserName, public val password: Password)

/**
 * If calls will be made which require authentication, clients should implement this interface and
 * pass an instance when constructing the MusicBrainzService. When username/password is needed
 * this will be used to provide the credentials. eAlvaBrainz doesn't persist this information
 * anywhere and only passes it encrypted for MusicBrainz authentication using the
 * [okhttp-digest](https://github.com/rburgst/okhttp-digest) library.
 *
 * # Important
 * The call to get credentials occurs for every function that requires authentication, needs to be
 * very responsive, is occurring during an HTTP request, and is NOT on the UI thread.
 */
public interface CredentialsProvider {
  public val credentials: Credentials
}
