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

package com.ealva.ealvabrainz.browse

import com.ealva.ealvabrainz.brainz.data.Inc
import com.ealva.ealvabrainz.brainz.data.Relationships
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup
import com.ealva.ealvabrainz.common.Limit
import com.ealva.ealvabrainz.common.Offset
import com.ealva.ealvabrainz.common.QueryMap
import com.ealva.ealvabrainz.common.QueryMapItem
import com.ealva.ealvabrainz.common.buildQueryMap
import com.ealva.ealvabrainz.common.include
import com.ealva.ealvabrainz.common.limit
import com.ealva.ealvabrainz.common.offset
import com.ealva.ealvabrainz.common.putItem
import com.ealva.ealvabrainz.common.status
import com.ealva.ealvabrainz.common.types

public interface EntityBrowse<B : Inc> {
  /** What information should be included with the returned entities */
  public fun include(vararg browse: B)

  /** What relationships should be included in the returned list of releases */
  public fun relationships(vararg rels: Relationships)

  /** If Releases or Release.Groups are returned, narrow the type */
  public fun types(vararg types: ReleaseGroup.Type)

  /** If Releases are returned, narrow the status */
  public fun status(vararg status: Release.Status)
}

internal abstract class BaseBrowse<B : Inc>(
  private val browseOn: QueryMapItem
) : EntityBrowse<B> {
  private var incSet: MutableSet<Inc> = mutableSetOf()
  private var typeSet: Set<ReleaseGroup.Type>? = null
  private var statusSet: Set<Release.Status>? = null

  override fun include(vararg browse: B) {
    incSet.addAll(browse)
  }

  override fun relationships(vararg rels: Relationships) {
    incSet.addAll(rels)
  }

  override fun types(vararg types: ReleaseGroup.Type) {
    typeSet = types.toSet()
  }

  override fun status(vararg status: Release.Status) {
    statusSet = status.toSet()
  }

  protected open fun Set<Inc>.verifyIncludes(): Set<Inc> = apply {}

  protected open fun Set<ReleaseGroup.Type>.verifyTypes(
    includes: Set<Inc>
  ): Set<ReleaseGroup.Type> = apply {}

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
