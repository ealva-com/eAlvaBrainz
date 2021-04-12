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

package com.ealva.ealvabrainz.brainz.data

import com.ealva.ealvabrainz.matchers.toBeEmpty
import com.nhaarman.expect.expect
import com.nhaarman.expect.fail
import com.squareup.moshi.Moshi
import org.junit.Before
import org.junit.Test
import com.ealva.ealvabrainz.brainz.data.toReleaseStatus as toStatus

internal class ReleaseTest {
  private lateinit var moshi: Moshi

  @Before
  fun setup() {
    moshi = theBrainzMoshi
  }

  @Test
  fun `test reify Release Status`() {
    expect("official".toStatus()).toBe(Release.Status.Official)
    expect("promotion".toStatus()).toBe(Release.Status.Promotion)
    expect("bootleg".toStatus()).toBe(Release.Status.Bootleg)
    expect("pseudo-release".toStatus()).toBe(Release.Status.PseudoRelease)

    expect("Official".toStatus()).toBe(Release.Status.Official)
    expect("BOOTLEG".toStatus()).toBe(Release.Status.Bootleg)

    expect("dummy".toStatus()).toBeInstanceOf<Release.Status.Unrecognized> { status ->
      expect("dummy".toStatus()).toBeTheSameAs(status)
    }
  }

  /**
   * Obviously non-exhaustive - test that various areas of a Release are parsed as expected.
   */
  @Test
  fun `test parse release all inc`() {
    moshi.adapter(Release::class.java).fromJson(releaseIncAllJson)?.let { release ->
      expect(release.asin).toBeEmpty()
      expect(release.packaging).toBe("")
      expect(release.aliases).toBeEmpty()

      expect(release.coverArtArchive.count).toBe(1)
      expect(release.coverArtArchive.front).toHold()

      expect(release.media).toHaveSize(2)
      val firstMedia = release.media.first()
      expect(firstMedia.formatId).toBe("9712d52a-4509-3d4b-a1a2-67c88c643e31")
      expect(firstMedia.trackCount).toBe(1)
      expect(firstMedia.theTracks).toHaveSize(1)
    } ?: fail("Release is null")
  }

  @Test
  fun `test release with null objects`() {
    moshi.adapter(Release::class.java).lenient().fromJson(releaseWithNullObjects)?.let { release ->
      expect(release.coverArtArchive).toBeTheSameAs(CoverArtArchive.NullCoverArtArchive)
      expect(release.textRepresentation).toBeTheSameAs(TextRepresentation.NullTextRepresentation)
    } ?: fail("Release is null")
  }

  @Test
  fun `test release with missing objects`() {
    moshi.adapter(Release::class.java)
      .lenient()
      .fromJson(releaseWithMissingObjects)
      ?.let { release ->
        expect(release.coverArtArchive).toBeTheSameAs(CoverArtArchive.NullCoverArtArchive)
        expect(release.textRepresentation).toBeTheSameAs(TextRepresentation.NullTextRepresentation)
      } ?: fail("Release is null")
  }
}

