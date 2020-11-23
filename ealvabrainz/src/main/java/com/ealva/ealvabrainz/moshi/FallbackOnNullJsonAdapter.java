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

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Contains the delegate used for the type and a fallback in case the json value is "null". This
 * adapter helps us implement the NullObject pattern in all our data classes. No nulls, only
 * empty strings, empty lists, and NullObject interface implementations;
 * <br/><br/>
 * <pre>
 *    data class MyData(val value: String = "NullObjectName") {
 *      companion object {
 *        val NullMyData = MyData()
 *      }
 *    }
 *
 *   val MyData.isNullObject get() = this === NullMyData
 *
 *    data class ContainsMyData(
 *      {@literal @}field:FallbackOnNull val value: MyData = MyData.NullMyData
 *    )
 * </pre>
 * <br/>
 * <br/>
 * When Moshi encounters '"mydata": null' this adapter sees the null and returns the NullObject
 * instead
 * @see com.ealva.ealvabrainz.brainz.data.FallbackMap
 */
final class FallbackOnNullJsonAdapter<T> extends JsonAdapter<T> {
  final JsonAdapter<T> delegate;
  private final T fallback;
  final String fallbackType;

  FallbackOnNullJsonAdapter(JsonAdapter<T> delegate, T fallback, String rawType) {
    this.delegate = delegate;
    this.fallback = fallback;
    this.fallbackType = rawType;
  }

  /**
   * Get the next T from the reader or the fallback object if null
   *
   * @param reader json source
   * @return an instance of T from the delegate or the {@code fallback} if the token is null
   * @throws IOException from JsonReader if peek or read
   */
  @Override public T fromJson(JsonReader reader) throws IOException {
    if (reader.peek() == JsonReader.Token.NULL) {
      reader.nextNull();
      return fallback;
    }
    return delegate.fromJson(reader);
  }

  /**
   * Directly delgates to "real" {@code JsonAdapter<T>}
   */
  @Override public void toJson(@NotNull JsonWriter writer, T value) throws IOException {
    delegate.toJson(writer, value);
  }

  @Override public String toString() {
    return delegate + ".fallbackOnNull(" + fallbackType + '=' + fallback + ')';
  }
}
