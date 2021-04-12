eAlvaBrainz
===========
Kotlin [MusicBrainz][brainz]/[CoverArtArchive][coverart] [Retrofit][retrofit] libraries for Android

**Currently in an beta state, mostly stable API**.

A few small examples to start:

```kotlin
// Get the artist represented by the ArtistMbid and include all Misc info
brainzSvc.lookupArtist(mbid) { include(*Artist.Include.values()) }
  .onSuccess { artist -> handleArtist(artist, mbid) }
  .onFailure { brainzMsg -> displayError(brainzMsg.asString(resourceFetcher)) }

// Get the artist Nirvana's info and include aliases
val nirvana = ArtistMbid("5b11f4ce-a62d-471e-81fc-a69a8278c7da") // maybe obtained via find
lookupArtist(nirvana) { misc(Artist.Misc.Aliases) }
  .onSuccess {}
  .onFailure {}

// Find releases for the artist name and release title
val jethroTull = ArtistName("Jethro Tull")
val aqualung = AlbumTitle("Aqualung")
findRelease(Limit(4)) { artist(jethroTull) and release(aqualung) }

// Browse events for the given artist and limit the results to 15
val metallicaMbid = ArtistMbid("65f4f0c5-ef9e-490c-aee3-909e7ae6b2ab") // maybe obtained via find
val limit = Limit(15)
browseEvents(EventBrowse.BrowseOn.Artist(metallicaMbid), limit)

// Browse Releases by an artist and limit the results to official, album releases (no bootlegs or
// promos and no singles, compilations, etc)
val theBeatles = ArtistMbid("b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d") // maybe obtained via find
browseReleases(ReleaseBrowse.BrowseOn.Artist(theBeatles)) {
  types(ReleaseGroup.Type.Album)
  status(Release.Status.Official)
}.onSuccess { browseReleaseList ->
  // handle browse
}.onFailure {
  // handle error path
}

// Find all ReleaseGroups by The Beatles whose first release date was between 1967 and 1969
// inclusively, where the release was an album, but not a compilation or interview, and was an
// official release (not bootleg or promotion)
findReleaseGroup {
  artist(ArtistName("The Beatles")) and
    firstReleaseDate { Year("1967") inclusive Year("1969") } and
    primaryType(ReleaseGroup.Type.Album) and
    !secondaryType { ReleaseGroup.Type.Compilation or ReleaseGroup.Type.Interview } and
    status(Release.Status.Official)
}.onSuccess { releaseGroupList ->
  // handle group list
}.onFailure {
  // handle error path
}
```

# Design philosophy
The design philosophy is to provide a type safe interface to the MusicBrainz server, dispatching
work on a background thread using coroutine dispatchers, providing many of the requirements for
well-behaved clients (rate limiting, user agent, etc), and converting responses to easily handled
results. Given the complexity of a typical call path regarding the litany of possible errors with
calling remote servers, parsing Json, etc., special care is given to returned values. A Result
monad style was chosen to make sunny day and error path code straightforward and to avoid throwing
exceptions across coroutine boundaries. This is not a functional library, but the style of
[Railway Oriented Programming][railway] fits very nicely with handling the various result
possibilities.

For lookup and browse functions, a call specific lambda receiver is provided to guide the client
with regard to what options are available without needing to know the underlying details. Find
functions provide call specific lambda receivers which are the base of a relatively simple, but
extensive, DSL for building a lucene query. This style provides type safety and attempts to
constrain choices to a valid set of options. Using Android Studio's basic or smart completion inside
one of the lambda receivers will display the possibilities for building the lookup, browse, or find
in scope. Using the bare MusicBrainz retrofit client would require extensive string building which
can be error-prone and lacks type support. There are quite a few value (inline) classes to provide
type support without generating extra garbage.

