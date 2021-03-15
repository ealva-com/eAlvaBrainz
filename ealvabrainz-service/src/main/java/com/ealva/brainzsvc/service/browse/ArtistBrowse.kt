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

import com.ealva.brainzsvc.service.browse.ArtistBrowse.BrowseOn
import com.ealva.ealvabrainz.brainz.MusicBrainz
import com.ealva.ealvabrainz.brainz.data.AreaMbid
import com.ealva.ealvabrainz.brainz.data.Artist
import com.ealva.ealvabrainz.brainz.data.BrowseArtistList
import com.ealva.ealvabrainz.brainz.data.Include
import com.ealva.ealvabrainz.brainz.data.RecordingMbid
import com.ealva.ealvabrainz.brainz.data.Relationships
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupMbid
import com.ealva.ealvabrainz.brainz.data.ReleaseMbid
import com.ealva.ealvabrainz.brainz.data.WorkMbid
import com.ealva.ealvabrainz.brainz.data.join
import retrofit2.Response

/**
 * Builds the artist browsing call based on [BrowseOn] type, [include], [relationships], [types],
 * and [status]
 */
public interface ArtistBrowse {
  /**
   * BrowseOn the entity related to a group of releases.
   */
  @Suppress("unused")
  public sealed class BrowseOn {
    internal abstract suspend fun execute(
      musicBrainz: MusicBrainz,
      limit: Int?,
      offset: Int?,
      includeSet: String?,
      typeSet: String?,
      statusSet: String?
    ): Response<BrowseArtistList>

    public class Area(private val areaMbid: AreaMbid) : BrowseOn() {
      override suspend fun execute(
        musicBrainz: MusicBrainz,
        limit: Int?,
        offset: Int?,
        includeSet: String?,
        typeSet: String?,
        statusSet: String?
      ): Response<BrowseArtistList> {
        return musicBrainz.browseArtists(
          areaId = areaMbid.value,
          limit = limit,
          offset = offset,
          include = includeSet,
          type = typeSet,
          status = statusSet
        )
      }
    }

    public class Recording(private val recordingMbid: RecordingMbid) : BrowseOn() {
      override suspend fun execute(
        musicBrainz: MusicBrainz,
        limit: Int?,
        offset: Int?,
        includeSet: String?,
        typeSet: String?,
        statusSet: String?
      ): Response<BrowseArtistList> {
        return musicBrainz.browseArtists(
          recordingId = recordingMbid.value,
          limit = limit,
          offset = offset,
          include = includeSet,
          type = typeSet,
          status = statusSet
        )
      }
    }

    public class Release(private val releaseMbid: ReleaseMbid) : BrowseOn() {
      override suspend fun execute(
        musicBrainz: MusicBrainz,
        limit: Int?,
        offset: Int?,
        includeSet: String?,
        typeSet: String?,
        statusSet: String?
      ): Response<BrowseArtistList> {
        return musicBrainz.browseArtists(
          releaseId = releaseMbid.value,
          limit = limit,
          offset = offset,
          include = includeSet,
          type = typeSet,
          status = statusSet
        )
      }
    }

    public class ReleaseGroup(private val releaseGroupMbid: ReleaseGroupMbid) : BrowseOn() {
      override suspend fun execute(
        musicBrainz: MusicBrainz,
        limit: Int?,
        offset: Int?,
        includeSet: String?,
        typeSet: String?,
        statusSet: String?
      ): Response<BrowseArtistList> {
        return musicBrainz.browseArtists(
          releaseGroupId = releaseGroupMbid.value,
          limit = limit,
          offset = offset,
          include = includeSet,
          type = typeSet,
          status = statusSet
        )
      }
    }

    public class Work(private val workMbid: WorkMbid) : BrowseOn() {
      override suspend fun execute(
        musicBrainz: MusicBrainz,
        limit: Int?,
        offset: Int?,
        includeSet: String?,
        typeSet: String?,
        statusSet: String?
      ): Response<BrowseArtistList> {
        return musicBrainz.browseArtists(
          workId = workMbid.value,
          limit = limit,
          offset = offset,
          include = includeSet,
          type = typeSet,
          status = statusSet
        )
      }
    }
  }

  /**
   * What information should be included with the returned entities
   */
  public fun include(vararg browse: Release.Browse)

  /**
   * What relationships should be included in the returned list of releases
   */
  public fun relationships(vararg rels: Relationships)

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

internal class ArtistBrowseOp(private val browseOn: BrowseOn) : ArtistBrowse {
  private var includeSet: MutableSet<Include> = mutableSetOf()
  private var typeSet: Set<Release.Type>? = null
  private var statusSet: Set<Release.Status>? = null

  override fun include(vararg browse: Release.Browse) {
    includeSet.addAll(browse)
  }

  override fun relationships(vararg rels: Relationships) {
    includeSet.addAll(rels)
  }

  override fun types(vararg types: Release.Type) {
    typeSet = types.toSet()
  }

  override fun status(vararg status: Release.Status) {
    statusSet = status.toSet()
  }

  suspend fun execute(
    musicBrainz: MusicBrainz,
    limit: Int? = null,
    offset: Int? = null,
  ): Response<BrowseArtistList> {
    return browseOn.execute(
      musicBrainz,
      limit,
      offset,
      if (includeSet.isNotEmpty()) includeSet.join() else null,
      typeSet?.join(),
      statusSet?.join()
    )
  }
}
