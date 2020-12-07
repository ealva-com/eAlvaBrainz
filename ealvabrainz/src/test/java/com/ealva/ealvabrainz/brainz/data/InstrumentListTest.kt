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
import com.squareup.moshi.Moshi
import org.junit.Before
import org.junit.Test

public class InstrumentListTest {
  private lateinit var moshi: Moshi

  @Before
  public fun setup() {
    moshi = theMoshi
  }

  @Test
  public fun `test instrument list query result`() {
    moshi.adapter(InstrumentList::class.java).fromJson(noseQueryJson)?.run {
      expect(created).toBe("2020-02-18T15:06:51.403Z")
      expect(count).toBe(4)
      expect(offset).toBe(0)
      expect(instruments).toHaveSize(4)

      instruments[0].run {
        expect(id).toBe("3d082a7d-e8d9-4c7b-b8d0-513883a7d586")
        expect(type).toBe("Wind instrument")
        expect(name).toBe("nose whistle")
        expect(description).toContain("Humanatone")
        expect(score).toBe(100)
        expect(aliases).toHaveSize(7)
        aliases[0].run {
          expect(name).toBe("Nasenflöte")
          expect(primary).toBe(true)
          expect(sortName).toBe("Nasenflöte")
          expect(locale).toBe("de")
          expect(typeId).toBe("2322fc94-fbf3-3c09-b23c-aa5ec8d14fcd")
        }
        expect(aliases[1].name).toBe("Humanatone")
        expect(aliases[2].name).toBe("鼻ホイッスル")
        expect(aliases[2].type).toBe("Instrument name")
        expect(aliases[3].name).toBe("ninavile")
        expect(aliases[3].locale).toBe("et")
        expect(aliases[6].name).toBe("neusfluitje")
        expect(aliases[6].locale).toBe("nl")
      }

      instruments[3].run {
        expect(id).toBe("68064791-4108-4c97-812f-990c45d1ba0d")
        expect(name).toBe("nguru")
        expect(disambiguation).toBe("Māori taonga pūoro small vessel flute")
        expect(description).toContain("Small vessel flute made of wood, clay, bone")
        expect(tags).toHaveSize(9)
        expect(tags).toContain(
          listOf(
            Tag(name = "traditional", count = 1),
            Tag(count = 1, name = "stone"),
            Tag(count = 1, name = "māori"),
            Tag(count = 1, name = "vessel flute"),
            Tag(count = 1, name = "wood"),
            Tag(count = 1, name = "gourd"),
            Tag(count = 1, name = "clay"),
            Tag(count = 1, name = "taonga pūoro"),
            Tag(count = 1, name = "bone")
          )
        )
      }
    }
  }
}

@Suppress("MaxLineLength")
/**
 * https://musicbrainz.org/ws/2/instrument/?query=Nose&fmt=json
 */
