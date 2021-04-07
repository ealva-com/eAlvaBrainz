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

import com.ealva.ealvabrainz.brainz.data.Mbid
import com.ealva.ealvabrainz.common.BrainzMarker
import com.ealva.ealvabrainz.lucene.Term
import com.ealva.ealvabrainz.lucene.and
import com.ealva.ealvabrainz.lucene.or

@BrainzMarker
public interface MbidTermBuilder<T : Mbid> {
  public infix fun T.or(other: T): Term
  public infix fun T.and(other: T): Term
  public infix fun Term.or(other: T): Term
  public infix fun Term.and(other: T): Term

  public companion object {
    public operator fun <T : Mbid> invoke(): MbidTermBuilder<T> = MbidTermBuilderImpl()
  }
}

private class MbidTermBuilderImpl<T : Mbid> : MbidTermBuilder<T> {
  override fun T.or(other: T): Term = Term(value).or(Term(other.value))
  override fun T.and(other: T): Term = Term(value).and(Term(other.value))
  override fun Term.or(other: T): Term = or(Term(other.value))
  override fun Term.and(other: T): Term = and(Term(other.value))
}
