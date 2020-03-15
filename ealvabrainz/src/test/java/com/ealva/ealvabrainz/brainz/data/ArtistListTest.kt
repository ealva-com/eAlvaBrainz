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

class ArtistListTest {
  private lateinit var moshi: Moshi

  @Before
  fun setup() {
    moshi = theMoshi
  }

  @Test
  fun `test artist list query result`() {
    moshi.adapter(ArtistList::class.java).fromJson(johnLennonJson)?.run {
      expect(created).toBe("2020-02-10T23:01:20.201Z")
      expect(count).toBe(3)
      expect(offset).toBe(0)
      expect(artists).toHaveSize(3)
      artists[0].run {
        expect(id).toBe("4d5447d7-c61c-4120-ba1b-d7f471d385b9")
        expect(artistType).toBe(ArtistType.Person)
        expect(typeId).toBe("b6e035f4-3ce9-331c-97df-83397230b0df")
        expect(score).toBe(100)
        expect(sortName).toBe("Lennon, John")
        expect(gender).toBe("male")
        expect(country).toBe("GB")
        expect(area.id).toBe("8a754a16-0027-3a29-b6d7-2b40ea0481ed")
        expect(area.type).toBe("Country")
        expect(area.typeId).toBe("06dd0ae4-8c74-30bb-b43d-95dcedf961de")
        expect(area.lifeSpan.ended).toBe(false)
        expect(area.name).toBe("United Kingdom")
        expect(beginArea.type).toBe("City")
        expect(beginArea.name).toBe("Liverpool")
        expect(endArea.name).toBe("Upper West Side")
        expect(endArea.type).toBe("District")
        expect(disambiguation).toBe("The Beatles")
        expect(ipis).toHaveSize(2)
        expect(ipis).toContain(listOf("00017798450","00273545259"))
        expect(lifeSpan.begin).toBe("1940-10-09")
        expect(lifeSpan.ended).toBe(true)
        expect(lifeSpan.end).toBe("1980-12-08")
        expect(aliases).toHaveSize(9)
        aliases[0].run {
          expect(name).toBe("ジョン・レノン")
          expect(sortName).toBe("ジョン・レノン")
          expect(type).toBe("Artist name")
        }
        expect(tags).toHaveSize(20)
        expect(tags.map { it.name }).toContain(listOf(
          "rock",
          "pop",
          "folk",
          "experimental",
          "british",
          "singer-songwriter",
          "uk",
          "britannique",
          "classic rock",
          "pop rock",
          "english",
          "united kingdom",
          "classic pop and rock",
          "peace",
          "death by murder",
          "death by gun",
          "singer/songwriter",
          "rock & roll",
          "wifebeater",
          "murdered"
        ))
      }
    } ?: fail("ArtistLists is null")
  }
}