private const val releaseIncAllJson =
  """
{
  "asin": null,
  "packaging": null,
  "aliases": [],
  "cover-art-archive": {
    "count": 1,
    "front": true,
    "artwork": true,
    "darkened": false,
    "back": false
  },
  "genres": [],
  "disambiguation": "",
  "tags": [],
  "barcode": null,
  "packaging-id": null,
  "text-representation": {
    "language": "eng",
    "script": "Latn"
  },
  "date": "2003-12-04",
  "status": "Official",
  "quality": "normal",
  "media": [
    {
      "format-id": "9712d52a-4509-3d4b-a1a2-67c88c643e31",
      "tracks": [
        {
          "title": "æ³o",
          "number": "1",
          "artist-credit": [
            {
              "name": "Autechre",
              "artist": {
                "sort-name": "Autechre",
                "disambiguation": "English electronic music duo Rob Brown & Sean Booth",
                "aliases": [
                  {
                    "begin": null,
                    "sort-name": "Autchere",
                    "end": null,
                    "type": null,
                    "locale": null,
                    "type-id": null,
                    "primary": null,
                    "name": "Autchere",
                    "ended": false
                  },
                  {
                    "primary": null,
                    "ended": false,
                    "name": "Autecher",
                    "type-id": null,
                    "end": null,
                    "locale": null,
                    "type": null,
                    "sort-name": "Autecher",
                    "begin": null
                  },
                  {
                    "type-id": null,
                    "name": "Autreche",
                    "ended": false,
                    "primary": null,
                    "begin": null,
                    "sort-name": "Autreche",
                    "locale": null,
                    "type": null,
                    "end": null
                  },
                  {
                    "ended": false,
                    "name": "ae",
                    "primary": null,
                    "type-id": null,
                    "locale": null,
                    "type": null,
                    "end": null,
                    "sort-name": "ae",
                    "begin": null
                  },
                  {
                    "type-id": "894afba6-2816-3c24-8072-eadb66bd04bc",
                    "name": "オウテカ",
                    "ended": false,
                    "primary": false,
                    "begin": null,
                    "sort-name": "オウテカ",
                    "type": "Artist name",
                    "locale": "ja",
                    "end": null
                  }
                ],
                "name": "Autechre",
                "rating": {
                  "votes-count": 15,
                  "value": 5
                },
                "id": "410c9baf-5469-44f6-9852-826524b80c61"
              },
              "joinphrase": " & "
            },
            {
              "artist": {
                "aliases": [
                  {
                    "type": null,
                    "locale": null,
                    "end": null,
                    "sort-name": "H3ÖH (The Hafler Trio)",
                    "begin": null,
                    "ended": false,
                    "name": "H3ÖH (The Hafler Trio)",
                    "primary": null,
                    "type-id": null
                  },
                  {
                    "type-id": null,
                    "name": "Hafler Trio",
                    "ended": false,
                    "primary": null,
                    "begin": null,
                    "sort-name": "Hafler Trio",
                    "locale": null,
                    "type": null,
                    "end": null
                  }
                ],
                "name": "The Hafler Trio",
                "id": "146c01d0-d3a2-44c3-acb5-9208bce75e14",
                "rating": {
                  "value": null,
                  "votes-count": 0
                },
                "sort-name": "Hafler Trio, The",
                "disambiguation": ""
              },
              "name": "The Hafler Trio",
              "joinphrase": ""
            }
          ],
          "position": 1,
          "id": "61af3e5a-14e0-350d-9826-a884c6e586b1",
          "recording": {
            "title": "æ³o",
            "isrcs": [],
            "artist-credit": [
              {
                "artist": {
                  "id": "410c9baf-5469-44f6-9852-826524b80c61",
                  "rating": {
                    "votes-count": 15,
                    "value": 5
                  },
                  "name": "Autechre",
                  "disambiguation": "English electronic music duo Rob Brown & Sean Booth",
                  "sort-name": "Autechre"
                },
                "name": "Autechre",
                "joinphrase": " & "
              },
              {
                "joinphrase": "",
                "artist": {
                  "sort-name": "Hafler Trio, The",
                  "disambiguation": "",
                  "name": "The Hafler Trio",
                  "rating": {
                    "votes-count": 0,
                    "value": null
                  },
                  "id": "146c01d0-d3a2-44c3-acb5-9208bce75e14"
                },
                "name": "The Hafler Trio"
              }
            ],
            "aliases": [],
            "length": 974546,
            "video": false,
            "disambiguation": "",
            "tags": [],
            "genres": [],
            "id": "af87f070-238b-46c1-aa3e-f831ab91fa20",
            "rating": {
              "votes-count": 2,
              "value": 4.5
            }
          },
          "length": 974546
        }
      ],
      "track-count": 1,
      "format": "CD",
      "discs": [
        {
          "sectors": 73241,
          "offsets": [
            150
          ],
          "id": "nN2g3a0ZSjovyIgK3bJl6_.j8C4-",
          "offset-count": 1
        }
      ],
      "position": 1,
      "track-offset": 0,
      "title": "æ³o"
    },
    {
      "format-id": "9712d52a-4509-3d4b-a1a2-67c88c643e31",
      "track-count": 1,
      "tracks": [
        {
          "title": "h³æ",
          "number": "1",
          "position": 1,
          "artist-credit": [
            {
              "joinphrase": " & ",
              "name": "Autechre",
              "artist": {
                "sort-name": "Autechre",
                "disambiguation": "English electronic music duo Rob Brown & Sean Booth",
                "aliases": [
                  {
                    "sort-name": "Autchere",
                    "begin": null,
                    "type": null,
                    "locale": null,
                    "end": null,
                    "type-id": null,
                    "ended": false,
                    "name": "Autchere",
                    "primary": null
                  },
                  {
                    "type-id": null,
                    "primary": null,
                    "ended": false,
                    "name": "Autecher",
                    "sort-name": "Autecher",
                    "begin": null,
                    "end": null,
                    "type": null,
                    "locale": null
                  },
                  {
                    "ended": false,
                    "name": "Autreche",
                    "primary": null,
                    "type-id": null,
                    "type": null,
                    "locale": null,
                    "end": null,
                    "sort-name": "Autreche",
                    "begin": null
                  },
                  {
                    "type-id": null,
                    "primary": null,
                    "name": "ae",
                    "ended": false,
                    "begin": null,
                    "sort-name": "ae",
                    "end": null,
                    "locale": null,
                    "type": null
                  },
                  {
                    "locale": "ja",
                    "type": "Artist name",
                    "end": null,
                    "begin": null,
                    "sort-name": "オウテカ",
                    "name": "オウテカ",
                    "ended": false,
                    "primary": false,
                    "type-id": "894afba6-2816-3c24-8072-eadb66bd04bc"
                  }
                ],
                "id": "410c9baf-5469-44f6-9852-826524b80c61",
                "rating": {
                  "votes-count": 15,
                  "value": 5
                },
                "name": "Autechre"
              }
            },
            {
              "joinphrase": "",
              "name": "The Hafler Trio",
              "artist": {
                "disambiguation": "",
                "sort-name": "Hafler Trio, The",
                "id": "146c01d0-d3a2-44c3-acb5-9208bce75e14",
                "rating": {
                  "value": null,
                  "votes-count": 0
                },
                "name": "The Hafler Trio",
                "aliases": [
                  {
                    "ended": false,
                    "name": "H3ÖH (The Hafler Trio)",
                    "primary": null,
                    "type-id": null,
                    "locale": null,
                    "type": null,
                    "end": null,
                    "sort-name": "H3ÖH (The Hafler Trio)",
                    "begin": null
                  },
                  {
                    "primary": null,
                    "name": "Hafler Trio",
                    "ended": false,
                    "type-id": null,
                    "end": null,
                    "type": null,
                    "locale": null,
                    "begin": null,
                    "sort-name": "Hafler Trio"
                  }
                ]
              }
            }
          ],
          "length": 922546,
          "recording": {
            "rating": {
              "votes-count": 2,
              "value": 4.5
            },
            "id": "5aff6309-2e02-4a47-9233-32d7dcc9a960",
            "genres": [],
            "disambiguation": "",
            "tags": [],
            "aliases": [],
            "artist-credit": [
              {
                "artist": {
                  "sort-name": "Autechre",
                  "disambiguation": "English electronic music duo Rob Brown & Sean Booth",
                  "name": "Autechre",
                  "rating": {
                    "votes-count": 15,
                    "value": 5
                  },
                  "id": "410c9baf-5469-44f6-9852-826524b80c61"
                },
                "name": "Autechre",
                "joinphrase": " & "
              },
              {
                "artist": {
                  "rating": {
                    "votes-count": 0,
                    "value": null
                  },
                  "id": "146c01d0-d3a2-44c3-acb5-9208bce75e14",
                  "name": "The Hafler Trio",
                  "sort-name": "Hafler Trio, The",
                  "disambiguation": ""
                },
                "name": "The Hafler Trio",
                "joinphrase": ""
              }
            ],
            "length": 922546,
            "video": false,
            "title": "h³æ",
            "isrcs": []
          },
          "id": "5f2031a2-c67d-3bec-8ae5-8d22847ab0a5"
        }
      ],
      "track-offset": 0,
      "title": "h³æ",
      "format": "CD",
      "discs": [
        {
          "sectors": 69341,
          "id": "aSHvkMnq2jZVFEK.DmSPbvN_f54-",
          "offsets": [
            150
          ],
          "offset-count": 1
        }
      ],
      "position": 2
    }
  ],
  "title": "æ³o & h³æ",
  "annotation": "",
  "status-id": "4e304316-386d-3409-af2e-78857eec5cfe",
  "artist-credit": [
    {
      "joinphrase": " & ",
      "artist": {
        "sort-name": "Autechre",
        "genres": [
          {
            "count": 1,
            "name": "ambient techno"
          },
          {
            "name": "electronic",
            "count": 8
          },
          {
            "name": "electronica",
            "count": 1
          },
          {
            "count": 4,
            "name": "experimental"
          },
          {
            "count": 2,
            "name": "glitch"
          },
          {
            "count": 8,
            "name": "idm"
          }
        ],
        "disambiguation": "English electronic music duo Rob Brown & Sean Booth",
        "tags": [
          {
            "name": "abstract",
            "count": 1
          },
          {
            "count": 1,
            "name": "ambient techno"
          },
          {
            "count": 1,
            "name": "autechre"
          },
          {
            "count": 2,
            "name": "british"
          },
          {
            "name": "dance and electronica",
            "count": 1
          },
          {
            "name": "electronic",
            "count": 8
          },
          {
            "name": "electronica",
            "count": 1
          },
          {
            "name": "english",
            "count": 1
          },
          {
            "name": "experimental",
            "count": 4
          },
          {
            "count": 2,
            "name": "glitch"
          },
          {
            "count": 8,
            "name": "idm"
          },
          {
            "count": 1,
            "name": "mutachre"
          },
          {
            "count": 3,
            "name": "uk"
          },
          {
            "name": "warp",
            "count": 2
          }
        ],
        "aliases": [
          {
            "sort-name": "Autchere",
            "begin": null,
            "end": null,
            "type": null,
            "locale": null,
            "type-id": null,
            "primary": null,
            "ended": false,
            "name": "Autchere"
          },
          {
            "locale": null,
            "type": null,
            "end": null,
            "sort-name": "Autecher",
            "begin": null,
            "ended": false,
            "name": "Autecher",
            "primary": null,
            "type-id": null
          },
          {
            "end": null,
            "type": null,
            "locale": null,
            "begin": null,
            "sort-name": "Autreche",
            "primary": null,
            "name": "Autreche",
            "ended": false,
            "type-id": null
          },
          {
            "primary": null,
            "name": "ae",
            "ended": false,
            "type-id": null,
            "end": null,
            "type": null,
            "locale": null,
            "begin": null,
            "sort-name": "ae"
          },
          {
            "name": "オウテカ",
            "ended": false,
            "primary": false,
            "type-id": "894afba6-2816-3c24-8072-eadb66bd04bc",
            "type": "Artist name",
            "locale": "ja",
            "end": null,
            "begin": null,
            "sort-name": "オウテカ"
          }
        ],
        "name": "Autechre",
        "id": "410c9baf-5469-44f6-9852-826524b80c61"
      },
      "name": "Autechre"
    },
    {
      "name": "The Hafler Trio",
      "artist": {
        "disambiguation": "",
        "tags": [
          {
            "name": "ambient",
            "count": 2
          },
          {
            "count": 1,
            "name": "british"
          },
          {
            "count": 2,
            "name": "dark ambient"
          },
          {
            "name": "drone",
            "count": 2
          },
          {
            "name": "electronic",
            "count": 1
          },
          {
            "name": "experimental",
            "count": 1
          },
          {
            "name": "glitch",
            "count": 2
          },
          {
            "count": 1,
            "name": "uk"
          }
        ],
        "genres": [
          {
            "name": "ambient",
            "count": 2
          },
          {
            "name": "dark ambient",
            "count": 2
          },
          {
            "count": 2,
            "name": "drone"
          },
          {
            "count": 1,
            "name": "electronic"
          },
          {
            "count": 1,
            "name": "experimental"
          },
          {
            "count": 2,
            "name": "glitch"
          }
        ],
        "sort-name": "Hafler Trio, The",
        "id": "146c01d0-d3a2-44c3-acb5-9208bce75e14",
        "name": "The Hafler Trio",
        "aliases": [
          {
            "type-id": null,
            "name": "H3ÖH (The Hafler Trio)",
            "ended": false,
            "primary": null,
            "begin": null,
            "sort-name": "H3ÖH (The Hafler Trio)",
            "type": null,
            "locale": null,
            "end": null
          },
          {
            "type-id": null,
            "name": "Hafler Trio",
            "ended": false,
            "primary": null,
            "begin": null,
            "sort-name": "Hafler Trio",
            "locale": null,
            "type": null,
            "end": null
          }
        ]
      },
      "joinphrase": ""
    }
  ],
  "country": "GB",
  "release-events": [
    {
      "area": {
        "name": "United Kingdom",
        "iso-3166-1-codes": [
          "GB"
        ],
        "id": "8a754a16-0027-3a29-b6d7-2b40ea0481ed",
        "sort-name": "United Kingdom",
        "disambiguation": ""
      },
      "date": "2003-12-04"
    }
  ],
  "collections": [
    {
      "type-id": "d94659b2-4ce5-3a98-b4b8-da1131cf33ee",
      "entity-type": "release",
      "release-count": 278,
      "id": "70b4f89c-33a9-4f69-828c-f965956cd333",
      "type": "Release",
      "name": "[TɅRQ WɅVE TO REVIEW]",
      "editor": "Tarq"
    }
  ],
  "label-info": [
    {
      "label": {
        "id": "a0759efa-f583-49ea-9a8d-d5bbce55541c",
        "label-code": null,
        "genres": [],
        "tags": [],
        "disambiguation": "",
        "sort-name": "Phonometrography",
        "name": "Phonometrography",
        "aliases": []
      },
      "catalog-number": "pgram002"
    }
  ],
  "id": "59211ea4-ffd2-4ad9-9a4e-941d3148024a",
  "release-group": {
    "artist-credit": [
      {
        "joinphrase": " & ",
        "artist": {
          "disambiguation": "English electronic music duo Rob Brown & Sean Booth",
          "sort-name": "Autechre",
          "rating": {
            "value": 5,
            "votes-count": 15
          },
          "id": "410c9baf-5469-44f6-9852-826524b80c61",
          "name": "Autechre",
          "aliases": [
            {
              "type-id": null,
              "primary": null,
              "ended": false,
              "name": "Autchere",
              "sort-name": "Autchere",
              "begin": null,
              "end": null,
              "locale": null,
              "type": null
            },
            {
              "begin": null,
              "sort-name": "Autecher",
              "end": null,
              "type": null,
              "locale": null,
              "type-id": null,
              "primary": null,
              "name": "Autecher",
              "ended": false
            },
            {
              "name": "Autreche",
              "ended": false,
              "primary": null,
              "type-id": null,
              "locale": null,
              "type": null,
              "end": null,
              "begin": null,
              "sort-name": "Autreche"
            },
            {
              "name": "ae",
              "ended": false,
              "primary": null,
              "type-id": null,
              "locale": null,
              "type": null,
              "end": null,
              "begin": null,
              "sort-name": "ae"
            },
            {
              "ended": false,
              "name": "オウテカ",
              "primary": false,
              "type-id": "894afba6-2816-3c24-8072-eadb66bd04bc",
              "type": "Artist name",
              "locale": "ja",
              "end": null,
              "sort-name": "オウテカ",
              "begin": null
            }
          ]
        },
        "name": "Autechre"
      },
      {
        "joinphrase": "",
        "artist": {
          "sort-name": "Hafler Trio, The",
          "disambiguation": "",
          "aliases": [
            {
              "begin": null,
              "sort-name": "H3ÖH (The Hafler Trio)",
              "locale": null,
              "type": null,
              "end": null,
              "type-id": null,
              "name": "H3ÖH (The Hafler Trio)",
              "ended": false,
              "primary": null
            },
            {
              "type-id": null,
              "primary": null,
              "ended": false,
              "name": "Hafler Trio",
              "sort-name": "Hafler Trio",
              "begin": null,
              "end": null,
              "type": null,
              "locale": null
            }
          ],
          "name": "The Hafler Trio",
          "rating": {
            "votes-count": 0,
            "value": null
          },
          "id": "146c01d0-d3a2-44c3-acb5-9208bce75e14"
        },
        "name": "The Hafler Trio"
      }
    ],
    "aliases": [],
    "title": "æ³o & h³æ",
    "first-release-date": "2003-12-04",
    "primary-type": "EP",
    "secondary-types": [],
    "rating": {
      "value": 3,
      "votes-count": 1
    },
    "secondary-type-ids": [],
    "id": "bb5622c1-65b2-3bc4-83e6-0dcaa56abaf2",
    "primary-type-id": "6d0c5bf6-7a33-3420-a519-44fc63eedebf",
    "tags": [
      {
        "name": "abstract",
        "count": 1
      },
      {
        "name": "ambient",
        "count": 1
      },
      {
        "name": "minimal",
        "count": 1
      }
    ],
    "disambiguation": "",
    "genres": [
      {
        "count": 1,
        "name": "ambient"
      },
      {
        "count": 1,
        "name": "minimal"
      }
    ]
  }
}"""

