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
import com.ealva.ealvabrainz.lucene.ExclusiveRange
import com.ealva.ealvabrainz.lucene.InclusiveRange
import com.ealva.ealvabrainz.lucene.Term
import com.ealva.ealvabrainz.lucene.and
import com.ealva.ealvabrainz.lucene.or

@BrainzMarker
public interface NumberTermBuilder {
  public infix fun Int.orTerm(other: Int): Term
  public infix fun Int.andTerm(other: Int): Term
  public infix fun Int.inclusive(other: Int): InclusiveRange
  public infix fun Int.exclusive(other: Int): ExclusiveRange

  public infix fun Long.orTerm(other: Long): Term
  public infix fun Long.andTerm(other: Long): Term
  public infix fun Long.inclusive(other: Long): InclusiveRange
  public infix fun Long.exclusive(other: Long): ExclusiveRange

  public infix fun Double.orTerm(other: Double): Term
  public infix fun Double.andTerm(other: Double): Term
  public infix fun Double.inclusive(other: Double): InclusiveRange
  public infix fun Double.exclusive(other: Double): ExclusiveRange

  public companion object {
    public operator fun invoke(): NumberTermBuilder = NumberTermBuilderImpl
  }
}

private object NumberTermBuilderImpl : NumberTermBuilder {
  override fun Int.orTerm(other: Int): Term = Term(this) or Term(other)
  override fun Int.andTerm(other: Int): Term = Term(this) and Term(other)
  override fun Int.inclusive(other: Int): InclusiveRange = InclusiveRange(Term(this), Term(other))
  override fun Int.exclusive(other: Int): ExclusiveRange = ExclusiveRange(Term(this), Term(other))

  override fun Long.orTerm(other: Long): Term = Term(this) or Term(other)
  override fun Long.andTerm(other: Long): Term = Term(this) and Term(other)
  override fun Long.inclusive(other: Long): InclusiveRange = InclusiveRange(Term(this), Term(other))
  override fun Long.exclusive(other: Long): ExclusiveRange = ExclusiveRange(Term(this), Term(other))

  override fun Double.orTerm(other: Double): Term = Term(this) or Term(other)
  override fun Double.andTerm(other: Double): Term = Term(this) and Term(other)
  override fun Double.inclusive(other: Double): InclusiveRange =
    InclusiveRange(Term(this), Term(other))
  override fun Double.exclusive(other: Double): ExclusiveRange =
    ExclusiveRange(Term(this), Term(other))
}
