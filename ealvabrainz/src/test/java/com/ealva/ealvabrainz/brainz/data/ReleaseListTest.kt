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

internal class ReleaseListTest {

  private lateinit var moshi: Moshi

  @Before
  fun setup() {
    moshi = theMoshi
  }

  @Test
  fun `test full release list`() {
    moshi.adapter(ReleaseList::class.java).lenient().fromJson(blackCrowesLions)?.let { list ->
      // TODO beef-up tests
      expect(list.count).toBe(7)
    } ?: fail("ReleaseList is null")
  }

}

private const val blackCrowesLions = """
{
  "created": "2020-02-03T22:15:29.616Z",
  "count": 7,
  "offset": 0,
  "releases": [
    {
      "id": "ca2866c0-e204-4b0e-8fd2-00823863e2b2",
      "score": 100,
      "count": 1,
      "title": "Lions",
      "status": "Official",
      "packaging": {
        "id": "ec27701a-4a22-37f4-bfac-6616e0f9750a",
        "name": "Jewel Case"
      },
      "text-representation": {
        "language": "eng",
        "script": "Latn"
      },
      "artist-credit": [
        {
          "name": "The Black Crowes",
          "artist": {
            "id": "02ceff75-7363-493e-a78d-912dc86c7460",
            "name": "The Black Crowes",
            "sort-name": "Black Crowes, The"
          }
        }
      ],
      "release-group": {
        "id": "dffd04c1-7ba4-3904-ac3a-e33de148a25e",
        "type-id": "f529b476-6e62-324f-b0aa-1f3e33d313fc",
        "title": "Lions",
        "primary-type": "Album"
      },
      "date": "2001-05-08",
      "country": "CA",
      "release-events": [
        {
          "date": "2001-05-08",
          "area": {
            "id": "71bbafaa-e825-3e15-8ca9-017dcad1748b",
            "name": "Canada",
            "sort-name": "Canada",
            "iso-3166-1-codes": [
              "CA"
            ]
          }
        }
      ],
      "barcode": "638812709127",
      "label-info": [
        {
          "catalog-number": "63881-27091-2",
          "label": {
            "id": "dc2f5993-7a3d-4c59-bba0-0a77bf9d7416",
            "name": "V2"
          }
        }
      ],
      "track-count": 13,
      "media": [
        {
          "format": "CD",
          "disc-count": 0,
          "track-count": 13
        }
      ]
    },
    {
      "id": "c1c263a6-a5e7-4ae5-8718-1b0690bceb34",
      "score": 100,
      "count": 1,
      "title": "Lions",
      "artist-credit": [
        {
          "name": "The Black Crowes",
          "artist": {
            "id": "02ceff75-7363-493e-a78d-912dc86c7460",
            "name": "The Black Crowes",
            "sort-name": "Black Crowes, The"
          }
        }
      ],
      "release-group": {
        "id": "dffd04c1-7ba4-3904-ac3a-e33de148a25e",
        "type-id": "f529b476-6e62-324f-b0aa-1f3e33d313fc",
        "title": "Lions",
        "primary-type": "Album"
      },
      "barcode": "5033197156729",
      "label-info": [
        {
          "catalog-number": "VVR1015672",
          "label": {
            "id": "dc2f5993-7a3d-4c59-bba0-0a77bf9d7416",
            "name": "V2"
          }
        }
      ],
      "track-count": 13,
      "media": [
        {
          "format": "CD",
          "disc-count": 0,
          "track-count": 13
        }
      ]
    },
    {
      "id": "df06aa4c-7ecb-473c-9953-16d3af032bbf",
      "score": 100,
      "count": 1,
      "title": "Lions",
      "artist-credit": [
        {
          "name": "The Black Crowes",
          "artist": {
            "id": "02ceff75-7363-493e-a78d-912dc86c7460",
            "name": "The Black Crowes",
            "sort-name": "Black Crowes, The"
          }
        }
      ],
      "release-group": {
        "id": "dffd04c1-7ba4-3904-ac3a-e33de148a25e",
        "type-id": "f529b476-6e62-324f-b0aa-1f3e33d313fc",
        "title": "Lions",
        "primary-type": "Album"
      },
      "barcode": "5033197156729",
      "track-count": 13,
      "media": [
        {
          "format": "CD",
          "disc-count": 0,
          "track-count": 13
        }
      ]
    },
    {
      "id": "6bde6b11-86ea-4661-9c5c-3d72bc5a9ecb",
      "score": 100,
      "count": 1,
      "title": "Lions",
      "status": "Official",
      "packaging": {
        "id": "ec27701a-4a22-37f4-bfac-6616e0f9750a",
        "name": "Jewel Case"
      },
      "text-representation": {
        "language": "eng",
        "script": "Latn"
      },
      "artist-credit": [
        {
          "name": "The Black Crowes",
          "artist": {
            "id": "02ceff75-7363-493e-a78d-912dc86c7460",
            "name": "The Black Crowes",
            "sort-name": "Black Crowes, The"
          }
        }
      ],
      "release-group": {
        "id": "dffd04c1-7ba4-3904-ac3a-e33de148a25e",
        "type-id": "f529b476-6e62-324f-b0aa-1f3e33d313fc",
        "title": "Lions",
        "primary-type": "Album"
      },
      "date": "2001-04-25",
      "country": "JP",
      "release-events": [
        {
          "date": "2001-04-25",
          "area": {
            "id": "2db42837-c832-3c27-b4a3-08198f75693c",
            "name": "Japan",
            "sort-name": "Japan",
            "iso-3166-1-codes": [
              "JP"
            ]
          }
        }
      ],
      "barcode": "4520227009724",
      "asin": "B00005HY92",
      "label-info": [
        {
          "catalog-number": "V2CI 97",
          "label": {
            "id": "2efdd24a-5411-4124-98ee-c1e755e6d5cb",
            "name": "V2 Records Japan"
          }
        }
      ],
      "track-count": 14,
      "media": [
        {
          "format": "CD",
          "disc-count": 1,
          "track-count": 14
        }
      ]
    },
    {
      "id": "e7cee9e8-c7de-4cd2-9851-9da48b6a0a2c",
      "score": 100,
      "count": 1,
      "title": "Lions",
      "status": "Official",
      "packaging": {
        "id": "ec27701a-4a22-37f4-bfac-6616e0f9750a",
        "name": "Jewel Case"
      },
      "text-representation": {
        "language": "eng",
        "script": "Latn"
      },
      "artist-credit": [
        {
          "name": "The Black Crowes",
          "artist": {
            "id": "02ceff75-7363-493e-a78d-912dc86c7460",
            "name": "The Black Crowes",
            "sort-name": "Black Crowes, The"
          }
        }
      ],
      "release-group": {
        "id": "dffd04c1-7ba4-3904-ac3a-e33de148a25e",
        "type-id": "f529b476-6e62-324f-b0aa-1f3e33d313fc",
        "title": "Lions",
        "primary-type": "Album"
      },
      "date": "2001",
      "country": "AU",
      "release-events": [
        {
          "date": "2001",
          "area": {
            "id": "106e0bec-b638-3b37-b731-f53d507dc00e",
            "name": "Australia",
            "sort-name": "Australia",
            "iso-3166-1-codes": [
              "AU"
            ]
          }
        }
      ],
      "barcode": "9326382003318",
      "label-info": [
        {
          "catalog-number": "VVR1015678",
          "label": {
            "id": "dc2f5993-7a3d-4c59-bba0-0a77bf9d7416",
            "name": "V2"
          }
        }
      ],
      "track-count": 13,
      "media": [
        {
          "format": "CD",
          "disc-count": 1,
          "track-count": 13
        }
      ]
    },
    {
      "id": "1ab70dbf-ea38-39ca-aca7-ed7e03f78f1d",
      "score": 100,
      "count": 1,
      "title": "Lions",
      "status": "Official",
      "text-representation": {
        "language": "eng",
        "script": "Latn"
      },
      "artist-credit": [
        {
          "name": "The Black Crowes",
          "artist": {
            "id": "02ceff75-7363-493e-a78d-912dc86c7460",
            "name": "The Black Crowes",
            "sort-name": "Black Crowes, The"
          }
        }
      ],
      "release-group": {
        "id": "dffd04c1-7ba4-3904-ac3a-e33de148a25e",
        "type-id": "f529b476-6e62-324f-b0aa-1f3e33d313fc",
        "title": "Lions",
        "primary-type": "Album"
      },
      "track-count": 13,
      "media": [
        {
          "format": "Vinyl",
          "disc-count": 0,
          "track-count": 13
        }
      ]
    },
    {
      "id": "18c0e808-8cb0-422a-82fc-eb7015bd755b",
      "score": 100,
      "count": 1,
      "title": "Lions",
      "status": "Official",
      "packaging": {
        "id": "ec27701a-4a22-37f4-bfac-6616e0f9750a",
        "name": "Jewel Case"
      },
      "text-representation": {
        "language": "eng",
        "script": "Latn"
      },
      "artist-credit": [
        {
          "name": "The Black Crowes",
          "artist": {
            "id": "02ceff75-7363-493e-a78d-912dc86c7460",
            "name": "The Black Crowes",
            "sort-name": "Black Crowes, The"
          }
        }
      ],
      "release-group": {
        "id": "dffd04c1-7ba4-3904-ac3a-e33de148a25e",
        "type-id": "f529b476-6e62-324f-b0aa-1f3e33d313fc",
        "title": "Lions",
        "primary-type": "Album"
      },
      "date": "2001-05-08",
      "country": "US",
      "release-events": [
        {
          "date": "2001-05-08",
          "area": {
            "id": "489ce91b-6658-3307-9877-795b68554c98",
            "name": "United States",
            "sort-name": "United States",
            "iso-3166-1-codes": [
              "US"
            ]
          }
        }
      ],
      "barcode": "638812709127",
      "asin": "B00005B19O",
      "label-info": [
        {
          "catalog-number": "63881-27091-2",
          "label": {
            "id": "dc2f5993-7a3d-4c59-bba0-0a77bf9d7416",
            "name": "V2"
          }
        }
      ],
      "track-count": 13,
      "media": [
        {
          "format": "Enhanced CD",
          "disc-count": 4,
          "track-count": 13
        }
      ]
    }
  ]
}
"""
