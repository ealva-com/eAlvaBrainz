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

import com.ealva.ealvabrainz.brainz.MusicBrainz
import com.ealva.ealvabrainz.brainz.data.AreaMbid
import com.ealva.ealvabrainz.brainz.data.Artist
import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.brainz.data.BrowseReleaseList
import com.ealva.ealvabrainz.brainz.data.Include
import com.ealva.ealvabrainz.brainz.data.LabelMbid
import com.ealva.ealvabrainz.brainz.data.RecordingMbid
import com.ealva.ealvabrainz.brainz.data.Relationships
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupMbid
import com.ealva.ealvabrainz.brainz.data.TrackMbid
import com.ealva.ealvabrainz.brainz.data.join
import retrofit2.Response

/**
 * Builds the release browsing call based on [BrowseOn] type, [include], [relationships], [types],
 * and [status]
 */
public interface ReleaseBrowse {
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
    ): Response<BrowseReleaseList>

    public class Area(private val areaMbid: AreaMbid) : BrowseOn() {
      override suspend fun execute(
        musicBrainz: MusicBrainz,
        limit: Int?,
        offset: Int?,
        includeSet: String?,
        typeSet: String?,
        statusSet: String?
      ): Response<BrowseReleaseList> {
        return musicBrainz.browseReleases(
          areaId = areaMbid.value,
          limit = limit,
          offset = offset,
          include = includeSet,
          type = typeSet,
          status = statusSet
        )
      }
    }

    public class Artist(private val artistMbid: ArtistMbid) : BrowseOn() {
      override suspend fun execute(
        musicBrainz: MusicBrainz,
        limit: Int?,
        offset: Int?,
        includeSet: String?,
        typeSet: String?,
        statusSet: String?
      ): Response<BrowseReleaseList> {
        return musicBrainz.browseReleases(
          artistId = artistMbid.value,
          limit = limit,
          offset = offset,
          include = includeSet,
          type = typeSet,
          status = statusSet
        )
      }
    }

    public class Label(private val labelMbid: LabelMbid) : BrowseOn() {
      override suspend fun execute(
        musicBrainz: MusicBrainz,
        limit: Int?,
        offset: Int?,
        includeSet: String?,
        typeSet: String?,
        statusSet: String?
      ): Response<BrowseReleaseList> {
        return musicBrainz.browseReleases(
          labelId = labelMbid.value,
          limit = limit,
          offset = offset,
          include = includeSet,
          type = typeSet,
          status = statusSet
        )
      }
    }

    public class Track(private val trackMbid: TrackMbid) : BrowseOn() {
      override suspend fun execute(
        musicBrainz: MusicBrainz,
        limit: Int?,
        offset: Int?,
        includeSet: String?,
        typeSet: String?,
        statusSet: String?
      ): Response<BrowseReleaseList> {
        return musicBrainz.browseReleases(
          trackId = trackMbid.value,
          limit = limit,
          offset = offset,
          include = includeSet,
          type = typeSet,
          status = statusSet
        )
      }
    }

    public class TrackArtist(private val artistMbid: ArtistMbid) : BrowseOn() {
      override suspend fun execute(
        musicBrainz: MusicBrainz,
        limit: Int?,
        offset: Int?,
        includeSet: String?,
        typeSet: String?,
        statusSet: String?
      ): Response<BrowseReleaseList> {
        return musicBrainz.browseReleases(
          trackArtistId = artistMbid.value,
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
      ): Response<BrowseReleaseList> {
        return musicBrainz.browseReleases(
          recordingId = recordingMbid.value,
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
      ): Response<BrowseReleaseList> {
        return musicBrainz.browseReleases(
          releaseGroupId = releaseGroupMbid.value,
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

internal class ReleaseBrowseOp(
  private val browseOn: ReleaseBrowse.BrowseOn
) : ReleaseBrowse {
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
  ): Response<BrowseReleaseList> {
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
