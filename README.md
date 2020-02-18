eAlvaBrainz
===========
Kotlin [MusicBrainz][brainz]/[CoverArtArchive][coverart] [Retrofit][retrofit] libraries for Android

**Currently in very preliminary state**. Pushing unfinished to Github in case someone can use the 
preliminary work

This repository consists of 3 parts:
  * **ealvabrainz** - A library which consists of 2 Retrofit interfaces, MusicBrainz and CoverArt, 
  and supporting data classes to generate a MusicBrainz REST client.
  * **ealvabrainz-service** - Higher-level abstractions that wrap the Retrofit clients with a 
  richer interface, configures necessary Retrofit/OkHttp clients, and provides support for cache 
  control/throttling/user agent/etc.
  * **app** - Demonstrates search and lookup
  
As of now things are very preliminary, a very small portion of the MusicBrainz API is supported,
and no libraries are being published. **Pull requests welcome.** 
  
# Libraries
## ealvabrainz
Provides MusicBrainz and CoverArt interfaces which Retrofit.Builder can use to generate a REST 
client for the MusicBrainz and CoverArtArchive servers. 

The data classes created as response to MusicBrainz requests, plus added annotations/JsonAdapters, 
are provided to support the Null Object Pattern. Null is avoided almost entirely (one specific case 
remains). Null Strings become empty Strings, null Lists become empty lists, and a null reference 
is replaced by a specific instance of the class - known as a Null Object. 

Missing objects default to the their Null Object counterparts. Checking for null is not required, 
but it is possible to check for the Null Object via instance comparison. The Area class provides a 
short example:
```kotlin
@JsonClass(generateAdapter = true)
data class Area(
  var id: String = "",
  var name: String = "",
  @field:Json(name = "sort-name") var sortName: String = "",
  var disambiguation: String = "",
  @field:Json(name = "iso-3166-1-codes") var iso31661Codes: List<String> = emptyList()
) {
  companion object {
    val NullArea = Area()
    val fallbackMapping: Pair<String, Any> = Area::class.java.name to NullArea
  }
}

inline val Area.isNullObject
  get() = this === NullArea

inline class AreaMbid(override val value: String) : Mbid

inline val Area.mbid
  get() = AreaMbid(id)
```
A companion object is defined which contains the Null Object and a mapping between the class name 
and the fallback NullArea object. An extension function defines a Boolean isNullObject val. Also 
note the AreaMbid inline class. Since a MusicBrainz identifier (MBID) is just a string, these inline 
classes are meant to differentiate types of MBID to facilitate compile time type checking.

