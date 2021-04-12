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
import com.squareup.moshi.Moshi
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

public class RecordingTest {
  private lateinit var moshi: Moshi

  @Before
  public fun setup() {
    moshi = theBrainzMoshi
  }

  @Test
  public fun `test parse recording all inc`() {
    moshi.adapter(Recording::class.java).fromJson(recordingAllIncJson)?.run {
      expect(annotation).toBeEmpty()
      expect(genres).toBeEmpty()
      expect(disambiguation).toBe("video")
      expect(id).toBe("fcbcdc39-8851-4efc-a02a-ab0e13be224f")
      expect(title).toBe("LAST ANGEL")
      expect(artistCredit.size).toBe(2)
      expect(artistCredit[0].name).toBe("倖田來未")
      expect(artistCredit[0].joinphrase).toBe(" feat. ")
      expect(artistCredit[0].artist.sortName).toBe("Koda, Kumi")
      expect(artistCredit[0].artist.aliases).toHaveSize(9)
      expect(artistCredit[1].name).toBe("東方神起")
      expect(artistCredit[1].artist.aliases).toHaveSize(11)
      expect(artistCredit[1].artist.aliases[10].sortName).toBe("Dong Bang Shin Ki")
      expect(artistCredit[1].artist.genres).toHaveSize(4)
      expect(artistCredit[1].artist.genres[1].name).toBe("k-pop")
    } ?: fail("Recording is null")
  }
}

