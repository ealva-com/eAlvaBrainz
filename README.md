eAlvaBrainz
===========
Kotlin [MusicBrainz][brainz]/[CoverArtArchive][coverart] [Retrofit][retrofit] libraries for Android

**Currently in very preliminary state**. The interfaces are rapidly evolving in an attempt to
combine ease of use and designing to avoid runtime errors. The current direction is to provide
configuring lambdas in various calls and soon to build a query builder interface as the underlying
MusicBrainz calls can be somewhat complex (very large Lucene query interface). There is an escape
hatch of sorts in that the client can indirectly call the Retrofit interfaces via the
MusicBrainzService interface and have correct dispatching and error handling behavior. This is the
same method used internally. See these MusicBrainzService functions and interface:
```kotlin
suspend fun lookupArtist(
  artistMbid: ArtistMbid,
  lookup: ArtistLookup.() -> Unit = {}
): BrainzResult<Artist>

suspend fun <T : Any> brainz(block: BrainzCall<T>): BrainzResult<T>

interface ArtistLookup {
  /** Specify if any other entities and data related to the entities should be included */
  fun subquery(vararg subquery: Artist.Subquery)
  /** Specify other miscellaneous data to be included */
  fun misc(vararg misc: Artist.Misc)
  /** Included all miscellaneous data */
  fun allMisc()
  /** Include relationships based on subquery */
  fun relationships(vararg rels: Artist.Relations)
  /**
   * If [subquery] includes Artist.Subquery.Releases or Artist.Subquery.Releases the
   * Release.Type can be specified to further narrow results
   */
  fun types(vararg types: Release.Type)

  /**
   * If [subquery] includes Artist.Subquery.Releases a Release.Status can be specified to
   * further narrow results
   */
  fun status(vararg status: Release.Status)
}
```
This style provides type safety and attempts to constrain choices to a valid set of options. The
bare MusicBrainz retrofit client requires an enormous amount of string building in many situations.
The current interface is very inconsistent in this regard as refactoring is underway.

This repository consists of 3 parts:
  * **ealvabrainz** - A library which consists of 2 Retrofit interfaces, MusicBrainz and CoverArt, 
  and supporting data classes to generate a MusicBrainz REST client.
  * **ealvabrainz-service** - Higher-level abstractions that wrap the Retrofit clients with a 
  richer interface, configures necessary Retrofit/OkHttp clients, provides support for cache 
  control/throttling/user agent/etc, and dispatches calls on background threads using main-safe
  suspend functions.
  * **app** - Demonstrates search and lookup
  
As of now things are very preliminary, a small portion of the MusicBrainz API is supported,
and only SNAPSHOT libraries are being published. **Pull requests welcome.** 

