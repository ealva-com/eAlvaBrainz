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

package com.ealva.ealvabrainz.moshi;

import com.squareup.moshi.FromJson;

@SuppressWarnings("unused")
public class NullPrimitiveAdapter {
  @FromJson
  public int intFromJson(@Nullable Integer value) {
    if (value == null) {
      return 0;
    }
    //noinspection AutoUnboxing
    return value;
  }

  @FromJson
  public long longFromJson(@Nullable Long value) {
    if (value == null) {
      return 0;
    }
    //noinspection AutoUnboxing
    return value;
  }

  @FromJson
  public boolean booleanFromJson(@Nullable Boolean value) {
    if (value == null) {
      return false;
    }
    //noinspection AutoUnboxing
    return value;
  }

  @FromJson
  public double doubleFromJson(@Nullable Double value) {
    if (value == null) {
      return 0;
    }
    //noinspection AutoUnboxing
    return value;
  }

  @FromJson
  public float floatFromJson(@Nullable Float value) {
    if (value == null) {
      return 0;
    }
    //noinspection AutoUnboxing
    return value;
  }

}