private const val recordingAllIncJson =
  """
{
  "annotation": null,
  "genres": [],
  "length": null,
  "aliases": [],
  "disambiguation": "video",
  "rating": {
    "votes-count": 0,
    "value": null
  },
  "tags": [],
  "id": "fcbcdc39-8851-4efc-a02a-ab0e13be224f",
  "video": true,
  "title": "LAST ANGEL",
  "artist-credit": [
    {
      "name": "倖田來未",
      "joinphrase": " feat. ",
      "artist": {
        "name": "倖田來未",
        "sort-name": "Koda, Kumi",
        "genres": [],
        "id": "455641ea-fff4-49f6-8fb4-49f961d8f1ac",
        "tags": [
          {
            "name": "jpop",
            "count": 0
          }
        ],
        "disambiguation": "",
        "aliases": [
          {
            "locale": null,
            "primary": null,
            "type-id": null,
            "name": "Koda Kumi",
            "sort-name": "Koda Kumi",
            "type": null,
            "begin": null,
            "ended": false,
            "end": null
          },
          {
            "type": null,
            "begin": null,
            "ended": false,
            "end": null,
            "locale": null,
            "type-id": null,
            "primary": null,
            "sort-name": "Kouda Kumi",
            "name": "Kouda Kumi"
          },
          {
            "type": "Artist name",
            "begin": null,
            "ended": false,
            "end": null,
            "locale": "en",
            "type-id": "894afba6-2816-3c24-8072-eadb66bd04bc",
            "primary": true,
            "sort-name": "Koda, Kumi",
            "name": "Kumi Koda"
          },
          {
            "type": null,
            "begin": null,
            "end": null,
            "ended": false,
            "locale": null,
            "type-id": null,
            "primary": null,
            "sort-name": "こうだ くみ",
            "name": "こうだ くみ"
          },
          {
            "sort-name": "こうだくみ",
            "name": "こうだくみ",
            "primary": null,
            "type-id": null,
            "locale": null,
            "end": null,
            "ended": false,
            "begin": null,
            "type": null
          },
          {
            "begin": null,
            "end": null,
            "ended": false,
            "type": null,
            "type-id": null,
            "primary": null,
            "sort-name": "倖田 来未",
            "name": "倖田 来未",
            "locale": null
          },
          {
            "locale": null,
            "type-id": null,
            "primary": null,
            "sort-name": "倖田　來未",
            "name": "倖田　來未",
            "type": null,
            "begin": null,
            "ended": false,
            "end": null
          },
          {
            "name": "倖田來未",
            "sort-name": "こうだ くみ",
            "primary": true,
            "type-id": "894afba6-2816-3c24-8072-eadb66bd04bc",
            "locale": "ja",
            "end": null,
            "ended": false,
            "begin": null,
            "type": "Artist name"
          },
          {
            "type": "Legal name",
            "begin": null,
            "end": null,
            "ended": false,
            "locale": "ja_JP",
            "primary": false,
            "type-id": "d4dcd0c0-b341-3612-a332-c0ce797b25cf",
            "name": "神田來未子",
            "sort-name": "Koda, Kumiko"
          }
        ]
      }
    },
    {
      "name": "東方神起",
      "joinphrase": "",
      "artist": {
        "disambiguation": "",
        "aliases": [
          {
            "name": "DBSK",
            "sort-name": "DBSK",
            "primary": false,
            "type-id": "894afba6-2816-3c24-8072-eadb66bd04bc",
            "locale": "en",
            "ended": false,
            "end": null,
            "begin": null,
            "type": "Artist name"
          },
          {
            "type-id": null,
            "primary": null,
            "sort-name": "Dong Bang Shin Gi",
            "name": "Dong Bang Shin Gi",
            "locale": null,
            "begin": null,
            "end": null,
            "ended": false,
            "type": null
          },
          {
            "type": null,
            "end": null,
            "ended": false,
            "begin": null,
            "locale": null,
            "sort-name": "Dong Bang Shin Ki",
            "name": "Dong Bang Shin Ki",
            "primary": null,
            "type-id": null
          },
          {
            "primary": null,
            "type-id": null,
            "sort-name": "Dong Bang Sin Gi",
            "name": "Dong Bang Sin Gi",
            "locale": null,
            "begin": null,
            "end": null,
            "ended": false,
            "type": null
          },
          {
            "type-id": "894afba6-2816-3c24-8072-eadb66bd04bc",
            "primary": true,
            "sort-name": "TVXQ",
            "name": "TVXQ",
            "locale": "en",
            "begin": null,
            "ended": false,
            "end": null,
            "type": "Artist name"
          },
          {
            "end": null,
            "ended": false,
            "begin": null,
            "type": "Artist name",
            "sort-name": "TVXQ!",
            "name": "TVXQ!",
            "type-id": "894afba6-2816-3c24-8072-eadb66bd04bc",
            "primary": false,
            "locale": "en"
          },
          {
            "locale": null,
            "name": "TVfXQ",
            "sort-name": "TVfXQ",
            "primary": null,
            "type-id": null,
            "type": null,
            "end": null,
            "ended": false,
            "begin": null
          },
          {
            "locale": "ja",
            "sort-name": "Tohoshinki",
            "name": "Tohoshinki",
            "type-id": "894afba6-2816-3c24-8072-eadb66bd04bc",
            "primary": false,
            "type": "Artist name",
            "ended": false,
            "end": null,
            "begin": null
          },
          {
            "type": null,
            "begin": null,
            "end": null,
            "ended": false,
            "locale": null,
            "primary": null,
            "type-id": null,
            "name": "Tvxq! (Dong Bang Shin Ki)",
            "sort-name": "Tvxq! (Dong Bang Shin Ki)"
          },
          {
            "locale": "ja",
            "sort-name": "とうほうしんき",
            "name": "東方神起",
            "type-id": "894afba6-2816-3c24-8072-eadb66bd04bc",
            "primary": true,
            "type": "Artist name",
            "ended": false,
            "end": null,
            "begin": null
          },
          {
            "locale": "ko",
            "name": "동방신기",
            "sort-name": "Dong Bang Shin Ki",
            "primary": true,
            "type-id": "894afba6-2816-3c24-8072-eadb66bd04bc",
            "type": "Artist name",
            "end": null,
            "ended": false,
            "begin": null
          }
        ],
        "id": "05cbaf37-6dc2-4f71-a0ce-d633447d90c3",
        "tags": [
          {
            "count": 1,
            "name": "ballad"
          },
          {
            "name": "cleanup script",
            "count": 1
          },
          {
            "count": 1,
            "name": "cpop"
          },
          {
            "count": 1,
            "name": "dbsk"
          },
          {
            "name": "jpop",
            "count": 1
          },
          {
            "name": "k-pop",
            "count": 1
          },
          {
            "count": 1,
            "name": "korean"
          },
          {
            "count": 1,
            "name": "kpop"
          },
          {
            "name": "mid-tempo",
            "count": 1
          },
          {
            "name": "pop",
            "count": 1
          },
          {
            "count": 1,
            "name": "rock"
          },
          {
            "count": 1,
            "name": "trot"
          }
        ],
        "name": "東方神起",
        "sort-name": "Tohoshinki",
        "genres": [
          {
            "count": 1,
            "name": "ballad"
          },
          {
            "name": "k-pop",
            "count": 1
          },
          {
            "count": 1,
            "name": "pop"
          },
          {
            "name": "rock",
            "count": 1
          }
        ]
      }
    }
  ]
}
"""
