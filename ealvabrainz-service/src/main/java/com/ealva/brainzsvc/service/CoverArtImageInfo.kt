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

package com.ealva.brainzsvc.service

import android.net.Uri
import com.ealva.brainzsvc.net.toSecureUri
import com.ealva.brainzsvc.service.CoverArtImageInfo.ImageInfo
import com.ealva.ealvabrainz.brainz.data.CoverArtImage
import com.ealva.ealvabrainz.brainz.data.CoverArtImageSize
import com.ealva.ealvabrainz.brainz.data.CoverArtImageType
import com.ealva.ealvabrainz.brainz.data.CoverArtRelease
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupMbid
import com.ealva.ealvabrainz.brainz.data.ReleaseMbid
import com.ealva.ealvabrainz.brainz.data.imageTypes
import com.ealva.ealvabrainz.brainz.data.the250
import com.ealva.ealvabrainz.brainz.data.the500
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEmpty

/**
 * Transform operator converts ReleaseMbid to a RemoteImage which points to the CoverArtImage
 * Uri and contains other information about the image
 */
@JvmName("transformReleases")
public fun Flow<ReleaseMbid>.transform(service: CoverArtService): Flow<CoverArtImageInfo> = flow {
  collect { mbid ->
    when (val result = service.getReleaseArt(mbid)) {
      is Ok -> result.value.allImages.forEach { emit(it) }
      is Err -> emit(CoverArtImageInfo.NONE)
    }
  }
}.onEmpty { emit(CoverArtImageInfo.NONE) }

/**
 * Transform operator converts ReleaseGroupMbid to a RemoteImage which points to the CoverArtImage
 * Uri and contains other information about the image
 */
@JvmName("transformGroups")
public fun Flow<ReleaseGroupMbid>.transform(
  service: CoverArtService
): Flow<CoverArtImageInfo> = flow {
  collect { mbid ->
    when (val result = service.getReleaseGroupArt(mbid)) {
      is Ok -> result.value.allImages.forEach { emit(it) }
      is Err -> emit(CoverArtImageInfo.NONE)
    }
  }
}.onEmpty { emit(CoverArtImageInfo.NONE) }

public sealed class CoverArtImageInfo {
  public abstract val location: Uri
  public abstract val size: CoverArtImageSize
  public abstract val types: Set<CoverArtImageType>

  public class ImageInfo(
    override val location: Uri,
    override val size: CoverArtImageSize,
    private val coverArt: CoverArtImage
  ) : CoverArtImageInfo() {
    override val types: Set<CoverArtImageType> by lazy { coverArt.imageTypes.toSet() }
  }

  public object NONE : CoverArtImageInfo() {
    override val location: Uri = Uri.EMPTY
    override val size: CoverArtImageSize = CoverArtImageSize.Unknown
    override val types: Set<CoverArtImageType> = emptySet()
  }
}

public val CoverArtRelease.allImages: Sequence<CoverArtImageInfo>
  get() = images.asSequence().flatMap { image ->
    sequence {
      yield(ImageInfo(image.image.toSecureUri(), CoverArtImageSize.Original, image))
      yield(ImageInfo(image.thumbnails.size1200.toSecureUri(), CoverArtImageSize.Size1200, image))
      yield(ImageInfo(image.thumbnails.the500.toSecureUri(), CoverArtImageSize.Size500, image))
      yield(ImageInfo(image.thumbnails.the250.toSecureUri(), CoverArtImageSize.Size250, image))
    }.filterNot { it.location === Uri.EMPTY }
  }