The current version covers the majority of the MusicBrainz API. There is an escape hatch of
sorts in that the client can indirectly call the Retrofit interfaces via the MusicBrainzService
interface and have correct dispatching and error handling behavior. This is the same method used
internally. There are currently no write capabilities (can't set ratings or create collections),

This is a Kotlin library and not much thought was given to possible Java clients. Input and pull
requests are welcome.

This repository consists of 3 parts:
  * **ealvabrainz** - A library which consists of 2 Retrofit interfaces, MusicBrainz and CoverArt,
    and supporting data classes to generate a MusicBrainz REST client.
  * **ealvabrainz-service** - Higher-level abstractions that wrap the Retrofit clients with a
    richer interface, configures necessary Retrofit/OkHttp clients, provides support for cache
    control/throttling/user agent/authentication/etc, and dispatches calls on background threads 
    using main-safe suspend functions.
  * **app** - Demonstrates search and lookup
  
Check [here][maven-ealvabrainz] and [here][maven-ealvabrainz-service] for the latest published
releases. **Pull requests welcome.** 

For the latest SNAPSHOT check [here][ealvabrainz-snapshot] and [here][ealvabrainz-service-snapshot]
  
# Libraries
## ealvabrainz
Provides MusicBrainz and CoverArt interfaces which Retrofit.Builder can use to generate a REST 
client for the MusicBrainz and CoverArtArchive servers. This module contains the bulk of the code
to build the requests and decode the responses into objects. 

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

@JvmInline
value class AreaMbid(override val value: String) : Mbid

inline val Area.mbid
  get() = AreaMbid(id)
```
A companion object is defined which contains the Null Object and a mapping between the class name 
and the fallback NullArea object. An extension function defines a Boolean isNullObject val. Also 
note the AreaMbid value class. Since a MusicBrainz identifier (MBID) is just a string, these inline 
classes are meant to differentiate types of MBID to facilitate compile time type checking.

The MusicBrainz and CoverArt interfaces are defined with suspend functions, so are only callable
from a coroutine. It is expected that the service module will be used to handle constructing and
calling the generated Retrofit classes.
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

  companion object {
    /**
     * Instantiate a CoverArtService implementation which handles MusicBrainz server requirements
     * such as a required User-Agent format, throttling requests, and factories/adapters to support
     * converting Json to objects.
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

fun Flow<ReleaseMbid>.transform(service: CoverArtService): Flow<CoverArtImageInfo>

fun Flow<ReleaseGropMbid>.transform(service: CoverArtService): Flow<CoverArtImageInfo>
```
#### MusicBrainzService
This service is similar to CoverArtService in that it provides a higher-level abstraction and builds
the appropriate underlying Retrofit/OkHttp classes. MusicBrainzService has functions that take type 
specific parameters and format these into parameters for the underlying calls to the MusicBrainz
Retrofit client. There is also a generic ```brainz()``` function accepting a lambda which allows
direct calls to the MusicBrainz Retrofit client while providing correct coroutine dispatch and
simplifying error handling.

The MusicBrainz server API is extensively supported. Below are 3 examples of a lookup, a browse,
a find (query), and brainz function which underlies all calls to the server.
```kotlin
typealias BrainzCall<T> = suspend MusicBrainz.() -> Response<T>
typealias BrainzResult<T> = Result<T, BrainzMessage>

interface MusicBrainzService {
  /**
   * Find the Artist with the mbid ID. Provide an optional lambda with an ArtistLookup
   * receiver to specify if any other information should be included.
   */
  suspend fun lookupArtist(
    mbid: ArtistMbid,
    lookup: ArtistLookup.() -> Unit = {}
  ): BrainzResult<Artist>

  /**
   * Browse the recordings of the entity specified by [browseOn] (eg. Artist, Collection, Release,
   * or Work). Use [limit] and [offset] to page through the results. Provide an optional lambda with
   * a RecordingBrowse receiver to specify if other information should be included, such as
   * Artist Credits or some other relationships. BrowseRecordingList contains the total
   * number of Recordings, the offset returned, and a list of Recording objects.
   */
  suspend fun browseRecordings(
    browseOn: RecordingBrowse.BrowseOn,
    limit: Limit? = null,
    offset: Offset? = null,
    browse: RecordingBrowse.() -> Unit = {}
  ): BrainzResult<BrowseRecordingList>


  suspend fun findRelease(
    limit: Limit? = null,
    offset: Offset? = null,
    search: ReleaseSearch.() -> Unit
  ): BrainzResult<ReleaseList>

  // Calls the [block]
  suspend fun <T : Any> brainz(
    block: suspend MusicBrainz.() -> Response<T>
  ): Result<T, BrainzMessage>
}
```
The MusicBrainzService is constructed with a CoverArtService instance. This allows 
MusicBrainzService to provide functionality such as:
``` kotlin
suspend fun getReleaseGroupArtwork(mbid: ReleaseGroupMbid): Uri
```

A small find release group example showing the query DSL:
```kotlin
val result = findReleaseGroup {
  artist(LED_ZEPPELIN) and releaseGroup(HOUSES_OF_THE_HOLY)
}
```
The ReleaseGroupSearch supports all 17 possible query fields and the term DSL support: required,
prohibited, regular expressions, ranges, fuzzy search, proximity, and boosting. See the MusicBrainz
docs for details.
```kotlin
val revolver = Field("album", Term("Revolver"))
val rubberSoul = Field("album", Term("Rubber Soul"))
val beatles = Field("artist", +Term("The Beatles"))  // + operator indicated required term
val exp = beatles and (revolver or rubberSoul)
val exp2 = beatles and revolver or rubberSoul
```

Most MusicBrainzService functions return a Result<T, BrainzMessage>. Result<V, E> is a monad for 
modelling success (Ok) or failure (Err) operations. When Result is of type Ok, the 
value of type T is the result of the call to MusicBrainz. If an Err is returned, the error is a
subtype of BrainzMessage which indicates the type of error. Result is from the 
[kotlin-result] library and provides a nice implementation for
[Railway Oriented Programming][railway].

### Building and Executing Integration Tests and App
Several items must be defined in the local.properties file in the root folder of this project to
successfully build and run the app and integration tests. If the file doesn't exist, create it and
add the following (substituting your valid information):
```text
BRAINZ_APP_NAME="YourAppName"
BRAINZ_APP_VERSION="0.0.1"
BRAINZ_CONTACT_EMAIL="your@email.com"
```
The fields will be combined into a user agent passed to the servers.

One or more integration tests require authentication with the MusicBrainz server. The required
username and password must be defined in the local.properties file in the root folder of this
project. This file is not committed to version control as it's contents are private (obviously).
It would typically look something like:
```text
BRAINZ_USERNAME="my_username"
BRAINZ_PASSWORD="my_password"
```
where BRAINZ_USERNAME and BRAINZ_PASSWORD are set to your MusicBrainz.org credentials. If you don't
have an account, go to https://musicbrainz.org/register and create one. Not mandatory, however not 
setting these values correctly will result in some tests failing due to authentication errors.

Applications which call functions requiring authentication must implement the CredentialsProvider
interface and use it when constructing a MusicBrainzService implementation.

## app
The application demonstrates searching, browsing, and display of various MusicBrainz entities. Only
a few APIs are called - look at the integration tests for more examples.

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
This library contains classes others may find useful in a different context. While not necessarily
canonical, these may be used as examples or a starting point:
* Moshi annotated data classes for codegen and json adapter generation
* Moshi combination of data class style, annotations, and adapters to support the Null Object
  Pattern 
* Moshi annotation and adapter to support a fallback strategy for items missing from json
  (part of Null Object pattern)
* Moshi adapter that peeks names to determine which subtype to instantiate in relationships
* Retrofit interfaces defined with suspend
* Retrofit, OkHttp, and Moshi builders to fully support the Rest client
* Sealed class Result monad return from service calls avoiding exceptions across coroutine
  boundaries and providing transform/mapping/chaining style of result handling.
* Coroutine test strategy with a JUnit rule and a test dispatcher (test concurrent code)
* App uses Kotlin Views DSL for UI ([Splitties][splitties]) - no XML layout files 
* App defines some event callback flows to automate listener register/unregister based on lifecycle
  resulting in less client boilerplate

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
[maven-ealvabrainz]: https://search.maven.org/search?q=g:com.ealva%20AND%20a:ealvabrainz
[maven-ealvabrainz-service] https://search.maven.org/search?q=g:com.ealva%20AND%20a:ealvabrainz-service
[ealvabrainz-snapshot]: https://oss.sonatype.org/content/repositories/snapshots/com/ealva/ealvabrainz-service/
[ealvabrainz-service-snapshot]: https://oss.sonatype.org/content/repositories/snapshots/com/ealva/ealvabrainz-service/
