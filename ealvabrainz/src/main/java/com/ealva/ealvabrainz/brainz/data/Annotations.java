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

package com.ealva.ealvabrainz.brainz.data;

import com.squareup.moshi.JsonQualifier;
import kotlin.Pair;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

final class Annotations {
  /**
   * Returns a pair containing the subset of annotations without jsonQualifier
   * and the jsonQualified instance, or null if annotations does not contain
   * {@code jsonQualifier}.
   */
  public static <A extends Annotation> Pair<A, Set<Annotation>> nextAnnotations(
    Set<? extends Annotation> annotations, Class<A> jsonQualifier
  ) {

    if (!jsonQualifier.isAnnotationPresent(JsonQualifier.class)) {
      throw new IllegalArgumentException(jsonQualifier + " is not a JsonQualifier.");
    }

    if (annotations.isEmpty()) {
      return null;
    }

    for (Annotation annotation : annotations) {
      if (jsonQualifier.equals(annotation.annotationType())) {
        Set<? extends Annotation> delegateAnnotations = new LinkedHashSet<>(annotations);
        delegateAnnotations.remove(annotation);

        //noinspection unchecked Protected by the if statment.
        return new Pair<>((A)annotation, Collections.unmodifiableSet(delegateAnnotations));
      }
      A delegate = findDelegatedAnnotation(annotation, jsonQualifier);
      if (delegate != null) {
        Set<? extends Annotation> delegateAnnotations = new LinkedHashSet<>(annotations);
        delegateAnnotations.remove(annotation);
        return new Pair<>(delegate, Collections.unmodifiableSet(delegateAnnotations));
      }
    }
    return null;
  }

  private static <A extends Annotation> A findDelegatedAnnotation(
    Annotation annotation, Class<A> jsonQualifier
  ) {
    for (Annotation delegatedAnnotation : annotation.annotationType().getAnnotations()) {
      if (jsonQualifier.equals(delegatedAnnotation.annotationType())) {
        //noinspection unchecked
        return (A)delegatedAnnotation;
      }
    }
    return null;
  }

  private Annotations() {}
}
