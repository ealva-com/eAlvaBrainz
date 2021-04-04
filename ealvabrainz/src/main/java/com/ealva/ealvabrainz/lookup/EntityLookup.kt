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

package com.ealva.ealvabrainz.lookup

import com.ealva.ealvabrainz.brainz.data.Inc
import com.ealva.ealvabrainz.brainz.data.Relationships
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup
import com.ealva.ealvabrainz.brainz.data.joinOrNull
import com.ealva.ealvabrainz.common.BrainzInvalidIncludesException
import com.ealva.ealvabrainz.common.BrainzInvalidStatusException
import com.ealva.ealvabrainz.common.BrainzInvalidTypeException

/**
 * All entity lookups have these basic parameters
 */
public interface EntityLookup<M : Inc> {
  /** Add the entity miscellaneous types to includes param */
  public fun include(vararg misc: M)

  /** Add entity relationships to includes param */
  public fun relationships(vararg rels: Relationships)
}

/**
 * Several entity lookups have an additional subquery list of related entities
 */
public interface EntitySubqueryLookup<M : Inc> : EntityLookup<M> {
  /**
   * If entities include Releases or ReleaseGroups the
   * [ReleaseGroup.Type] can be specified to further narrow results
   */
  public fun types(vararg types: ReleaseGroup.Type)

  /**
   * If entities includes Releases a [Release.Status] can be specified to
   * further narrow results
   */
  public fun status(vararg status: Release.Status)
}

/**
 * Base class just implements the collection of various includes
 */
internal abstract class BaseEntityLookup<M> : EntityLookup<M> where M : Enum<M>, M : Inc {
  private val incSet: MutableSet<M> = mutableSetOf()
  private val relSet: MutableSet<Inc> = mutableSetOf()

  protected val allIncludes: Set<Inc>
    get() {
      validateInclude(incSet)
      return incSet + relSet
    }

  override fun include(vararg misc: M) {
    incSet.addAll(misc)
  }

  protected open fun validateInclude(set: Set<M>) {}

  protected fun Set<M>.ifContainsThenRequires(
    whenIncluded: Inc,
    mustInclude: Inc,
    vararg orThese: Inc
  ) {
    if (contains(whenIncluded)) {
      if (!contains(mustInclude)) {
        if (orThese.isNotEmpty()) {
          if (!any(orThese::contains)) {
            val oneOf = mutableSetOf(mustInclude).apply { addAll(orThese) }
            throw BrainzInvalidIncludesException("$whenIncluded requires one of $oneOf")
          }
        } else {
          throw BrainzInvalidIncludesException("$whenIncluded requires $mustInclude")
        }
      }
    }
  }

  override fun relationships(vararg rels: Relationships) {
    relSet.addAll(rels)
  }

  val include: String?
    get() = allIncludes.joinOrNull()
}

/**
 * Adds subquery to base entity lookups
 */
internal abstract class BaseSubqueryLookup<M> :
  BaseEntityLookup<M>(), EntitySubqueryLookup<M> where M : Enum<M>, M : Inc {

  protected var typeSet: Set<ReleaseGroup.Type>? = null
  protected var statusSet: Set<Release.Status>? = null

  override fun types(vararg types: ReleaseGroup.Type) {
    typeSet = types.toSet()
  }

  override fun status(vararg status: Release.Status) {
    statusSet = status.toSet()
  }
}

internal fun Set<ReleaseGroup.Type>.ensureValidType(
  incSet: Set<Inc>
): Set<ReleaseGroup.Type> = apply {
  if (isNotEmpty() && incSet.doesNotContainReleasesOrGroups()) throw BrainzInvalidTypeException()
}

internal fun Set<Release.Status>.ensureValidStatus(
  incSet: Set<Inc>
): Set<Release.Status> = apply {
  if (isNotEmpty() && incSet.doesNotContainReleases()) throw BrainzInvalidStatusException()
}

private fun Set<Inc>?.doesNotContainReleases(): Boolean =
  this == null || none { it.value == "releases" }

private fun Set<Inc>?.doesNotContainReleasesOrGroups(): Boolean =
  this == null || none { it.value == "releases" || it.value == "release-groups" }