private const val releaseWithNullObjects =
  """
{
  "media": [
    {
      "title": "æ³o",
      "track-offset": 0,
      "track-count": 1,
      "format": "CD",
      "format-id": "9712d52a-4509-3d4b-a1a2-67c88c643e31",
      "tracks": [
        {
          "id": "61af3e5a-14e0-350d-9826-a884c6e586b1",
          "title": "æ³o",
          "number": "1",
          "position": 1,
          "length": 974546,
          "recording": {
            "id": "af87f070-238b-46c1-aa3e-f831ab91fa20",
            "video": false,
            "disambiguation": "",
            "title": "æ³o",
            "length": 974546
          }
        }
      ],
      "position": 1
    },
    {
      "track-count": 1,
      "format": "CD",
      "title": "h³æ",
      "track-offset": 0,
      "format-id": "9712d52a-4509-3d4b-a1a2-67c88c643e31",
      "tracks": [
        {
          "id": "5f2031a2-c67d-3bec-8ae5-8d22847ab0a5",
          "title": "h³æ",
          "length": 922546,
          "position": 1,
          "number": "1",
          "recording": {
            "length": 922546,
            "id": "5aff6309-2e02-4a47-9233-32d7dcc9a960",
            "video": false,
            "title": "h³æ",
            "disambiguation": ""
          }
        }
      ],
      "position": 2
    }
  ],
  "id": "59211ea4-ffd2-4ad9-9a4e-941d3148024a",
  "status": "Official",
  "quality": "normal",
  "packaging": null,
  "country": "GB",
  "title": "æ³o & h³æ",
  "status-id": "4e304316-386d-3409-af2e-78857eec5cfe",
  "barcode": null,
  "disambiguation": "",
  "date": "2003-12-04",
  "asin": null,
  "release-events": [
    {
      "date": "2003-12-04",
      "area": {
        "name": "United Kingdom",
        "iso-3166-1-codes": [
          "GB"
        ],
        "id": "8a754a16-0027-3a29-b6d7-2b40ea0481ed",
        "sort-name": "United Kingdom",
        "disambiguation": ""
      }
    }
  ],
  "packaging-id": null,
  "cover-art-archive": null,
  "text-representation": null
}
"""

