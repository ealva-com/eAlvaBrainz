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

package com.ealva.brainzsvc.service.lookup

import com.ealva.brainzsvc.common.BrainzInvalidStatusException
import com.ealva.brainzsvc.common.BrainzInvalidTypeException
import com.ealva.ealvabrainz.brainz.data.Inc
import com.ealva.ealvabrainz.brainz.data.Relationships
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.joinOrNull

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
   * [Release.Type] can be specified to further narrow results
   */
  public fun types(vararg types: Release.Type)

  /**
   * If entities includes Releases a [Release.Status] can be specified to
   * further narrow results
   */
  public fun status(vararg status: Release.Status)
}

/**
 * Base class just implements the collection of various includes
 */
internal abstract class BaseEntityLookup<M : Inc> : EntityLookup<M> {
  protected val incSet: MutableSet<Inc> = mutableSetOf()

  override fun include(vararg misc: M) {
    incSet.addAll(misc)
  }

  override fun relationships(vararg rels: Relationships) {
    incSet.addAll(rels)
  }

  val include: String?
    get() = incSet.joinOrNull()
}

/**
 * Adds subquery to base entity lookups
 */
internal abstract class BaseSubqueryLookup<M : Inc> :
  BaseEntityLookup<M>(), EntitySubqueryLookup<M> {

  protected var typeSet: Set<Release.Type>? = null
  protected var statusSet: Set<Release.Status>? = null

  override fun types(vararg types: Release.Type) {
    typeSet = types.toSet()
  }

  override fun status(vararg status: Release.Status) {
    statusSet = status.toSet()
  }
}

internal fun Set<Release.Type>.ensureValidType(
  incSet: Set<Inc>
): Set<Release.Type> = apply {
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
