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

import com.ealva.ealvabrainz.brainz.data.FallbackMap;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonQualifier;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * Indicates that the annotated field may be {@code null} in the json source and provides
 * a fallback value. In our case typically a "NullObject"
 *
 * <p>To {@linkplain FallbackOnNull} {@linkplain FallbackOnNull#ADAPTER_FACTORY} must be added to
 * {@linkplain Moshi Moshi}:
 */
@Documented
@JsonQualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
public @interface FallbackOnNull {

  JsonAdapter.Factory ADAPTER_FACTORY = new JsonAdapter.Factory() {
    @Override public JsonAdapter<?> create(@NotNull Type type,
                                           @NotNull Set<? extends Annotation> annotations,
                                           @NotNull Moshi moshi) {

      Pair<FallbackOnNull, Set<Annotation>>
        nextAnnotations = Annotations.nextAnnotations(annotations, FallbackOnNull.class);
      if (nextAnnotations == null) { return null; }

      Class<?> rawType = Types.getRawType(type);
      String fallbackType = rawType.getName();

      Object fallback = retrieveFallback(nextAnnotations.getFirst(), fallbackType);

      if (nextAnnotations.getSecond() == null) { return null; }

      return new FallbackOnNullJsonAdapter<>(moshi.adapter(type, nextAnnotations.getSecond()),
                                             fallback,
                                             fallbackType);
    }

    private Object retrieveFallback(@SuppressWarnings("unused") FallbackOnNull annotation,
                                    String fallbackType) {
      return FallbackMap.INSTANCE.get(fallbackType);
    }
  };
}
