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

import com.ealva.brainzsvc.net.RawResponse
import com.ealva.brainzsvc.net.toRawResponse
import com.ealva.brainzsvc.service.BrainzMessage.BrainzExceptionMessage
import com.ealva.brainzsvc.service.BrainzMessage.BrainzStatusMessage.BrainzErrorCodeMessage
import com.ealva.brainzsvc.service.BrainzMessage.BrainzStatusMessage.BrainzNullReturn
import com.ealva.ealvabrainz.brainz.data.BrainzError
import com.ealva.ealvabrainz.brainz.data.theMoshi
import com.ealva.ealvabrainz.log.BrainzLog
import com.ealva.ealvalog.e
import com.ealva.ealvalog.invoke
import com.ealva.ealvalog.lazyLogger
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.getError
import com.squareup.moshi.JsonAdapter
import retrofit2.Response
import java.io.PrintWriter
import java.io.StringWriter

private val LOG by lazyLogger(BrainzLog.BRAINZ_ERROR_TAG, BrainzLog.marker)

public sealed class BrainzMessage {
  public abstract fun asString(fetcher: ResourceFetcher): String

  public sealed class BrainzStatusMessage(public val statusCode: Int) : BrainzMessage() {
    override fun asString(fetcher: ResourceFetcher): String =
      fetcher.fetch(R.string.ResultStatusCode, statusCode)

    public class BrainzNullReturn(statusCode: Int) : BrainzMessage.BrainzStatusMessage(statusCode) {
      init {
        if (BrainzLog.logBrainzErrors) LOG.e { it("Null return status code=%d", statusCode) }
      }
    }

    public class BrainzErrorCodeMessage(
      statusCode: Int,
      private val response: Response<*>
    ) : BrainzMessage.BrainzStatusMessage(statusCode) {
      init {
        if (BrainzLog.logBrainzErrors)
          LOG.e { it("Error code message. Status code=%d", statusCode) }
      }

      @Suppress("unused")
      public val rawResponse: RawResponse
        get() = response.toRawResponse()
    }
  }

  @Suppress("MemberVisibilityCanBePrivate")
  public class BrainzExceptionMessage(public val ex: Throwable) : BrainzMessage() {
    init {
      if (BrainzLog.logBrainzErrors) LOG.e { it("Brainz exception message. %s", ex) }
    }

    override fun asString(fetcher: ResourceFetcher): String = ex.message ?: ex.toString()

    @Suppress("unused")
    public fun stackTraceToString(throwable: Throwable): String {
      return StringWriter().apply {
        PrintWriter(this).also { pw ->
          throwable.printStackTrace(pw)
        }
      }.toString()
    }
  }
}

public fun <V> Result<V, BrainzMessage>.getErrorString(
  fetcher: ResourceFetcher
): String = when (this) {
  is Ok -> "Not an Err"
  is Err -> getError()?.asString(fetcher) ?: "No BrainzMessage Error"
}

public class BrainzErrorMessage(public val error: BrainzError) : BrainzMessage() {
  init {
    if (BrainzLog.logBrainzErrors) LOG.e { it("BrainzError=%s", error) }
  }

  override fun asString(fetcher: ResourceFetcher): String = error.toString()
}

public fun <V : Response<U>, U> Result<V, BrainzMessage>.mapResponse(): Result<U, BrainzMessage> =
  when (this) {
    is Ok -> handleResponse()
    is Err -> this
  }

private fun <U, V : Response<U>> Ok<V>.handleResponse(): Result<U, BrainzMessage> = try {
  when {
//    value == null -> Err(BrainzExceptionMessage(BrainzException("Null Response returned")))
    value.isSuccessful -> value.body()?.let { Ok(it) } ?: Err(BrainzNullReturn(value.code()))
    else -> value.toErrResult()
  }
} catch (e: Exception) {
  Err(BrainzExceptionMessage(e))
}

private fun <T, U> Response<T>.toErrResult(): Result<U, BrainzMessage> =
  Err(errorBody()?.string()?.makeBrainzErrorMessage() ?: BrainzErrorCodeMessage(code(), this))

private fun String.makeBrainzErrorMessage(): BrainzErrorMessage? = try {
  when {
    startsWith("<!DOCTYPE HTML") -> htmlToBrainzError() // Coverart returns HTML error response
    else -> errorAdapter.fromJson(this)
  }?.toBrainzErrorMessage()
} catch (e: Exception) {
  null
}

private val errorAdapter: JsonAdapter<BrainzError> by lazy {
  theMoshi.adapter(BrainzError::class.java)
}

private fun String.htmlToBrainzError(): BrainzError? {
  @Suppress("MagicNumber")
  // <p>No cover art found for release group adf7dc8b-97fb-45fa-9597-f7ec2a8002de</p>
  val errorMessage = substring((indexOf("<p>") + 3).coerceAtMost(length), indexOf("</p>"))
  return if (errorMessage.isNotEmpty()) {
    BrainzError(errorMessage)
  } else null
}

private fun BrainzError.toBrainzErrorMessage(): BrainzErrorMessage = BrainzErrorMessage(this)