private const val noseQueryJson =
  """
{
  "created": "2020-02-18T15:06:51.403Z",
  "count": 4,
  "offset": 0,
  "instruments": [
    {
      "id": "3d082a7d-e8d9-4c7b-b8d0-513883a7d586",
      "type": "Wind instrument",
      "type-id": "876464a8-e74f-3f40-9bd3-637d2b1743ae",
      "score": 100,
      "name": "nose whistle",
      "description": "The nose whistle (also known as the Humanatone) is a simple instrument played with the nose. The stream of air is directed over an edge in the instrument and the frequency of the notes produced is controlled by the volume of air.",
      "aliases": [
        {
          "sort-name": "Nasenflöte",
          "type-id": "2322fc94-fbf3-3c09-b23c-aa5ec8d14fcd",
          "name": "Nasenflöte",
          "locale": "de",
          "type": "Instrument name",
          "primary": true,
          "begin-date": null,
          "end-date": null
        },
        {
          "sort-name": "Humanatone",
          "name": "Humanatone",
          "locale": null,
          "type": null,
          "primary": null,
          "begin-date": null,
          "end-date": null
        },
        {
          "sort-name": "鼻ホイッスル",
          "type-id": "2322fc94-fbf3-3c09-b23c-aa5ec8d14fcd",
          "name": "鼻ホイッスル",
          "locale": "ja",
          "type": "Instrument name",
          "primary": true,
          "begin-date": null,
          "end-date": null
        },
        {
          "sort-name": "ninavile",
          "type-id": "2322fc94-fbf3-3c09-b23c-aa5ec8d14fcd",
          "name": "ninavile",
          "locale": "et",
          "type": "Instrument name",
          "primary": true,
          "begin-date": null,
          "end-date": null
        },
        {
          "sort-name": "nose whistle",
          "type-id": "2322fc94-fbf3-3c09-b23c-aa5ec8d14fcd",
          "name": "nose whistle",
          "locale": "en",
          "type": "Instrument name",
          "primary": true,
          "begin-date": null,
          "end-date": null
        },
        {
          "sort-name": "sifflet à nez",
          "type-id": "2322fc94-fbf3-3c09-b23c-aa5ec8d14fcd",
          "name": "sifflet à nez",
          "locale": "fr",
          "type": "Instrument name",
          "primary": true,
          "begin-date": null,
          "end-date": null
        },
        {
          "sort-name": "neusfluitje",
          "type-id": "2322fc94-fbf3-3c09-b23c-aa5ec8d14fcd",
          "name": "neusfluitje",
          "locale": "nl",
          "type": "Instrument name",
          "primary": true,
          "begin-date": null,
          "end-date": null
        }
      ]
    },
    {
      "id": "e2e7de25-20d5-4c3f-8a23-2b99d3e44730",
      "type": "Wind instrument",
      "type-id": "876464a8-e74f-3f40-9bd3-637d2b1743ae",
      "score": 99,
      "name": "nose flute",
      "description": "The nose flute is a flute played by the nose commonly found in countries in and around the Pacific.",
      "aliases": [
        {
          "sort-name": "Nasenflöte (traditionell)",
          "type-id": "2322fc94-fbf3-3c09-b23c-aa5ec8d14fcd",
          "name": "Nasenflöte (traditionell)",
          "locale": "de",
          "type": "Instrument name",
          "primary": true,
          "begin-date": null,
          "end-date": null
        },
        {
          "sort-name": "鼻笛",
          "type-id": "2322fc94-fbf3-3c09-b23c-aa5ec8d14fcd",
          "name": "鼻笛",
          "locale": "ja",
          "type": "Instrument name",
          "primary": true,
          "begin-date": null,
          "end-date": null
        },
        {
          "sort-name": "ninaflööt",
          "type-id": "2322fc94-fbf3-3c09-b23c-aa5ec8d14fcd",
          "name": "ninaflööt",
          "locale": "et",
          "type": "Instrument name",
          "primary": true,
          "begin-date": null,
          "end-date": null
        },
        {
          "sort-name": "flûte à nez",
          "type-id": "2322fc94-fbf3-3c09-b23c-aa5ec8d14fcd",
          "name": "flûte à nez",
          "locale": "fr",
          "type": "Instrument name",
          "primary": true,
          "begin-date": null,
          "end-date": null
        },
        {
          "sort-name": "nose flute",
          "type-id": "2322fc94-fbf3-3c09-b23c-aa5ec8d14fcd",
          "name": "nose flute",
          "locale": "en",
          "type": "Instrument name",
          "primary": true,
          "begin-date": null,
          "end-date": null
        },
        {
          "sort-name": "nenähuilu",
          "type-id": "2322fc94-fbf3-3c09-b23c-aa5ec8d14fcd",
          "name": "nenähuilu",
          "locale": "fi",
          "type": "Instrument name",
          "primary": true,
          "begin-date": null,
          "end-date": null
        },
        {
          "sort-name": "flauta nasal",
          "type-id": "2322fc94-fbf3-3c09-b23c-aa5ec8d14fcd",
          "name": "flauta nasal",
          "locale": "es",
          "type": "Instrument name",
          "primary": true,
          "begin-date": null,
          "end-date": null
        },
        {
          "sort-name": "neusfluit",
          "type-id": "2322fc94-fbf3-3c09-b23c-aa5ec8d14fcd",
          "name": "neusfluit",
          "locale": "nl",
          "type": "Instrument name",
          "primary": true,
          "begin-date": null,
          "end-date": null
        }
      ]
    },
    {
      "id": "a4f10af3-d440-4ee7-9093-c60a121df41a",
      "type": "Wind instrument",
      "type-id": "876464a8-e74f-3f40-9bd3-637d2b1743ae",
      "score": 43,
      "name": "kōauau ponga ihu",
      "disambiguation": "Māori taonga pūoro tiny gourd nose flute",
      "description": "nose flute made of gourd with it two holes, while it is tiny, it has a huge sound",
      "aliases": [
        {
          "sort-name": "Pōngaihu",
          "name": "Pōngaihu",
          "locale": null,
          "type": null,
          "primary": null,
          "begin-date": null,
          "end-date": null
        },
        {
          "sort-name": "köauau ponga ihu",
          "type-id": "7d5ef40f-4856-3000-8667-aa13b9db547d",
          "name": "köauau ponga ihu",
          "locale": null,
          "type": "Search hint",
          "primary": null,
          "begin-date": null,
          "end-date": null
        },
        {
          "sort-name": "koauau ponga ihu",
          "type-id": "7d5ef40f-4856-3000-8667-aa13b9db547d",
          "name": "koauau ponga ihu",
          "locale": null,
          "type": "Search hint",
          "primary": null,
          "begin-date": null,
          "end-date": null
        }
      ],
      "tags": [
        {
          "count": 1,
          "name": "traditional"
        },
        {
          "count": 1,
          "name": "flute"
        },
        {
          "count": 1,
          "name": "māori"
        },
        {
          "count": 1,
          "name": "gourd"
        },
        {
          "count": 1,
          "name": "taonga pūoro"
        },
        {
          "count": 1,
          "name": "nose flute"
        },
        {
          "count": 1,
          "name": "kōauau"
        },
        {
          "count": 1,
          "name": "needs image"
        }
      ]
    },
    {
      "id": "68064791-4108-4c97-812f-990c45d1ba0d",
      "type": "Wind instrument",
      "type-id": "876464a8-e74f-3f40-9bd3-637d2b1743ae",
      "score": 21,
      "name": "nguru",
      "disambiguation": "Māori taonga pūoro small vessel flute",
      "description": "Small vessel flute made of wood, clay, bone or soft stone like soapstone, it has 4 holes and is played with the nose or mouth.",
      "tags": [
        {
          "count": 1,
          "name": "traditional"
        },
        {
          "count": 1,
          "name": "stone"
        },
        {
          "count": 1,
          "name": "māori"
        },
        {
          "count": 1,
          "name": "vessel flute"
        },
        {
          "count": 1,
          "name": "wood"
        },
        {
          "count": 1,
          "name": "gourd"
        },
        {
          "count": 1,
          "name": "clay"
        },
        {
          "count": 1,
          "name": "taonga pūoro"
        },
        {
          "count": 1,
          "name": "bone"
        }
      ]
    }
  ]
}
"""
