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

import android.content.Context
import androidx.annotation.StringRes

public interface ResourceFetcher {
  public fun fetch(@StringRes stringRes: Int, vararg formatArgs: Any): String
}

public class ContextResourceFetcher(private val context: Context) : ResourceFetcher {
  override fun fetch(stringRes: Int, vararg formatArgs: Any): String {
    return context.getString(stringRes, *formatArgs)
  }
}
