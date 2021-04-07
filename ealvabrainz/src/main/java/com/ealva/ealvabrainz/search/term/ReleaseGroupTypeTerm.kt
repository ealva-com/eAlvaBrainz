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

import com.ealva.ealvabrainz.brainz.data.ReleaseGroup
import com.ealva.ealvabrainz.common.BrainzMarker
import com.ealva.ealvabrainz.lucene.Phrase
import com.ealva.ealvabrainz.lucene.Term
import com.ealva.ealvabrainz.lucene.and
import com.ealva.ealvabrainz.lucene.or

public class ReleaseGroupTypeTerm internal constructor(internal var term: Term) : Term() {
  override fun appendTo(builder: StringBuilder): StringBuilder = term.appendTo(builder)

  @BrainzMarker
  public interface Builder {
    public infix fun ReleaseGroup.Type.or(other: ReleaseGroup.Type): ReleaseGroupTypeTerm
    public infix fun ReleaseGroup.Type.and(other: ReleaseGroup.Type): ReleaseGroupTypeTerm
    public infix fun ReleaseGroupTypeTerm.or(other: ReleaseGroup.Type): ReleaseGroupTypeTerm
    public infix fun ReleaseGroupTypeTerm.and(other: ReleaseGroup.Type): ReleaseGroupTypeTerm

    public companion object {
      public operator fun invoke(): Builder = ReleaseGroupTypeTermBuilder
    }
  }
}

private object ReleaseGroupTypeTermBuilder : ReleaseGroupTypeTerm.Builder {
  override infix fun ReleaseGroup.Type.or(other: ReleaseGroup.Type): ReleaseGroupTypeTerm {
    return ReleaseGroupTypeTerm(Phrase(value) or Phrase(other.value))
  }

  override infix fun ReleaseGroup.Type.and(other: ReleaseGroup.Type): ReleaseGroupTypeTerm {
    return ReleaseGroupTypeTerm(Phrase(value) and Phrase(other.value))
  }

  override infix fun ReleaseGroupTypeTerm.or(other: ReleaseGroup.Type): ReleaseGroupTypeTerm {
    return ReleaseGroupTypeTerm(term or Phrase(other.value))
  }

  override infix fun ReleaseGroupTypeTerm.and(other: ReleaseGroup.Type): ReleaseGroupTypeTerm {
    return ReleaseGroupTypeTerm(term and Phrase(other.value))
  }
}
