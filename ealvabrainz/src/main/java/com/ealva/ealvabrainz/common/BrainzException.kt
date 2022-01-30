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

package com.ealva.ealvabrainz.common

public open class BrainzException internal constructor(
  message: String,
  cause: Throwable? = null
) : RuntimeException(message, cause)

public class BrainzInvalidTypeException : BrainzException(
  "Type is not a valid parameter unless 'include' contains releases or release-groups"
)

public class BrainzInvalidStatusException : BrainzException(
  "Status is not a valid parameter unless 'include' contains releases"
)

public class BrainzInvalidIncludesException(
  message: String
) : BrainzException(message)
