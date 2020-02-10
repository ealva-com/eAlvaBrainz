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

import com.nhaarman.expect.expect
import com.nhaarman.expect.fail
import com.squareup.moshi.Moshi
import org.junit.Before
import org.junit.Test

class ArtistTest {
  private lateinit var moshi: Moshi

  @Before
  fun setup() {
    moshi = theMoshi
  }

  /**
   * Obviously non-exhaustive - test that various areas of a Release are parsed as expected.
   */
  @Test
  fun `test parse release all inc`() {
    moshi.adapter(Artist::class.java).fromJson(artistAllIncJson)?.let { artist ->
      expect(artist.gender).toBe("Male")
      expect(artist.beginArea.id).toBe("dc08b8ca-f6ad-4163-b78e-d29079f5521a")
      expect(artist.beginArea.name).toBe("Lakewood")
      expect(artist.recordings[0].artistCredit[0].name).toBe("Benjamin Orr")
      expect(artist.recordings[0].artistCredit[0].artist.genres[0].name).toBe("rock")
      expect(artist.recordings[1].isrcs[0]).toBe("USEE10608065")
      expect(artist.releases[0].title).toBe("The Power of Love 3")
      expect(artist.releases[5].packaging).toBe("Box")
      expect(artist.endArea.name).toBe("Atlanta")
      expect(artist.endArea.id).toBe("26e0e534-19ea-4645-bfb3-1aa4e83a4046")
//      artist.tags[0].run {
//        expect(name).toBe("rock")
//        expect(count).toBe(1)
//      }
    } ?: fail("Release is null")
  }
}

