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

import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.common.BrainzMarker
import com.ealva.ealvabrainz.lucene.SingleTerm
import com.ealva.ealvabrainz.lucene.Term
import com.ealva.ealvabrainz.lucene.and
import com.ealva.ealvabrainz.lucene.or

public class ReleaseStatusTerm internal constructor(internal var term: Term) : Term() {
  override fun appendTo(builder: StringBuilder): StringBuilder = term.appendTo(builder)

  @BrainzMarker
  public interface Builder {
    public fun Release.Status.or(other: Release.Status): ReleaseStatusTerm
    public fun Release.Status.and(other: Release.Status): ReleaseStatusTerm
    public fun ReleaseStatusTerm.or(other: Release.Status): ReleaseStatusTerm
    public fun ReleaseStatusTerm.and(other: Release.Status): ReleaseStatusTerm

    public companion object {
      public operator fun invoke(): Builder = ReleaseStatusTermBuilder
    }
  }
}

private object ReleaseStatusTermBuilder : ReleaseStatusTerm.Builder {
  override fun Release.Status.or(other: Release.Status): ReleaseStatusTerm {
    return ReleaseStatusTerm(SingleTerm(value) or SingleTerm(other.value))
  }

  override fun Release.Status.and(other: Release.Status): ReleaseStatusTerm {
    return ReleaseStatusTerm(SingleTerm(value) and SingleTerm(other.value))
  }

  override fun ReleaseStatusTerm.or(other: Release.Status): ReleaseStatusTerm {
    return ReleaseStatusTerm(term or SingleTerm(other.value))
  }

  override fun ReleaseStatusTerm.and(other: Release.Status): ReleaseStatusTerm {
    return ReleaseStatusTerm(term and SingleTerm(other.value))
  }
}
