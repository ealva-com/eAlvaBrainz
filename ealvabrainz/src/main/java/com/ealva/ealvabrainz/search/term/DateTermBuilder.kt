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
import com.ealva.ealvabrainz.common.Year
import com.ealva.ealvabrainz.lucene.ExclusiveRange
import com.ealva.ealvabrainz.lucene.InclusiveRange
import com.ealva.ealvabrainz.lucene.Term
import com.ealva.ealvabrainz.lucene.and
import com.ealva.ealvabrainz.lucene.or
import java.time.LocalDate

@BrainzMarker
public interface DateTermBuilder {
  public infix fun Year.or(other: Year): Term
  public infix fun Year.and(other: Year): Term
  public infix fun Year.inclusive(other: Year): InclusiveRange
  public infix fun Year.exclusive(other: Year): ExclusiveRange

  public infix fun LocalDate.or(other: LocalDate): Term
  public infix fun LocalDate.and(other: LocalDate): Term
  public infix fun LocalDate.inclusive(other: LocalDate): InclusiveRange
  public infix fun LocalDate.exclusive(other: LocalDate): ExclusiveRange

  public companion object {
    public operator fun invoke(): DateTermBuilder = DateTermBuilderImpl
  }
}

private object DateTermBuilderImpl : DateTermBuilder {
  override fun Year.or(other: Year): Term = Term(this).or(Term(other))

  override fun LocalDate.or(other: LocalDate): Term = Term(this).or(Term(other))

  override fun Year.and(other: Year): Term = Term(this).and(Term(other))

  override fun LocalDate.and(other: LocalDate): Term = Term(this).and(Term(other))

  override fun Year.inclusive(other: Year): InclusiveRange = InclusiveRange(Term(this), Term(other))

  override fun LocalDate.inclusive(other: LocalDate): InclusiveRange =
    InclusiveRange(Term(this), Term(other))

  override fun Year.exclusive(other: Year): ExclusiveRange = ExclusiveRange(Term(this), Term(other))

  override fun LocalDate.exclusive(other: LocalDate): ExclusiveRange =
    ExclusiveRange(Term(this), Term(other))
}