private const val johnLennonJson = """
{
  "created": "2020-02-10T23:01:20.201Z",
  "count": 3,
  "offset": 0,
  "artists": [
    {
      "id": "4d5447d7-c61c-4120-ba1b-d7f471d385b9",
      "type": "Person",
      "type-id": "b6e035f4-3ce9-331c-97df-83397230b0df",
      "score": 100,
      "name": "John Lennon",
      "sort-name": "Lennon, John",
      "gender": "male",
      "country": "GB",
      "area": {
        "id": "8a754a16-0027-3a29-b6d7-2b40ea0481ed",
        "type": "Country",
        "type-id": "06dd0ae4-8c74-30bb-b43d-95dcedf961de",
        "name": "United Kingdom",
        "sort-name": "United Kingdom",
        "life-span": {
          "ended": null
        }
      },
      "begin-area": {
        "id": "c249c30e-88ab-4b2f-a745-96a25bd7afee",
        "type": "City",
        "type-id": "6fd8f29a-3d0a-32fc-980d-ea697b69da78",
        "name": "Liverpool",
        "sort-name": "Liverpool",
        "life-span": {
          "ended": null
        }
      },
      "end-area": {
        "id": "aaef6ccc-52fb-4add-ae99-aded66a0b170",
        "type": "District",
        "type-id": "84039871-5e47-38ca-a66a-45e512c8290f",
        "name": "Upper West Side",
        "sort-name": "Upper West Side",
        "life-span": {
          "ended": null
        }
      },
      "disambiguation": "The Beatles",
      "ipis": [
        "00017798450",
        "00273545259"
      ],
      "isnis": [
        "0000000121174585"
      ],
      "life-span": {
        "begin": "1940-10-09",
        "end": "1980-12-08",
        "ended": true
      },
      "aliases": [
        {
          "sort-name": "ジョン・レノン",
          "type-id": "894afba6-2816-3c24-8072-eadb66bd04bc",
          "name": "ジョン・レノン",
          "locale": "ja",
          "type": "Artist name",
          "primary": true,
          "begin-date": null,
          "end-date": null
        },
        {
          "sort-name": "Джон",
          "name": "Джон",
          "locale": "ru",
          "type": null,
          "primary": null,
          "begin-date": null,
          "end-date": null
        },
        {
          "sort-name": "Lennon, John",
          "type-id": "1937e404-b981-3cb7-8151-4c86ebfc8d8e",
          "name": "Lennon, John",
          "locale": null,
          "type": "Search hint",
          "primary": null,
          "begin-date": null,
          "end-date": null
        },
        {
          "sort-name": "Lennon",
          "name": "Lennon",
          "locale": null,
          "type": null,
          "primary": null,
          "begin-date": null,
          "end-date": null
        },
        {
          "sort-name": "J. Lennon",
          "name": "J. Lennon",
          "locale": null,
          "type": null,
          "primary": null,
          "begin-date": null,
          "end-date": null
        },
        {
          "sort-name": "Ono Lennon, John Winston",
          "type-id": "d4dcd0c0-b341-3612-a332-c0ce797b25cf",
          "name": "John Winston Ono Lennon",
          "locale": null,
          "type": "Legal name",
          "primary": null,
          "begin-date": "1969",
          "end-date": null
        },
        {
          "sort-name": "Lennon, John Winston",
          "type-id": "d4dcd0c0-b341-3612-a332-c0ce797b25cf",
          "name": "John Winston Lennon",
          "locale": null,
          "type": "Legal name",
          "primary": null,
          "begin-date": null,
          "end-date": "1969"
        },
        {
          "sort-name": "Леннон, Джон",
          "type-id": "894afba6-2816-3c24-8072-eadb66bd04bc",
          "name": "Джон Леннон",
          "locale": "ru",
          "type": "Artist name",
          "primary": null,
          "begin-date": null,
          "end-date": null
        },
        {
          "sort-name": "Lennon, John",
          "type-id": "894afba6-2816-3c24-8072-eadb66bd04bc",
          "name": "John Lennon",
          "locale": "en",
          "type": "Artist name",
          "primary": true,
          "begin-date": null,
          "end-date": null
        }
      ],
      "tags": [
        {
          "count": 6,
          "name": "rock"
        },
        {
          "count": 7,
          "name": "pop"
        },
        {
          "count": 1,
          "name": "folk"
        },
        {
          "count": 5,
          "name": "experimental"
        },
        {
          "count": 3,
          "name": "british"
        },
        {
          "count": 1,
          "name": "singer-songwriter"
        },
        {
          "count": 1,
          "name": "uk"
        },
        {
          "count": -1,
          "name": "britannique"
        },
        {
          "count": 1,
          "name": "classic rock"
        },
        {
          "count": 1,
          "name": "pop rock"
        },
        {
          "count": 0,
          "name": "english"
        },
        {
          "count": 0,
          "name": "united kingdom"
        },
        {
          "count": -1,
          "name": "classic pop and rock"
        },
        {
          "count": 1,
          "name": "peace"
        },
        {
          "count": -1,
          "name": "death by murder"
        },
        {
          "count": -1,
          "name": "death by gun"
        },
        {
          "count": 1,
          "name": "singer/songwriter"
        },
        {
          "count": 1,
          "name": "rock & roll"
        },
        {
          "count": 0,
          "name": "wifebeater"
        },
        {
          "count": 0,
          "name": "murdered"
        }
      ]
    },
    {
      "id": "abd66a77-afb3-4d97-9ecf-8975560dc1f3",
      "type": "Person",
      "type-id": "b6e035f4-3ce9-331c-97df-83397230b0df",
      "score": 66,
      "name": "John Lennon Brenner",
      "sort-name": "Brenner, John Lennon",
      "gender": "male",
      "begin-area": {
        "id": "1f40c6e1-47ba-4e35-996f-fe6ee5840e62",
        "type": "City",
        "type-id": "6fd8f29a-3d0a-32fc-980d-ea697b69da78",
        "name": "Los Angeles",
        "sort-name": "Los Angeles",
        "life-span": {
          "ended": null
        }
      },
      "disambiguation": "Scientist. son of Steve Brenner",
      "life-span": {
        "ended": null
      },
      "aliases": [
        {
          "sort-name": "Dr. John Lennon Brenner",
          "type-id": "1937e404-b981-3cb7-8151-4c86ebfc8d8e",
          "name": "Dr. John Lennon Brenner",
          "locale": null,
          "type": "Search hint",
          "primary": null,
          "begin-date": null,
          "end-date": null
        },
        {
          "sort-name": "John L. Brenner",
          "type-id": "894afba6-2816-3c24-8072-eadb66bd04bc",
          "name": "John L. Brenner",
          "locale": null,
          "type": "Artist name",
          "primary": null,
          "begin-date": null,
          "end-date": null
        },
        {
          "sort-name": "J.L. Brenner",
          "type-id": "894afba6-2816-3c24-8072-eadb66bd04bc",
          "name": "J.L. Brenner",
          "locale": null,
          "type": "Artist name",
          "primary": null,
          "begin-date": null,
          "end-date": null
        },
        {
          "sort-name": "Brenner, John Lennon",
          "type-id": "d4dcd0c0-b341-3612-a332-c0ce797b25cf",
          "name": "John Lennon Brenner",
          "locale": null,
          "type": "Legal name",
          "primary": null,
          "begin-date": null,
          "end-date": null
        }
      ]
    },
    {
      "id": "e72e258a-dc6a-4795-b9a1-ba67157de782",
      "type": "Group",
      "type-id": "e431f5f6-b5d2-343d-8b36-72607fffb74b",
      "score": 61,
      "name": "John Lennon Rifle Club",
      "sort-name": "John Lennon Rifle Club",
      "disambiguation": "Belgian",
      "life-span": {
        "ended": null
      }
    }
  ]
}
"""
