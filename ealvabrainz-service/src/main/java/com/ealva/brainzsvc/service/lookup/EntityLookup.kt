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

import com.ealva.brainzsvc.service.BrainzInvalidStatusException
import com.ealva.brainzsvc.service.BrainzInvalidTypeException
import com.ealva.ealvabrainz.brainz.data.Artist
import com.ealva.ealvabrainz.brainz.data.Include
import com.ealva.ealvabrainz.brainz.data.Relationships
import com.ealva.ealvabrainz.brainz.data.Release

/**
 * All entity lookups have these basic parameters
 */
public interface EntityLookup<M : Include> {
  /** Add the entity miscellaneous types to includes param */
  public fun misc(vararg misc: M)

  /** Add entity relationships to includes param */
  public fun relationships(vararg rels: Relationships)
}

/**
 * Several entity lookups have an additional subquery list of related entities
 */
public interface EntitySubqueryLookup<S : Include, M : Include> : EntityLookup<M> {
  public fun subquery(vararg subquery: S)

  /**
   * If entities include [Artist.Subquery.Releases] or [Artist.Subquery.ReleaseGroups] the
   * [Release.Type] can be specified to further narrow results
   */
  public fun types(vararg types: Release.Type)

  /**
   * If entities includes [Artist.Subquery.Releases] a [Release.Status] can be specified to
   * further narrow results
   */
  public fun status(vararg status: Release.Status)
}

/**
 * Base class just implements the collection of various includes
 */
internal abstract class BaseEntityLookup<M : Include> : EntityLookup<M> {
  protected val includeSet: MutableSet<Include> = mutableSetOf()

  override fun misc(vararg misc: M) {
    includeSet.addAll(misc)
  }

  override fun relationships(vararg rels: Relationships) {
    includeSet.addAll(rels)
  }
}

/**
 * Adds subquery to base entity lookups
 */
internal abstract class BaseSubqueryLookup<S : Include, M : Include> :
  BaseEntityLookup<M>(), EntitySubqueryLookup<S, M> {

  protected var typeSet: Set<Release.Type>? = null
  protected var statusSet: Set<Release.Status>? = null

  override fun subquery(vararg subquery: S) {
    includeSet.addAll(subquery)
  }

  override fun types(vararg types: Release.Type) {
    typeSet = types.toSet()
  }

  override fun status(vararg status: Release.Status) {
    statusSet = status.toSet()
  }
}

internal fun Set<Release.Type>.ensureValidType(
  include: Set<Include>
): Set<Release.Type> = apply {
  if (isNotEmpty() && include.doesNotContainReleasesOrGroups()) throw BrainzInvalidTypeException()
}

internal fun Set<Release.Status>.ensureValidStatus(
  include: Set<Include>
): Set<Release.Status> = apply {
  if (isNotEmpty() && include.doesNotContainReleases()) throw BrainzInvalidStatusException()
}

@Suppress("NOTHING_TO_INLINE") // only used once
private inline fun Set<Include>?.doesNotContainReleases(): Boolean =
  this == null || none { it.value == "releases" }

@Suppress("NOTHING_TO_INLINE") // only used once
private inline fun Set<Include>?.doesNotContainReleasesOrGroups(): Boolean =
  this == null || none { it.value == "releases" || it.value == "release-groups" }
