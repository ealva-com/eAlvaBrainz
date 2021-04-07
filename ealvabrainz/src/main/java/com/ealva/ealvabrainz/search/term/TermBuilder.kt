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

package com.ealva.ealvabrainz.search.term

import com.ealva.ealvabrainz.common.BrainzMarker
import com.ealva.ealvabrainz.lucene.Term
import com.ealva.ealvabrainz.lucene.and
import com.ealva.ealvabrainz.lucene.exclusive
import com.ealva.ealvabrainz.lucene.inclusive
import com.ealva.ealvabrainz.lucene.or
import com.ealva.ealvabrainz.lucene.ExclusiveRange as ExRange
import com.ealva.ealvabrainz.lucene.InclusiveRange as InRange

@BrainzMarker
public interface TermBuilder {
  public infix fun String.or(other: String): Term
  public infix fun String.and(other: String): Term
  public infix fun Term.or(other: String): Term
  public infix fun Term.and(other: String): Term
  public infix fun String.inclusive(other: String): InRange
  public infix fun String.exclusive(other: String): ExRange
  public infix fun Term.inclusive(other: String): InRange
  public infix fun Term.exclusive(other: String): ExRange

  public companion object {
    public operator fun invoke(): TermBuilder = TermBuilderImpl
  }
}

private object TermBuilderImpl : TermBuilder {
  override fun String.or(other: String): Term = Term(this).or(Term(other))
  override fun String.and(other: String): Term = Term(this).and(Term(other))
  override fun Term.or(other: String): Term = or(Term(other))
  override fun Term.and(other: String): Term = and(Term(other))
  override infix fun String.inclusive(other: String): InRange = Term(this).inclusive(Term(other))
  override infix fun String.exclusive(other: String): ExRange = Term(this).exclusive(Term(other))
  override fun Term.inclusive(other: String): InRange = inclusive(Term(other))
  override fun Term.exclusive(other: String): ExRange = exclusive(Term(other))
}
