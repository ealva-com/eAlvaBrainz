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

package com.ealva.brainzsvc.art

import android.content.Intent
import android.net.Uri
import android.util.Size

/**
 * It's expected that [location] will be the sole determinant for equality. This interface
 * is provided as common facade over the source of images.
 */
@Suppress("unused")
public interface RemoteImage : Comparable<RemoteImage> {
  public val location: Uri
  public val sizeBucket: SizeBucket
  public val sourceLogoDrawableRes: Int
  public val sourceIntent: Intent
  public val actualSize: Size?
}