While this module is not directly dependent upon Kotlin [coroutine][coroutines] libraries, the 
Retrofit interface functions are defined with suspend. This means clients will require Kotlin 
coroutine libraries.
```kotlin
interface CoverArt {
  /**
   * An example for looking up release artwork by mbid would be:
   * https://coverartarchive.org/release/91975b77-c9f2-46d1-a03b-f1fffbda1d1c
   *
   * @param entity either "release" or "release-group"
   * @param mbid the release or release-group mbid. In the example this would be:
   * 91975b77-c9f2-46d1-a03b-f1fffbda1d1c
   *
   * @return the CoverArtRelease associated with the mbid, wrapped in a Response
   */
  @GET("{entity}/{mbid}")
  suspend fun getArtwork(@Path("entity") entity: String, @Path("mbid") mbid: String): Response<CoverArtRelease>
}
```
Note that ```getArtwork()``` is suspending and may only be called from a coroutine. The higher level 
abstractions in ealvabrainz-service also define suspend functions along with providing [flows][flow] 
of images.    
## ealvabrainz-service
Provides CoverArtService and MusicBrainzService, which wrap the CoverArt and MusicBrainz Retrofit 
clients providing a higher-level function. 
#### CoverArtService    
The CoverArtService provides functions to retrieve artwork based on an MusicBrainz ID (MBID). It 
also has extension functions to convert flows of MBIDs to cover art images. The CoverArtService 
implementation builds and contains the necessary OkHttp client and Retrofit implementation of the 
CoverArt class.
```kotlin
interface CoverArtService {
  enum class Entity(val value: String) {
    ReleaseEntity("release"),
    ReleaseGroupEntity("release-group")
  }

  suspend fun getCoverArtRelease(entity: Entity, mbid: String): CoverArtRelease?

  companion object {
    fun make(
      context: Context,
      appName: String,
      appVersion: String,
      contactEmail: String
    ): CoverArtService
  }
}

fun Flow<ReleaseMbid>.transform(service: CoverArtService): Flow<RemoteImage>

fun Flow<ReleaseGroupMbid>.transform(service: CoverArtService): Flow<RemoteImage>
```
#### MusicBrainzService
This service is similar to CoverArtService in that it provides a higher-level abstraction and builds
the appropriate underlying Retrofit/OkHttp classes. MusicBrainzService has functions that take type 
specific parameters and format these into parameters for the underlying calls to the MusicBrainz
Retrofit client. There is also a generic ```brainz()``` function accepting a lambda which allows direct calls to the
MusicBrainz client while providing correct coroutine dispatch and simplifying error handling.
```kotlin
interface MusicBrainzService {
  suspend fun findRelease(
    artist: ArtistName,
    album: AlbumName,
    limit: Int? = null,
    offset: Int? = null
  ): MusicBrainzResult<ReleaseList>

  suspend fun lookupRelease(
    mbid: ReleaseMbid,
    include: List<Release.Lookup> = emptyList(),
    type: Release.Type = Release.Type.Any,
    status: Release.Status = Release.Status.Any
  ): MusicBrainzResult<Release>

  fun getReleaseArt(
    artist: ArtistName,
    album: AlbumName,
    maxReleases: Int = DEFAULT_MAX_RELEASE_COUNT
  ): Flow<RemoteImage>

  suspend fun <T : Any> brainz(
    block: suspend (brainz: MusicBrainz) -> Response<T>
  ): MusicBrainzResult<T>

  companion object {
    fun make(
      context: Context,
      userAgentAppName: String,
      userAgentAppVersion: String,
      userAgentEmailContact: String,
      coverArtService: CoverArtService
    ): MusicBrainzService
  }
}
```
The MusicBrainzService is constructed with a CoverArtService instance. This allows 
MusicBrainzService to provide functionality such as:
``` kotlin
fun getReleaseArt(artistName: ArtistName, albumName: AlbumName): Flow<RemoteImage>
```
which coordinates a search of releases and returns a flow of images. 

Most MusicBrainzService functions return a MusicBrainzResult sealed class based on the return type.
The implementation of the returned MusicBrainzResult indicates success, error, exception, or 
unknown.
```kotlin
sealed class MusicBrainzResult<out T : Any> {
  /** Call was successful and contains the [value] */
  data class Success<T : Any>(val value: T) : MusicBrainzResult<T>()

  /** Server returned an error and was converted to the common error body response */
  data class Error(val error: BrainzError) : MusicBrainzResult<Nothing>()

  /**
   * An error occurred and we can't grok the error body. Punt to caller
   */
  data class Unknown(val response: RawResponse) : MusicBrainzResult<Nothing>()

  /** An [exception] was thrown */
  data class Exceptional(val exception: MusicBrainzException) : MusicBrainzResult<Nothing>()
}
```
## app
TBD

Of Note
=======
This library contains classes others may find useful in a different context. While not necessarily canonical, these
may be used as examples or a starting point:
* Moshi annotated data classes for codegen and json adapter generation
* Moshi custom json adapter copied form codegen and modified to support a name possibly being of 2 types
* Moshi combination of data class style, annotations, and adapters to support the Null Object Pattern 
* Moshi annotation and adapter to support a fallback strategy for items missing from json (part of Null Object pattern)
* Moshi adapter that peeks names to determine which subtype to instantiate
* Retrofit interfaces defined with suspend or returning a flow 
* Retrofit, OkHttp, and Moshi builders to fully support the Rest client
* Coroutine test strategy with a JUnit rule and a test dispatcher (test concurrent code)
* 

Related
=======
* [MusicBrainz][brainz]
* [CoverArtArchive][coverart]
* [Retrofit][retrofit]
* [Moshi][moshi]
* [OkHttp][okhttp]
* [Kotlin coroutines][coroutines] and [flows][flow]
  
[brainz]: https://musicbrainz.org/
[coverart]: https://musicbrainz.org/doc/Cover_Art_Archive
[retrofit]: https://github.com/square/retrofit
[moshi]: https://github.com/square/moshi
[okhttp]: https://github.com/square/okhttp/
[coroutines]: https://kotlinlang.org/docs/reference/coroutines-overview.html
[flow]: https://kotlinlang.org/docs/reference/coroutines/flow.html  