private const val releaseWithMissingObjects =
  """
{
  "media": [
    {
      "title": "æ³o",
      "track-offset": 0,
      "track-count": 1,
      "format": "CD",
      "format-id": "9712d52a-4509-3d4b-a1a2-67c88c643e31",
      "tracks": [
        {
          "id": "61af3e5a-14e0-350d-9826-a884c6e586b1",
          "title": "æ³o",
          "number": "1",
          "position": 1,
          "length": 974546,
          "recording": {
            "id": "af87f070-238b-46c1-aa3e-f831ab91fa20",
            "video": false,
            "disambiguation": "",
            "title": "æ³o",
            "length": 974546
          }
        }
      ],
      "position": 1
    },
    {
      "track-count": 1,
      "format": "CD",
      "title": "h³æ",
      "track-offset": 0,
      "format-id": "9712d52a-4509-3d4b-a1a2-67c88c643e31",
      "tracks": [
        {
          "id": "5f2031a2-c67d-3bec-8ae5-8d22847ab0a5",
          "title": "h³æ",
          "length": 922546,
          "position": 1,
          "number": "1",
          "recording": {
            "length": 922546,
            "id": "5aff6309-2e02-4a47-9233-32d7dcc9a960",
            "video": false,
            "title": "h³æ",
            "disambiguation": ""
          }
        }
      ],
      "position": 2
    }
  ],
  "id": "59211ea4-ffd2-4ad9-9a4e-941d3148024a",
  "status": "Official",
  "quality": "normal",
  "packaging": null,
  "country": "GB",
  "title": "æ³o & h³æ",
  "status-id": "4e304316-386d-3409-af2e-78857eec5cfe",
  "barcode": null,
  "disambiguation": "",
  "date": "2003-12-04",
  "asin": null,
  "release-events": [
    {
      "date": "2003-12-04",
      "area": {
        "name": "United Kingdom",
        "iso-3166-1-codes": [
          "GB"
        ],
        "id": "8a754a16-0027-3a29-b6d7-2b40ea0481ed",
        "sort-name": "United Kingdom",
        "disambiguation": ""
      }
    }
  ],
  "packaging-id": null
}
"""
