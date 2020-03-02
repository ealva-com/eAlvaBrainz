/*
 * Copyright (c) 2020  Eric A. Snell
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

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.ealva.brainzsvc.art.RemoteImage
import com.ealva.brainzsvc.art.RemoteImageData
import com.ealva.brainzsvc.art.SizeBucket
import com.ealva.brainzsvc.service.CoverArtService.Entity.ReleaseEntity
import com.ealva.brainzsvc.service.CoverArtService.Entity.ReleaseGroupEntity
import com.ealva.ealvabrainz.brainz.CoverArt
import com.ealva.ealvabrainz.brainz.data.CoverArtImage
import com.ealva.ealvabrainz.brainz.data.CoverArtImageType
import com.ealva.ealvabrainz.brainz.data.CoverArtRelease
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupMbid
import com.ealva.ealvabrainz.brainz.data.ReleaseMbid
import com.ealva.ealvabrainz.brainz.data.imageTypes
import com.ealva.ealvabrainz.brainz.data.theLarge
import com.ealva.ealvabrainz.brainz.data.theSmall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import timber.log.Timber
import java.io.File

/**
 * Transform operator converts ReleaseMbid to a RemoteImage which points to the CoverArtImage
 * Uri and contains other information about the image
 */
@JvmName("transformReleases")
fun Flow<ReleaseMbid>.transform(service: CoverArtService): Flow<RemoteImage> = flow {
  collect { mbid ->
    doTransform(service, mbid.value, ReleaseEntity)
  }
}

/**
 * Transform operator converts ReleaseGroupMbid to a RemoteImage which points to the CoverArtImage
 * Uri and contains other information about the image
 */
@JvmName("transformGroups")
fun Flow<ReleaseGroupMbid>.transform(service: CoverArtService): Flow<RemoteImage> = flow {
  collect { mbid ->
    doTransform(service, mbid.value, ReleaseGroupEntity)
  }
}

/**
 * CoverArtService is a wrapper around a Retrofit CoverArt instance that provides higher level
 * functionality and some extra type safety.
 */
interface CoverArtService {
  enum class Entity(val value: String) {
    ReleaseEntity("release"),
    ReleaseGroupEntity("release-group")
  }

  /**
   * If Retrofit call is successful returns the result, else a MusicBrainzException is thrown.
   * This call does not return a MusicBrainzResult as an incorrect [mbid] should not occur as it
   * should come directly from MusicBrainz. A null return means the artwork didn't exist. An
   * exception being thrown should truly be exceptional, such as an IOException.
   * @param entity either [ReleaseEntity] or [ReleaseGroupEntity]
   * @param mbid a MusicBrainz ID (MBID)
   * @return a [CoverArtRelease] if successful or null if the [entity]-[mbid] pair weren't found
   * @exception MusicBrainzException thrown if the underlying Retrofit service throws
   */
  suspend fun getCoverArtRelease(entity: Entity, mbid: String): CoverArtRelease?

  companion object {
    /**
     * Instantiate a CoverArtService implementation which handles MusicBrainz server requirements
     * such as a required User-Agent format, throttling requests, and factories/adapters to support
     * the returned data classes.
     */
    fun make(
      ctx: Context,
      appName: String,
      appVersion: String,
      contactEmail: String
    ): CoverArtService =
      make(
        buildCoverArt(appName, appVersion, contactEmail, File(ctx.cacheDir, CACHE_DIR)),
        Dispatchers.IO
      )

    /**
     * For test, providing for injection of Mocks and test dispatcher.
     */
    internal fun make(coverArt: CoverArt, dispatcher: CoroutineDispatcher): CoverArtService =
      CoverArtServiceImpl(coverArt, dispatcher)
  }
}

//private const val COVER_ART_API_URL = "http://coverartarchive.org/"
private const val COVER_ART_API_SECURE_URL = "https://coverartarchive.org/"

private val SERVICE_NAME = CoverArtServiceImpl::class.java.simpleName
private const val CACHE_DIR = "CoverArtArchive"
private val INTENT_BRAINZ = Intent(Intent.ACTION_VIEW, Uri.parse("https://musicbrainz.org/"))

private class CoverArtServiceImpl(
  private val coverArt: CoverArt,
  private val dispatcher: CoroutineDispatcher
) : CoverArtService {

  override suspend fun getCoverArtRelease(
    entity: CoverArtService.Entity,
    mbid: String
  ): CoverArtRelease? = withContext(dispatcher) {
    try {
      coverArt.getArtwork(entity.value, mbid).run {
        if (isSuccessful) body() else null
      }
    } catch (e: Exception) {
      throw MusicBrainzException("Unexpected error: '${e.message.orEmpty()}'", e)
    }
  }
}

private fun buildCoverArt(
  appName: String,
  appVersion: String,
  contactEmail: String,
  cacheDirectory: File
) = Retrofit.Builder()
  .client(makeOkHttpClient(SERVICE_NAME, appName, appVersion, contactEmail, cacheDirectory))
  .baseUrl(COVER_ART_API_SECURE_URL)
  .addMoshiConverterFactory()
  .build()
  .create(CoverArt::class.java)

private suspend fun FlowCollector<RemoteImage>.doTransform(
  service: CoverArtService,
  mbidString: String,
  entity: CoverArtService.Entity
) {
  try {
    service.getCoverArtRelease(entity, mbidString)?.firstImageOrNull()?.run {
      val addedOriginal = emitImage(image, SizeBucket.ORIGINAL)
      thumbnails.run {
        if (!emitImage(theLarge, SizeBucket.ORIGINAL)) emitImage(
          theSmall,
          SizeBucket.MEDIUM
        )
        if (!addedOriginal) emitImage(size1200, SizeBucket.EXTRA_LARGE)
      }
    }
  } catch (e: Exception) {
    Timber.e(e, "Transform %s=%s", entity, mbidString)
  }
}

private fun CoverArtRelease.firstImageOrNull(): CoverArtImage? = images
  .asSequence()
  .map { image -> Pair(image, image.imageTypes.firstOrNull()) }
  .filterNot { pair -> pair.second == null }
  .sortedBy { pair ->
    when (pair.second) {
      CoverArtImageType.TYPE_FRONT -> 0
      CoverArtImageType.TYPE_BACK -> 40
      CoverArtImageType.TYPE_BOOKLET -> 10
      CoverArtImageType.TYPE_MEDIUM -> 30
      CoverArtImageType.TYPE_TRAY -> 60
      CoverArtImageType.TYPE_OBI -> 80
      CoverArtImageType.TYPE_SPINE -> 90
      CoverArtImageType.TYPE_TRACK -> 70
      CoverArtImageType.TYPE_LINER -> 10
      CoverArtImageType.TYPE_STICKER -> 50
      CoverArtImageType.TYPE_POSTER -> 20
      CoverArtImageType.TYPE_WATERMARK -> 100
      CoverArtImageType.TYPE_OTHER -> 60
      else -> Int.MAX_VALUE
    }
  }
  .firstOrNull()?.first

private suspend fun FlowCollector<RemoteImageData>.emitImage(image: String, bucket: SizeBucket) =
  if (image.isNotEmpty()) {
    emit(RemoteImageData.fromUrl(image, bucket, R.drawable.ic_musicbrainz_logo, INTENT_BRAINZ))
    true
  } else false