private const val artistAllIncJson = """
{
  "begin_area": {
    "sort-name": "Lakewood",
    "disambiguation": "",
    "id": "dc08b8ca-f6ad-4163-b78e-d29079f5521a",
    "name": "Lakewood"
  },
  "gender": "Male",
  "recordings": [
    {
      "id": "f1e22de6-3836-4cc0-b6e5-75e97623189f",
      "disambiguation": "",
      "aliases": [],
      "artist-credit": [
        {
          "name": "Benjamin Orr",
          "joinphrase": "",
          "artist": {
            "name": "Benjamin Orr",
            "tags": [
              {
                "count": 1,
                "name": "rock"
              }
            ],
            "genres": [
              {
                "name": "rock",
                "count": 1
              }
            ],
            "id": "94652393-67a7-4688-ad62-d30111c9ff0c",
            "disambiguation": "",
            "sort-name": "Orr, Benjamin",
            "aliases": []
          }
        }
      ],
      "length": 271400,
      "tags": [],
      "title": "Hold On",
      "isrcs": [
        "USEE10608069"
      ],
      "genres": [],
      "video": false
    },
    {
      "aliases": [],
      "id": "cd174099-b077-4fb1-a076-00f4886833b4",
      "disambiguation": "",
      "artist-credit": [
        {
          "artist": {
            "genres": [
              {
                "name": "rock",
                "count": 1
              }
            ],
            "tags": [
              {
                "count": 1,
                "name": "rock"
              }
            ],
            "name": "Benjamin Orr",
            "aliases": [],
            "sort-name": "Orr, Benjamin",
            "disambiguation": "",
            "id": "94652393-67a7-4688-ad62-d30111c9ff0c"
          },
          "joinphrase": "",
          "name": "Benjamin Orr"
        }
      ],
      "length": 272493,
      "tags": [],
      "title": "In Circles",
      "genres": [],
      "video": false,
      "isrcs": [
        "USEE10608065"
      ]
    },
    {
      "title": "Skyline",
      "video": false,
      "genres": [],
      "isrcs": [
        "USEE10608066"
      ],
      "aliases": [],
      "disambiguation": "",
      "id": "a6cc9f90-2936-4aa4-a766-cb3a99af8f55",
      "tags": [],
      "artist-credit": [
        {
          "artist": {
            "id": "94652393-67a7-4688-ad62-d30111c9ff0c",
            "disambiguation": "",
            "sort-name": "Orr, Benjamin",
            "aliases": [],
            "name": "Benjamin Orr",
            "tags": [
              {
                "name": "rock",
                "count": 1
              }
            ],
            "genres": [
              {
                "name": "rock",
                "count": 1
              }
            ]
          },
          "name": "Benjamin Orr",
          "joinphrase": ""
        }
      ],
      "length": 250133
    },
    {
      "title": "Spinning",
      "video": false,
      "genres": [],
      "isrcs": [
        "USEE10608068"
      ],
      "aliases": [],
      "id": "f01fe70f-e6d5-428f-ba2c-5003be7b58cc",
      "disambiguation": "",
      "length": 268973,
      "artist-credit": [
        {
          "joinphrase": "",
          "name": "Benjamin Orr",
          "artist": {
            "id": "94652393-67a7-4688-ad62-d30111c9ff0c",
            "disambiguation": "",
            "sort-name": "Orr, Benjamin",
            "aliases": [],
            "name": "Benjamin Orr",
            "tags": [
              {
                "count": 1,
                "name": "rock"
              }
            ],
            "genres": [
              {
                "name": "rock",
                "count": 1
              }
            ]
          }
        }
      ],
      "tags": []
    },
    {
      "tags": [],
      "length": 267066,
      "artist-credit": [
        {
          "artist": {
            "sort-name": "Orr, Benjamin",
            "aliases": [],
            "id": "94652393-67a7-4688-ad62-d30111c9ff0c",
            "disambiguation": "",
            "genres": [
              {
                "name": "rock",
                "count": 1
              }
            ],
            "name": "Benjamin Orr",
            "tags": [
              {
                "count": 1,
                "name": "rock"
              }
            ]
          },
          "name": "Benjamin Orr",
          "joinphrase": ""
        }
      ],
      "disambiguation": "",
      "id": "71082377-5a62-40c2-a24f-7fd27956f237",
      "aliases": [],
      "isrcs": [
        "USEE10251351"
      ],
      "genres": [],
      "video": false,
      "title": "Stay the Night"
    },
    {
      "aliases": [],
      "id": "e1f78c11-0327-4009-83b1-588371851c06",
      "disambiguation": "",
      "artist-credit": [
        {
          "joinphrase": "",
          "name": "Benjamin Orr",
          "artist": {
            "sort-name": "Orr, Benjamin",
            "aliases": [],
            "id": "94652393-67a7-4688-ad62-d30111c9ff0c",
            "disambiguation": "",
            "genres": [
              {
                "name": "rock",
                "count": 1
              }
            ],
            "name": "Benjamin Orr",
            "tags": [
              {
                "name": "rock",
                "count": 1
              }
            ]
          }
        }
      ],
      "length": 258000,
      "tags": [],
      "title": "Stay the Night",
      "genres": [],
      "video": false,
      "isrcs": []
    },
    {
      "id": "75f07bdc-3cb5-4131-a6b3-01ed2ee37979",
      "disambiguation": "",
      "aliases": [],
      "length": 262000,
      "artist-credit": [
        {
          "artist": {
            "aliases": [],
            "sort-name": "Orr, Benjamin",
            "disambiguation": "",
            "id": "94652393-67a7-4688-ad62-d30111c9ff0c",
            "genres": [
              {
                "count": 1,
                "name": "rock"
              }
            ],
            "tags": [
              {
                "count": 1,
                "name": "rock"
              }
            ],
            "name": "Benjamin Orr"
          },
          "joinphrase": "",
          "name": "Benjamin Orr"
        }
      ],
      "tags": [],
      "title": "Stay The Night",
      "isrcs": [],
      "video": false,
      "genres": []
    },
    {
      "tags": [],
      "length": 248360,
      "artist-credit": [
        {
          "joinphrase": "",
          "name": "Benjamin Orr",
          "artist": {
            "name": "Benjamin Orr",
            "tags": [
              {
                "name": "rock",
                "count": 1
              }
            ],
            "genres": [
              {
                "name": "rock",
                "count": 1
              }
            ],
            "id": "94652393-67a7-4688-ad62-d30111c9ff0c",
            "disambiguation": "",
            "sort-name": "Orr, Benjamin",
            "aliases": []
          }
        }
      ],
      "disambiguation": "",
      "id": "6e193fc8-f996-4b60-b029-40b4e2cd72c8",
      "aliases": [],
      "isrcs": [
        "USEE10608071"
      ],
      "genres": [],
      "video": false,
      "title": "That's the Way"
    },
    {
      "isrcs": [
        "USEE10608070"
      ],
      "genres": [],
      "video": false,
      "title": "The Lace",
      "tags": [],
      "artist-credit": [
        {
          "artist": {
            "genres": [
              {
                "name": "rock",
                "count": 1
              }
            ],
            "name": "Benjamin Orr",
            "tags": [
              {
                "count": 1,
                "name": "rock"
              }
            ],
            "sort-name": "Orr, Benjamin",
            "aliases": [],
            "id": "94652393-67a7-4688-ad62-d30111c9ff0c",
            "disambiguation": ""
          },
          "joinphrase": "",
          "name": "Benjamin Orr"
        }
      ],
      "length": 262200,
      "disambiguation": "",
      "id": "072e5217-e968-40c6-b1cd-db50518507c2",
      "aliases": []
    },
    {
      "genres": [],
      "video": false,
      "isrcs": [
        "USEE10608072"
      ],
      "title": "This Time Around",
      "length": 310640,
      "artist-credit": [
        {
          "artist": {
            "genres": [
              {
                "count": 1,
                "name": "rock"
              }
            ],
            "name": "Benjamin Orr",
            "tags": [
              {
                "name": "rock",
                "count": 1
              }
            ],
            "sort-name": "Orr, Benjamin",
            "aliases": [],
            "id": "94652393-67a7-4688-ad62-d30111c9ff0c",
            "disambiguation": ""
          },
          "name": "Benjamin Orr",
          "joinphrase": ""
        }
      ],
      "tags": [],
      "aliases": [],
      "id": "158a67f0-b1b6-4fa3-ad4d-9dd4c09a4b15",
      "disambiguation": ""
    },
    {
      "disambiguation": "",
      "id": "6bb57be1-b4b0-4b4c-8fb1-f30dd8899177",
      "aliases": [],
      "tags": [],
      "length": 260240,
      "artist-credit": [
        {
          "artist": {
            "name": "Benjamin Orr",
            "tags": [
              {
                "name": "rock",
                "count": 1
              }
            ],
            "genres": [
              {
                "count": 1,
                "name": "rock"
              }
            ],
            "id": "94652393-67a7-4688-ad62-d30111c9ff0c",
            "disambiguation": "",
            "sort-name": "Orr, Benjamin",
            "aliases": []
          },
          "name": "Benjamin Orr",
          "joinphrase": ""
        }
      ],
      "title": "Too Hot to Stop",
      "isrcs": [
        "USEE10608064"
      ],
      "genres": [],
      "video": false
    },
    {
      "artist-credit": [
        {
          "name": "Benjamin Orr",
          "joinphrase": "",
          "artist": {
            "name": "Benjamin Orr",
            "tags": [
              {
                "count": 1,
                "name": "rock"
              }
            ],
            "genres": [
              {
                "count": 1,
                "name": "rock"
              }
            ],
            "id": "94652393-67a7-4688-ad62-d30111c9ff0c",
            "disambiguation": "",
            "sort-name": "Orr, Benjamin",
            "aliases": []
          }
        }
      ],
      "length": 291200,
      "tags": [],
      "id": "27ad9ee9-10fb-440a-9fbe-72ff63d8c430",
      "disambiguation": "",
      "aliases": [],
      "isrcs": [
        "USEE10608067"
      ],
      "video": false,
      "genres": [],
      "title": "When You're Gone"
    }
  ],
  "type": "Person",
  "begin-area": {
    "sort-name": "Lakewood",
    "disambiguation": "",
    "id": "dc08b8ca-f6ad-4163-b78e-d29079f5521a",
    "name": "Lakewood"
  },
  "type-id": "b6e035f4-3ce9-331c-97df-83397230b0df",
  "gender-id": "36d3d30a-839d-3eda-8cb3-29be4384e4a9",
  "sort-name": "Orr, Benjamin",
  "aliases": [],
  "name": "Benjamin Orr",
  "isnis": [
    "000000011851397X"
  ],
  "works": [
    {
      "tags": [],
      "iswcs": [],
      "disambiguation": "",
      "type-id": null,
      "id": "4612bad6-7cfc-420d-9c5a-433907d0fdb9",
      "languages": [
        "eng"
      ],
      "language": "eng",
      "aliases": [],
      "genres": [],
      "type": null,
      "attributes": [],
      "title": "Stay the Night"
    }
  ],
  "ipis": [
    "00126197768"
  ],
  "country": "US",
  "end_area": {
    "name": "Atlanta",
    "sort-name": "Atlanta",
    "disambiguation": "",
    "id": "26e0e534-19ea-4645-bfb3-1aa4e83a4046"
  },
  "annotation": null,
  "genres": [
    {
      "count": 1,
      "name": "rock"
    }
  ],
  "id": "94652393-67a7-4688-ad62-d30111c9ff0c",
  "disambiguation": "",
  "life-span": {
    "begin": "1947-08-09",
    "ended": true,
    "end": "2000-10-03"
  },
  "area": {
    "name": "United States",
    "iso-3166-1-codes": [
      "US"
    ],
    "id": "489ce91b-6658-3307-9877-795b68554c98",
    "disambiguation": "",
    "sort-name": "United States"
  },
  "releases": [
    {
      "status": "Official",
      "title": "The Power of Love 3",
      "media": [
        {
          "discs": [
            {
              "offset-count": 16,
              "id": "xPsTmtG8PyTHkEF7luY2VrZw.9E-",
              "offsets": [
                150,
                21575,
                39942,
                58565,
                81955,
                101570,
                119870,
                140530,
                159690,
                182100,
                201092,
                219107,
                240642,
                261172,
                277040,
                297177
              ],
              "sectors": 314420
            }
          ],
          "position": 1,
          "format-id": "9712d52a-4509-3d4b-a1a2-67c88c643e31",
          "track-count": 16,
          "title": "",
          "format": "CD"
        }
      ],
      "release-group": null,
      "packaging": null,
      "release-events": [
        {
          "area": {
            "sort-name": "United States",
            "id": "489ce91b-6658-3307-9877-795b68554c98",
            "disambiguation": "",
            "name": "United States",
            "iso-3166-1-codes": [
              "US"
            ]
          },
          "date": "1988"
        }
      ],
      "text-representation": {
        "language": "eng",
        "script": "Latn"
      },
      "date": "1988",
      "quality": "normal",
      "aliases": [],
      "country": "US",
      "genres": [],
      "barcode": null,
      "packaging-id": null,
      "artist-credit": [
        {
          "artist": {
            "tags": [],
            "name": "Various Artists",
            "genres": [],
            "disambiguation": "add compilations to this artist",
            "id": "89ad4ac3-39f7-470e-963a-56509c546377",
            "aliases": [],
            "sort-name": "Various Artists"
          },
          "joinphrase": "",
          "name": "Various Artists"
        }
      ],
      "status-id": "4e304316-386d-3409-af2e-78857eec5cfe",
      "tags": [],
      "id": "68f15ced-4d48-4a84-a733-3d9de985f49b",
      "disambiguation": ""
    },
    {
      "title": "The Best of '80s Rock, Volume 4",
      "media": [
        {
          "position": 1,
          "discs": [
            {
              "offset-count": 9,
              "id": "eaahwyzxcXBptB._2c18FcxknvY-",
              "offsets": [
                150,
                15873,
                33285,
                51508,
                71598,
                88903,
                108208,
                120250,
                136628
              ],
              "sectors": 149973
            }
          ],
          "title": "",
          "format": null,
          "track-count": 9,
          "format-id": null
        }
      ],
      "release-group": null,
      "packaging": null,
      "text-representation": {
        "script": "Latn",
        "language": "eng"
      },
      "release-events": [
        {
          "date": "1994-11-29",
          "area": {
            "iso-3166-1-codes": [
              "US"
            ],
            "name": "United States",
            "disambiguation": "",
            "id": "489ce91b-6658-3307-9877-795b68554c98",
            "sort-name": "United States"
          }
        }
      ],
      "date": "1994-11-29",
      "status": "Official",
      "quality": "normal",
      "aliases": [],
      "packaging-id": null,
      "barcode": null,
      "country": "US",
      "genres": [],
      "id": "47d6c59e-171a-43d2-b62c-4211cddf33d2",
      "disambiguation": "",
      "artist-credit": [
        {
          "name": "Various Artists",
          "joinphrase": "",
          "artist": {
            "genres": [],
            "name": "Various Artists",
            "tags": [],
            "sort-name": "Various Artists",
            "aliases": [],
            "id": "89ad4ac3-39f7-470e-963a-56509c546377",
            "disambiguation": "add compilations to this artist"
          }
        }
      ],
      "status-id": "4e304316-386d-3409-af2e-78857eec5cfe",
      "tags": []
    },
    {
      "release-group": null,
      "title": "Then '80s Again, Totally Oldies 7",
      "media": [
        {
          "format": "CD",
          "title": "",
          "track-count": 18,
          "format-id": "9712d52a-4509-3d4b-a1a2-67c88c643e31",
          "discs": [
            {
              "offset-count": 18,
              "id": "s.rDS2HeC3IO8vAOIeEsZ6YmpHE-",
              "offsets": [
                150,
                16561,
                32395,
                47145,
                63195,
                75223,
                93203,
                108460,
                124788,
                137548,
                153743,
                172269,
                189146,
                208896,
                228813,
                245213,
                260673,
                279959
              ],
              "sectors": 305800
            }
          ],
          "position": 1
        }
      ],
      "date": "2004",
      "text-representation": {
        "script": "Latn",
        "language": "eng"
      },
      "release-events": [
        {
          "date": "2004",
          "area": {
            "iso-3166-1-codes": [
              "US"
            ],
            "name": "United States",
            "sort-name": "United States",
            "disambiguation": "",
            "id": "489ce91b-6658-3307-9877-795b68554c98"
          }
        }
      ],
      "packaging": null,
      "status": "Official",
      "quality": "normal",
      "aliases": [],
      "packaging-id": null,
      "barcode": "030206654622",
      "country": "US",
      "genres": [],
      "disambiguation": "",
      "id": "b00c679c-22a0-4d2a-941c-0269b2cfc182",
      "tags": [],
      "status-id": "4e304316-386d-3409-af2e-78857eec5cfe",
      "artist-credit": [
        {
          "joinphrase": "",
          "name": "Various Artists",
          "artist": {
            "tags": [],
            "name": "Various Artists",
            "genres": [],
            "disambiguation": "add compilations to this artist",
            "id": "89ad4ac3-39f7-470e-963a-56509c546377",
            "aliases": [],
            "sort-name": "Various Artists"
          }
        }
      ]
    },
    {
      "genres": [],
      "country": "FR",
      "barcode": "5052498680955",
      "packaging-id": null,
      "tags": [],
      "status-id": "4e304316-386d-3409-af2e-78857eec5cfe",
      "artist-credit": [
        {
          "name": "Various Artists",
          "joinphrase": "",
          "artist": {
            "tags": [],
            "name": "Various Artists",
            "genres": [],
            "disambiguation": "add compilations to this artist",
            "id": "89ad4ac3-39f7-470e-963a-56509c546377",
            "aliases": [],
            "sort-name": "Various Artists"
          }
        }
      ],
      "disambiguation": "",
      "id": "15ef42ca-068d-445a-9c45-34444aca7abe",
      "status": "Official",
      "text-representation": {
        "script": "Latn",
        "language": null
      },
      "date": "2011",
      "release-events": [
        {
          "date": "2011",
          "area": {
            "id": "08310658-51eb-3801-80de-5a0739207115",
            "disambiguation": "",
            "sort-name": "France",
            "name": "France",
            "iso-3166-1-codes": [
              "FR"
            ]
          }
        }
      ],
      "packaging": null,
      "release-group": null,
      "media": [
        {
          "title": "",
          "format": "CD",
          "track-count": 17,
          "format-id": "9712d52a-4509-3d4b-a1a2-67c88c643e31",
          "discs": [],
          "position": 1
        },
        {
          "position": 2,
          "discs": [],
          "format-id": "9712d52a-4509-3d4b-a1a2-67c88c643e31",
          "track-count": 19,
          "title": "",
          "format": "CD"
        },
        {
          "discs": [],
          "position": 3,
          "format-id": "9712d52a-4509-3d4b-a1a2-67c88c643e31",
          "track-count": 17,
          "title": "",
          "format": "CD"
        },
        {
          "discs": [],
          "position": 4,
          "title": "",
          "format": "CD",
          "format-id": "9712d52a-4509-3d4b-a1a2-67c88c643e31",
          "track-count": 20
        }
      ],
      "title": "California Groove Vol. III \"From L.A. To Miami\"",
      "aliases": [],
      "quality": "normal"
    },
    {
      "tags": [],
      "status-id": "4e304316-386d-3409-af2e-78857eec5cfe",
      "artist-credit": [
        {
          "artist": {
            "sort-name": "Various Artists",
            "aliases": [],
            "id": "89ad4ac3-39f7-470e-963a-56509c546377",
            "disambiguation": "add compilations to this artist",
            "genres": [],
            "name": "Various Artists",
            "tags": []
          },
          "joinphrase": "",
          "name": "Various Artists"
        }
      ],
      "disambiguation": "",
      "id": "dc5caf6e-e6ba-482d-9bb5-388417890b81",
      "genres": [],
      "country": "DE",
      "barcode": null,
      "packaging-id": null,
      "aliases": [],
      "quality": "normal",
      "status": "Official",
      "text-representation": {
        "script": null,
        "language": null
      },
      "release-events": [
        {
          "area": {
            "sort-name": "Germany",
            "id": "85752fda-13c4-31a3-bee5-0e5cb1f51dad",
            "disambiguation": "",
            "name": "Germany",
            "iso-3166-1-codes": [
              "DE"
            ]
          },
          "date": "2014-04-11"
        }
      ],
      "date": "2014-04-11",
      "packaging": null,
      "release-group": null,
      "media": [
        {
          "position": 1,
          "discs": [
            {
              "offsets": [
                150,
                21298,
                40394,
                59409,
                75069,
                96989,
                116401,
                140697,
                160454,
                180457,
                206614,
                225327,
                241054,
                258257,
                278945,
                304941,
                326004
              ],
              "sectors": 347640,
              "offset-count": 17,
              "id": "DyIFSnCBYVwjgQsmWE2ExTYbjNY-"
            }
          ],
          "format": "CD",
          "title": "Der Letzte Bulle, Volume 5, (disc 1)",
          "format-id": "9712d52a-4509-3d4b-a1a2-67c88c643e31",
          "track-count": 17
        },
        {
          "position": 2,
          "discs": [
            {
              "sectors": 332440,
              "offsets": [
                150,
                15093,
                31005,
                47351,
                64085,
                84421,
                100653,
                117286,
                137273,
                163542,
                184440,
                201451,
                220825,
                241192,
                260986,
                281105,
                299044,
                316919
              ],
              "id": "85BbN1I7aye2WXosD2VdS89.8Mo-",
              "offset-count": 18
            }
          ],
          "format": "CD",
          "title": "Der Letzte Bulle, Volume 5, (disc 2)",
          "track-count": 18,
          "format-id": "9712d52a-4509-3d4b-a1a2-67c88c643e31"
        }
      ],
      "title": "Der letzte Bulle, Volume 5"
    },
    {
      "packaging": "Box",
      "text-representation": {
        "script": "Latn",
        "language": "eng"
      },
      "release-events": [
        {
          "date": "2018",
          "area": {
            "name": "France",
            "iso-3166-1-codes": [
              "FR"
            ],
            "sort-name": "France",
            "id": "08310658-51eb-3801-80de-5a0739207115",
            "disambiguation": ""
          }
        }
      ],
      "date": "2018",
      "media": [
        {
          "position": 1,
          "discs": [],
          "track-count": 19,
          "format-id": "9712d52a-4509-3d4b-a1a2-67c88c643e31",
          "format": "CD",
          "title": ""
        },
        {
          "discs": [],
          "position": 2,
          "format": "CD",
          "title": "",
          "track-count": 21,
          "format-id": "9712d52a-4509-3d4b-a1a2-67c88c643e31"
        },
        {
          "discs": [],
          "position": 3,
          "title": "",
          "format": "CD",
          "format-id": "9712d52a-4509-3d4b-a1a2-67c88c643e31",
          "track-count": 17
        },
        {
          "discs": [],
          "position": 4,
          "format-id": "9712d52a-4509-3d4b-a1a2-67c88c643e31",
          "track-count": 19,
          "title": "",
          "format": "CD"
        },
        {
          "title": "",
          "format": "CD",
          "format-id": "9712d52a-4509-3d4b-a1a2-67c88c643e31",
          "track-count": 23,
          "position": 5,
          "discs": []
        },
        {
          "track-count": 19,
          "format-id": "9712d52a-4509-3d4b-a1a2-67c88c643e31",
          "format": "CD",
          "title": "",
          "position": 6,
          "discs": []
        }
      ],
      "title": "California Groove, Volume IV",
      "release-group": null,
      "status": "Official",
      "aliases": [],
      "quality": "normal",
      "barcode": "0190295671914",
      "packaging-id": "c1668fc7-8944-4a00-bc3e-46e8d861d211",
      "genres": [],
      "country": "FR",
      "id": "46335dfb-db08-4e9c-96c6-a9d68cf28bf4",
      "disambiguation": "",
      "artist-credit": [
        {
          "name": "Various Artists",
          "joinphrase": "",
          "artist": {
            "genres": [],
            "name": "Various Artists",
            "tags": [],
            "sort-name": "Various Artists",
            "aliases": [],
            "id": "89ad4ac3-39f7-470e-963a-56509c546377",
            "disambiguation": "add compilations to this artist"
          }
        }
      ],
      "tags": [],
      "status-id": "4e304316-386d-3409-af2e-78857eec5cfe"
    }
  ],
  "release-groups": [
    {
      "disambiguation": "",
      "id": "73f92e3b-def7-4e57-9e46-10de06cc006f",
      "releases": [],
      "aliases": [],
      "primary-type": "Album",
      "tags": [],
      "artist-credit": [
        {
          "artist": {
            "genres": [
              {
                "name": "rock",
                "count": 1
              }
            ],
            "name": "Benjamin Orr",
            "tags": [
              {
                "name": "rock",
                "count": 1
              }
            ],
            "sort-name": "Orr, Benjamin",
            "aliases": [],
            "id": "94652393-67a7-4688-ad62-d30111c9ff0c",
            "disambiguation": ""
          },
          "name": "Benjamin Orr",
          "joinphrase": ""
        }
      ],
      "secondary-types": [],
      "first-release-date": "1986",
      "title": "The Lace",
      "secondary-type-ids": [],
      "primary-type-id": "f529b476-6e62-324f-b0aa-1f3e33d313fc",
      "genres": []
    },
    {
      "genres": [],
      "primary-type-id": "d6038452-8ee0-3f68-affc-2de9a1ede0b9",
      "secondary-type-ids": [],
      "title": "Stay the Night",
      "secondary-types": [],
      "first-release-date": "1986-11",
      "primary-type": "Single",
      "tags": [],
      "artist-credit": [
        {
          "artist": {
            "name": "Benjamin Orr",
            "tags": [
              {
                "count": 1,
                "name": "rock"
              }
            ],
            "genres": [
              {
                "name": "rock",
                "count": 1
              }
            ],
            "id": "94652393-67a7-4688-ad62-d30111c9ff0c",
            "disambiguation": "",
            "sort-name": "Orr, Benjamin",
            "aliases": []
          },
          "joinphrase": "",
          "name": "Benjamin Orr"
        }
      ],
      "aliases": [],
      "releases": [],
      "disambiguation": "",
      "id": "837255b5-8af2-498e-b365-665c5e83775b"
    }
  ],
  "end-area": {
    "name": "Atlanta",
    "sort-name": "Atlanta",
    "disambiguation": "",
    "id": "26e0e534-19ea-4645-bfb3-1aa4e83a4046"
  },
  "rating": {
    "votes-count": 0,
    "value": null
  },
  "tags": [
    {
      "name": "rock",
      "count": 1
    }
  ]
}
"""