For the latest SNAPSHOT check [here][ealvabrainz-snapshot] and [here][ealvabrainz-snapshot-service]
  
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
  suspend fun getArtwork(
    @Path("entity") entity: String,
    @Path("mbid") mbid: String
  ): Response<CoverArtRelease>
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

  suspend fun getReleaseArt(mbid: ReleaseMbid): CoverArtResult

  suspend fun getReleaseGroupArt(mbid: ReleaseGroupMbid): CoverArtResult

  val resourceFetcher: ResourceFetcher

  companion object {
    /**
     * Instantiate a CoverArtService implementation which handles MusicBrainz server requirements
     * such as a required User-Agent format, throttling requests, and factories/adapters to support
     * the returned data classes.
     */
    operator fun invoke(
      ctx: Context,
      appName: String,
      appVersion: String,
      contactEmail: String,
      resourceFetcher: ResourceFetcher,
      dispatcher: CoroutineDispatcher = Dispatchers.IO
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
  /**
   * Find a ReleaseList based on [artist] and [album] (album = release), limiting
   * the results to [limit], starting at [offset]. The [limit] and [offset] facilitate paging
   * through results
   */
  suspend fun findRelease(
    artist: ArtistName,
    album: AlbumName,
    limit: Int? = null,
    offset: Int? = null
  ): BrainzResult<ReleaseList>

  /**
   * Find the Release identified by [mbid]. Use [include], [type], and/or [status] to specify
   * information to be included in the Release.
   */
  suspend fun lookupRelease(
    mbid: ReleaseMbid,
    include: List<Release.Lookup>? = null,
    type: List<Release.Type>? = null,
    status: List<Release.Status>? = null
  ): BrainzResult<Release>

  fun getReleaseArt(
    artist: ArtistName,
    album: AlbumName,
    maxReleases: Int = DEFAULT_MAX_RELEASE_COUNT
  ): Flow<RemoteImage>

  /**
   * A main-safe function that calls the [block] function, with MusicBrainz as a receiver,
   * dispatched by the contained CoroutineDispatcher (typically Dispatchers.IO when not under
   * test)
   *
   * Usually [block] is a lambda which makes a direct call to the MusicBrainz Retrofit client. The
   * [block] is responsible for building the necessary String parameters, "query" in case of a
   * find call and "inc" include if doing a lookup. Use the Subquery and Misc defined
   * in the various entity objects for doing a lookup and use SearchField to build queries
   *
   * @return an Ok with value of type [T] or, if response is not successful, an Err. An Err
   * will be a BrainzMessage of type:
   * * BrainzExceptionMessage if an underlying exception is thrown
   * * BrainzNullReturn subclass of BrainzStatusMessage, if the response is OK but null
   * * BrainzErrorCodeMessage subclass of BrainzStatusMessage, if the response is not successful
   */
  suspend fun <T : Any> brainz(block: BrainzCall<T>): BrainzResult<T>

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

MusicBrainzService functions return a Result<T, BrainzMessage>. Result<V, E> is a monad for 
modelling success (Ok) or failure (Err) operations. When Result is of type Ok, the 
value of type T is the result of the call to MusicBrainz. If an Err is returned, the error is a
subtype of BrainzMessage which indicates the type of error. Result is from the 
kotlin-result[kotlin-result] library and provides a nice implementation for 
[Railway Oriented Programming][railway].
## app
The application demonstrates searching, browsing, and display of various MusicBrainz entities.
Currently the user needs to know how to build a MusicBrainz 
[query](https://musicbrainz.org/doc/Development/XML_Web_Service/Version_2/Search).

Given Kotlin coroutines, flows, and lifecycle scope, it is easy to define flows that properly
set and remove listeners based on component lifecycle, to conflate events, and to possibly emit
richer objects than provided by underlying Views. Consumers only need to collect from a flow and 
the underlying listener is properly registered/unregistered based on lifecycle.

The app uses no layout XML and instead uses a Kotlin DSL from the [Splitties][splitties] library.
The UI is defined in a way so as to segregate UI functionality and, as a result, classes such as
Activities, Fragments, ViewHolders, etc. are very small. Using this DSL keeps the UI definition
and implementation together in a single file/class, is inherently type safe/null safe, eliminates 
the need for findViewById or view binding, eliminates reflection used during inflation, and greatly 
reduces development friction (1 language/1 class vs 2 languages/multiple files). 

It's expected the app will be ported to Compose some time in the future.
  
Of Note
=======
This library contains classes others may find useful in a different context. While not necessarily canonical, these
may be used as examples or a starting point:
* Moshi annotated data classes for codegen and json adapter generation
* Moshi combination of data class style, annotations, and adapters to support the Null Object Pattern 
* Moshi annotation and adapter to support a fallback strategy for items missing from json (part of Null Object pattern)
* Moshi adapter that peeks names to determine which subtype to instantiate
* Retrofit interfaces defined with suspend or returning a flow 
* Retrofit, OkHttp, and Moshi builders to fully support the Rest client
* Sealed class MusicBrainzResult from MusicBrainzService as opposed to exceptions
* Coroutine test strategy with a JUnit rule and a test dispatcher (test concurrent code)
* App uses Kotlin Views DSL for UI ([Splitties][splitties]) - no XML layout files 
* App defines some event callback flows to automate listener register/unregister based on lifecycle resulting in less client boilerplate

Related
=======
* [MusicBrainz][brainz]
* [CoverArtArchive][coverart]
* [Retrofit][retrofit]
* [Moshi][moshi]
* [OkHttp][okhttp]
* [Kotlin coroutines][coroutines] and [flows][flow]
* [Splitties][splitties]
  
[brainz]: https://musicbrainz.org/
[coverart]: https://musicbrainz.org/doc/Cover_Art_Archive
[retrofit]: https://github.com/square/retrofit
[moshi]: https://github.com/square/moshi
[okhttp]: https://github.com/square/okhttp/
[coroutines]: https://kotlinlang.org/docs/reference/coroutines-overview.html
[flow]: https://kotlinlang.org/docs/reference/coroutines/flow.html  
[splitties]: https://github.com/LouisCAD/Splitties
[kotlin-result]: https://github.com/michaelbull/kotlin-result
[railway]: https://fsharpforfunandprofit.com/rop/
[ealvabrainz-snapshot]: https://oss.sonatype.org/content/repositories/snapshots/com/ealva/ealvabrainz-service/
[ealvabrainz-service-snapshot]: https://oss.sonatype.org/content/repositories/snapshots/com/ealva/ealvabrainz-service/
