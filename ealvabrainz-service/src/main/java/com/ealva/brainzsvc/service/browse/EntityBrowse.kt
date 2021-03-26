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

package com.ealva.brainzsvc.service.browse

import com.ealva.brainzsvc.common.Limit
import com.ealva.brainzsvc.common.Offset
import com.ealva.brainzsvc.common.QueryMap
import com.ealva.brainzsvc.common.QueryMapItem
import com.ealva.brainzsvc.common.buildQueryMap
import com.ealva.brainzsvc.common.include
import com.ealva.brainzsvc.common.limit
import com.ealva.brainzsvc.common.offset
import com.ealva.brainzsvc.common.putItem
import com.ealva.brainzsvc.common.status
import com.ealva.brainzsvc.common.types
import com.ealva.ealvabrainz.brainz.data.Inc
import com.ealva.ealvabrainz.brainz.data.Relationships
import com.ealva.ealvabrainz.brainz.data.Release

public interface EntityBrowse<B : Inc> {
  /** What information should be included with the returned entities */
  public fun include(vararg browse: B)

  /** What relationships should be included in the returned list of releases */
  public fun relationships(vararg rels: Relationships)

  /** If Releases or Release.Groups are returned, narrow the type */
  public fun types(vararg types: Release.Type)

  /** If Releases are returned, narrow the status */
  public fun status(vararg status: Release.Status)
}

internal abstract class BaseBrowse<B : Inc>(
  private val browseOn: QueryMapItem
) : EntityBrowse<B> {
  private var incSet: MutableSet<Inc> = mutableSetOf()
  private var typeSet: Set<Release.Type>? = null
  private var statusSet: Set<Release.Status>? = null

  override fun include(vararg browse: B) {
    incSet.addAll(browse)
  }

  override fun relationships(vararg rels: Relationships) {
    incSet.addAll(rels)
  }

  override fun types(vararg types: Release.Type) {
    typeSet = types.toSet()
  }

  override fun status(vararg status: Release.Status) {
    statusSet = status.toSet()
  }

  protected open fun Set<Inc>.verifyIncludes(): Set<Inc> = apply {}
  protected open fun Set<Release.Type>.verifyTypes(includes: Set<Inc>): Set<Release.Type> = apply {}
  protected open fun Set<Release.Status>.verifyStatus(includes: Set<Inc>): Set<Release.Status> =
    apply {}

  fun queryMap(limit: Limit? = null, offset: Offset? = null): QueryMap = buildQueryMap {
    putItem(browseOn)
    include(incSet.verifyIncludes())
    limit(limit)
    offset(offset)
    types(typeSet?.verifyTypes(incSet))
    status(statusSet?.verifyStatus(incSet))
  }
}
