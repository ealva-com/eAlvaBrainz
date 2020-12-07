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

import com.nhaarman.expect.ListMatcher
import com.nhaarman.expect.expect
import com.nhaarman.expect.fail
import com.squareup.moshi.Moshi
import org.junit.Before
import org.junit.Test

public fun <T> ListMatcher<T>.toContain(expectedList: List<T>, message: (() -> Any?)? = null) {
  val theActual = actual ?: fail(
    "Expected value to contain $expectedList, but the actual value was null.",
    message
  )
  if (!theActual.containsAll(expectedList)) {
    fail("Expected $actual to contain $expectedList", message)
  }
}

public class RecordingListTest {
  private lateinit var moshi: Moshi

  @Before
  public fun setup() {
    moshi = theMoshi
  }

  @Test
  public fun `test recording list query result`() {
    moshi.adapter(RecordingList::class.java).fromJson(recordingListJson)?.run {
      expect(created).toBe("2020-02-10T17:06:49.087Z")
      expect(count).toBe(1)
      expect(recordings).toHaveSize(1)
      recordings[0].run {
        expect(id).toBe("026fa041-3917-4c73-9079-ed16e36f20f8")
        expect(score).toBe(100)
        expect(length).toBe(178583)
        expect(artistCredit).toHaveSize(1)
        artistCredit[0].run {
          expect(name).toBe("Dua Lipa")
          expect(artist.id).toBe("6f1a58bf-9b1b-49cf-a44a-6cefad7ae04f")
          expect(artist.sortName).toBe("Lipa, Dua")
        }
        expect(isrcs).toContain(listOf("GBAHT1600302", "DEUM71601954", "GBAHT1600331"))
        expect(tags.map { it.name }).toContain(
          listOf("pop", "electropop", "dance-pop", "contemporary r&b")
        )
        expect(releases).toHaveSize(20)
        releases[2].run {
          expect(id).toBe("1a343c09-e2e5-497f-a6d0-3ca62a17fc4f")
          expect(disambiguation).toBe("complete edition")
          expect(releaseGroup.id).toBe("93747376-b698-4610-a023-d256de075460")
          expect(releaseGroup.thePrimaryTypeId).toBe("f529b476-6e62-324f-b0aa-1f3e33d313fc")
          expect(releaseGroup.primaryType).toBe("Album")
        }
        releases[8].run {
          expect(id).toBe("0806229a-8598-404d-812b-0267e956b9e5")
          expect(releaseGroup.thePrimaryTypeId).toBe("f529b476-6e62-324f-b0aa-1f3e33d313fc")
          expect(releaseGroup.primaryType).toBe("Album")
          expect(releaseEvents).toHaveSize(1)
          releaseEvents[0].run {
            expect(date).toBe("2018-10-19")
            expect(area.id).toBe("525d4e18-3d00-31b9-a58b-a146a916de8f")
            expect(area.name).toBe("[Worldwide]")
            expect(area.iso31661Codes).toHaveSize(1)
            expect(area.iso31661Codes[0]).toBe("XW")
          }
          expect(media).toHaveSize(1)
          media[0].run {
            expect(format).toBe("Digital Media")
            expect(theTracks).toHaveSize(1)
          }
        }
        releases[19].run {
          expect(id).toBe("383be31c-37a0-4e08-8cda-cbcbbc587ae5")
          expect(releaseEvents).toHaveSize(1)
          releaseEvents[0].run {
            expect(date).toBe("2016-08-26")
          }
          expect(media).toHaveSize(1)
          media[0].run {
            expect(position).toBe(1)
            expect(trackCount).toBe(1)
            expect(trackOffset).toBe(0)
            expect(theTracks).toHaveSize(1)
            theTracks[0].run {
              expect(id).toBe("0ef6e647-4aeb-438e-8c8a-50c22c511203")
              expect(number).toBe("1")
              expect(length).toBe(179000)
            }
          }
        }
      }
    } ?: fail("RecordingList is null")
  }
}

/**
 * http://musicbrainz.org/ws/2/recording/?query=isrc:GBAHT1600302&fmt=json
 */
private const val recordingListJson =
  """
{
  "created": "2020-02-10T17:06:49.087Z",
  "count": 1,
  "offset": 0,
  "recordings": [
    {
      "id": "026fa041-3917-4c73-9079-ed16e36f20f8",
      "score": 100,
      "title": "Blow Your Mind (Mwah)",
      "length": 178583,
      "disambiguation": "explicit",
      "video": null,
      "artist-credit": [
        {
          "name": "Dua Lipa",
          "artist": {
            "id": "6f1a58bf-9b1b-49cf-a44a-6cefad7ae04f",
            "name": "Dua Lipa",
            "sort-name": "Lipa, Dua"
          }
        }
      ],
      "releases": [
        {
          "id": "1529430a-5e3c-4145-acfe-33f6ea4b09f9",
          "count": 1,
          "title": "Dua Lipa",
          "status": "Official",
          "release-group": {
            "id": "93747376-b698-4610-a023-d256de075460",
            "type-id": "f529b476-6e62-324f-b0aa-1f3e33d313fc",
            "title": "Dua Lipa",
            "primary-type": "Album"
          },
          "date": "2017",
          "country": "RU",
          "release-events": [
            {
              "date": "2017",
              "area": {
                "id": "1f1fc3a4-9500-39b8-9f10-f0a465557eef",
                "name": "Russia",
                "sort-name": "Russia",
                "iso-3166-1-codes": [
                  "RU"
                ]
              }
            }
          ],
          "track-count": 12,
          "media": [
            {
              "position": 1,
              "format": "CD",
              "track": [
                {
                  "id": "a9072808-3b27-43f6-a9c8-cd294e82186c",
                  "number": "6",
                  "title": "Blow Your Mind (Mwah)",
                  "length": 179000
                }
              ],
              "track-count": 12,
              "track-offset": 5
            }
          ]
        },
        {
          "id": "74a67a66-5486-45bd-978d-96a23856c99e",
          "count": 1,
          "title": "Dua Lipa",
          "status": "Official",
          "disambiguation": "deluxe edition",
          "release-group": {
            "id": "93747376-b698-4610-a023-d256de075460",
            "type-id": "f529b476-6e62-324f-b0aa-1f3e33d313fc",
            "title": "Dua Lipa",
            "primary-type": "Album"
          },
          "date": "2017-06-02",
          "country": "XE",
          "release-events": [
            {
              "date": "2017-06-02",
              "area": {
                "id": "89a675c2-3e37-3518-b83c-418bad59a85a",
                "name": "Europe",
                "sort-name": "Europe",
                "iso-3166-1-codes": [
                  "XE"
                ]
              }
            }
          ],
          "track-count": 17,
          "media": [
            {
              "position": 1,
              "format": "CD",
              "track": [
                {
                  "id": "697b678d-48c6-4791-bd93-4e4793bd59cf",
                  "number": "6",
                  "title": "Blow Your Mind (Mwah)",
                  "length": 179386
                }
              ],
              "track-count": 17,
              "track-offset": 5
            }
          ]
        },
        {
          "id": "1a343c09-e2e5-497f-a6d0-3ca62a17fc4f",
          "count": 2,
          "title": "Dua Lipa",
          "status": "Official",
          "disambiguation": "complete edition",
          "release-group": {
            "id": "93747376-b698-4610-a023-d256de075460",
            "type-id": "f529b476-6e62-324f-b0aa-1f3e33d313fc",
            "title": "Dua Lipa",
            "primary-type": "Album"
          },
          "date": "2018-10-19",
          "country": "XE",
          "release-events": [
            {
              "date": "2018-10-19",
              "area": {
                "id": "89a675c2-3e37-3518-b83c-418bad59a85a",
                "name": "Europe",
                "sort-name": "Europe",
                "iso-3166-1-codes": [
                  "XE"
                ]
              }
            }
          ],
          "track-count": 25,
          "media": [
            {
              "position": 1,
              "format": "CD",
              "track": [
                {
                  "id": "6e522793-ff89-4e9c-b07a-cd1914eccee4",
                  "number": "6",
                  "title": "Blow Your Mind (Mwah)",
                  "length": 179000
                }
              ],
              "track-count": 17,
              "track-offset": 5
            }
          ]
        },
        {
          "id": "e498c49b-6176-4221-ad6e-497d9a49516d",
          "count": 2,
          "title": "Dua Lipa (complete edition)",
          "status": "Official",
          "release-group": {
            "id": "93747376-b698-4610-a023-d256de075460",
            "type-id": "f529b476-6e62-324f-b0aa-1f3e33d313fc",
            "title": "Dua Lipa",
            "primary-type": "Album"
          },
          "date": "2018-10-19",
          "country": "US",
          "release-events": [
            {
              "date": "2018-10-19",
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
          "track-count": 25,
          "media": [
            {
              "position": 1,
              "format": "CD",
              "track": [
                {
                  "id": "a26b964a-837b-460d-88a2-19314b90dca8",
                  "number": "6",
                  "title": "Blow Your Mind (Mwah)",
                  "length": 178583
                }
              ],
              "track-count": 17,
              "track-offset": 5
            }
          ]
        },
        {
          "id": "6ec1f854-f81b-40a0-8daf-07cac9ff026b",
          "count": 1,
          "title": "Dua Lipa (deluxe edition)",
          "status": "Official",
          "release-group": {
            "id": "93747376-b698-4610-a023-d256de075460",
            "type-id": "f529b476-6e62-324f-b0aa-1f3e33d313fc",
            "title": "Dua Lipa",
            "primary-type": "Album"
          },
          "date": "2017-06-02",
          "country": "GB",
          "release-events": [
            {
              "date": "2017-06-02",
              "area": {
                "id": "8a754a16-0027-3a29-b6d7-2b40ea0481ed",
                "name": "United Kingdom",
                "sort-name": "United Kingdom",
                "iso-3166-1-codes": [
                  "GB"
                ]
              }
            }
          ],
          "track-count": 17,
          "media": [
            {
              "position": 1,
              "format": "Digital Media",
              "track": [
                {
                  "id": "a2fcb379-7638-4f27-b2e4-5242a69ed09c",
                  "number": "6",
                  "title": "Blow Your Mind (Mwah)",
                  "length": 178000
                }
              ],
              "track-count": 17,
              "track-offset": 5
            }
          ]
        },
        {
          "id": "10b7ac15-19e8-44e4-830a-b22a4c04cb4e",
          "count": 1,
          "title": "Dua Lipa",
          "status": "Official",
          "disambiguation": "Japan deluxe edition",
          "release-group": {
            "id": "93747376-b698-4610-a023-d256de075460",
            "type-id": "f529b476-6e62-324f-b0aa-1f3e33d313fc",
            "title": "Dua Lipa",
            "primary-type": "Album"
          },
          "date": "2017-06-02",
          "country": "JP",
          "release-events": [
            {
              "date": "2017-06-02",
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
          "track-count": 19,
          "media": [
            {
              "position": 1,
              "format": "CD",
              "track": [
                {
                  "id": "b8de8dc7-a84e-496a-8a07-a29b648ef674",
                  "number": "6",
                  "title": "Blow Your Mind (Mwah)",
                  "length": 178583
                }
              ],
              "track-count": 19,
              "track-offset": 5
            }
          ]
        },
        {
          "id": "0cc6afc1-931e-46ef-881e-7cd98c428793",
          "count": 1,
          "title": "Dua Lipa",
          "status": "Official",
          "disambiguation": "Germany deluxe edition",
          "release-group": {
            "id": "93747376-b698-4610-a023-d256de075460",
            "type-id": "f529b476-6e62-324f-b0aa-1f3e33d313fc",
            "title": "Dua Lipa",
            "primary-type": "Album"
          },
          "date": "2017-06-02",
          "country": "DE",
          "release-events": [
            {
              "date": "2017-06-02",
              "area": {
                "id": "85752fda-13c4-31a3-bee5-0e5cb1f51dad",
                "name": "Germany",
                "sort-name": "Germany",
                "iso-3166-1-codes": [
                  "DE"
                ]
              }
            }
          ],
          "track-count": 17,
          "media": [
            {
              "position": 1,
              "format": "Digital Media",
              "track": [
                {
                  "id": "d2b9b6fc-a2fd-42e0-b873-e61b00624c5c",
                  "number": "6",
                  "title": "Blow Your Mind (Mwah)",
                  "length": 178583
                }
              ],
              "track-count": 17,
              "track-offset": 5
            }
          ]
        },
        {
          "id": "a22cb9d6-bbb4-494b-8363-0835c1652351",
          "count": 1,
          "title": "Dua Lipa",
          "status": "Official",
          "release-group": {
            "id": "93747376-b698-4610-a023-d256de075460",
            "type-id": "f529b476-6e62-324f-b0aa-1f3e33d313fc",
            "title": "Dua Lipa",
            "primary-type": "Album"
          },
          "date": "2017-06-02",
          "country": "BR",
          "release-events": [
            {
              "date": "2017-06-02",
              "area": {
                "id": "f45b47f8-5796-386e-b172-6c31b009a5d8",
                "name": "Brazil",
                "sort-name": "Brazil",
                "iso-3166-1-codes": [
                  "BR"
                ]
              }
            }
          ],
          "track-count": 12,
          "media": [
            {
              "position": 1,
              "format": "CD",
              "track": [
                {
                  "id": "53d02b03-032d-4823-8bfd-3dcbac7f9a95",
                  "number": "6",
                  "title": "Blow Your Mind (Mwah)",
                  "length": 179386
                }
              ],
              "track-count": 12,
              "track-offset": 5
            }
          ]
        },
        {
          "id": "0806229a-8598-404d-812b-0267e956b9e5",
          "count": 1,
          "title": "Dua Lipa (complete edition)",
          "status": "Official",
          "release-group": {
            "id": "93747376-b698-4610-a023-d256de075460",
            "type-id": "f529b476-6e62-324f-b0aa-1f3e33d313fc",
            "title": "Dua Lipa",
            "primary-type": "Album"
          },
          "date": "2018-10-19",
          "country": "XW",
          "release-events": [
            {
              "date": "2018-10-19",
              "area": {
                "id": "525d4e18-3d00-31b9-a58b-a146a916de8f",
                "name": "[Worldwide]",
                "sort-name": "[Worldwide]",
                "iso-3166-1-codes": [
                  "XW"
                ]
              }
            }
          ],
          "track-count": 25,
          "media": [
            {
              "position": 1,
              "format": "Digital Media",
              "track": [
                {
                  "id": "d205ee9d-0e5b-4851-bba4-d0643044043d",
                  "number": "6",
                  "title": "Blow Your Mind (Mwah)",
                  "length": 178000
                }
              ],
              "track-count": 25,
              "track-offset": 5
            }
          ]
        },
        {
          "id": "faae05c9-0080-4575-a098-92739f5d7e3e",
          "count": 2,
          "title": "Dua Lipa (complete edition)",
          "status": "Official",
          "release-group": {
            "id": "93747376-b698-4610-a023-d256de075460",
            "type-id": "f529b476-6e62-324f-b0aa-1f3e33d313fc",
            "title": "Dua Lipa",
            "primary-type": "Album"
          },
          "date": "2018-10-19",
          "country": "GB",
          "release-events": [
            {
              "date": "2018-10-19",
              "area": {
                "id": "8a754a16-0027-3a29-b6d7-2b40ea0481ed",
                "name": "United Kingdom",
                "sort-name": "United Kingdom",
                "iso-3166-1-codes": [
                  "GB"
                ]
              }
            }
          ],
          "track-count": 25,
          "media": [
            {
              "position": 1,
              "format": "CD",
              "track": [
                {
                  "id": "efa18881-eed9-44e7-ad20-f0a8d21bf600",
                  "number": "6",
                  "title": "Blow Your Mind (Mwah)",
                  "length": 178583
                }
              ],
              "track-count": 17,
              "track-offset": 5
            }
          ]
        },
        {
          "id": "ca9fe684-223b-48ca-ac8b-f75a15d0229b",
          "count": 2,
          "title": "Life Is Music 2016.2",
          "status": "Official",
          "artist-credit": [
            {
              "name": "Various Artists",
              "artist": {
                "id": "89ad4ac3-39f7-470e-963a-56509c546377",
                "name": "Various Artists",
                "sort-name": "Various Artists",
                "disambiguation": "add compilations to this artist"
              }
            }
          ],
          "release-group": {
            "id": "406d765d-c5ca-43e9-b268-a33878927ff5",
            "type-id": "dd2a21e1-0c00-3729-a7a0-de60b84eb5d1",
            "title": "Life Is Music 2016.2",
            "primary-type": "Album",
            "secondary-types": [
              "Compilation"
            ]
          },
          "date": "2016-12-02",
          "country": "BE",
          "release-events": [
            {
              "date": "2016-12-02",
              "area": {
                "id": "5b8a5ee5-0bb3-34cf-9a75-c27c44e341fc",
                "name": "Belgium",
                "sort-name": "Belgium",
                "iso-3166-1-codes": [
                  "BE"
                ]
              }
            }
          ],
          "track-count": 39,
          "media": [
            {
              "position": 1,
              "format": "Digital Media",
              "track": [
                {
                  "id": "331e1f0b-5a10-468c-95e1-a72c159c257d",
                  "number": "8",
                  "title": "Blow Your Mind (Mwah)",
                  "length": 178583
                }
              ],
              "track-count": 20,
              "track-offset": 7
            }
          ]
        },
        {
          "id": "6313b666-6042-486b-b2aa-614e7542ff68",
          "count": 2,
          "title": "Life Is Music 2016.2",
          "status": "Official",
          "artist-credit": [
            {
              "name": "Various Artists",
              "artist": {
                "id": "89ad4ac3-39f7-470e-963a-56509c546377",
                "name": "Various Artists",
                "sort-name": "Various Artists",
                "disambiguation": "add compilations to this artist"
              }
            }
          ],
          "release-group": {
            "id": "406d765d-c5ca-43e9-b268-a33878927ff5",
            "type-id": "dd2a21e1-0c00-3729-a7a0-de60b84eb5d1",
            "title": "Life Is Music 2016.2",
            "primary-type": "Album",
            "secondary-types": [
              "Compilation"
            ]
          },
          "date": "2016-12-02",
          "country": "BE",
          "release-events": [
            {
              "date": "2016-12-02",
              "area": {
                "id": "5b8a5ee5-0bb3-34cf-9a75-c27c44e341fc",
                "name": "Belgium",
                "sort-name": "Belgium",
                "iso-3166-1-codes": [
                  "BE"
                ]
              }
            }
          ],
          "track-count": 39,
          "media": [
            {
              "position": 1,
              "format": "CD",
              "track": [
                {
                  "id": "7e93e73c-e8f2-4218-af5e-baae4ce98882",
                  "number": "8",
                  "title": "Blow Your Mind (Mwah)",
                  "length": 177266
                }
              ],
              "track-count": 20,
              "track-offset": 7
            }
          ]
        },
        {
          "id": "88c57ec1-7b04-4cad-92ad-aa967d7fd11e",
          "count": 1,
          "title": "Dua Lipa",
          "status": "Official",
          "release-group": {
            "id": "93747376-b698-4610-a023-d256de075460",
            "type-id": "f529b476-6e62-324f-b0aa-1f3e33d313fc",
            "title": "Dua Lipa",
            "primary-type": "Album"
          },
          "date": "2017-06-02",
          "country": "FR",
          "release-events": [
            {
              "date": "2017-06-02",
              "area": {
                "id": "08310658-51eb-3801-80de-5a0739207115",
                "name": "France",
                "sort-name": "France",
                "iso-3166-1-codes": [
                  "FR"
                ]
              }
            },
            {
              "date": "2017-06-02",
              "area": {
                "id": "489ce91b-6658-3307-9877-795b68554c98",
                "name": "United States",
                "sort-name": "United States",
                "iso-3166-1-codes": [
                  "US"
                ]
              }
            },
            {
              "date": "2017-06-02",
              "area": {
                "id": "8a754a16-0027-3a29-b6d7-2b40ea0481ed",
                "name": "United Kingdom",
                "sort-name": "United Kingdom",
                "iso-3166-1-codes": [
                  "GB"
                ]
              }
            },
            {
              "date": "2017-06-02",
              "area": {
                "id": "8524c7d9-f472-3890-a458-f28d5081d9c4",
                "name": "New Zealand",
                "sort-name": "New Zealand",
                "iso-3166-1-codes": [
                  "NZ"
                ]
              }
            },
            {
              "date": "2017-06-02",
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
          "track-count": 12,
          "media": [
            {
              "position": 1,
              "format": "Digital Media",
              "track": [
                {
                  "id": "b74b2a35-900a-446c-8a63-7a5b01c82a5e",
                  "number": "6",
                  "title": "Blow Your Mind (Mwah)",
                  "length": 178000
                }
              ],
              "track-count": 12,
              "track-offset": 5
            }
          ]
        },
        {
          "id": "1aaa0641-b875-45f4-845a-260a0bceb2c9",
          "count": 1,
          "title": "Dua Lipa",
          "status": "Official",
          "disambiguation": "Italian special edition",
          "release-group": {
            "id": "93747376-b698-4610-a023-d256de075460",
            "type-id": "f529b476-6e62-324f-b0aa-1f3e33d313fc",
            "title": "Dua Lipa",
            "primary-type": "Album"
          },
          "date": "2017-10-20",
          "country": "IT",
          "release-events": [
            {
              "date": "2017-10-20",
              "area": {
                "id": "c6500277-9a3d-349b-bf30-41afdbf42add",
                "name": "Italy",
                "sort-name": "Italy",
                "iso-3166-1-codes": [
                  "IT"
                ]
              }
            }
          ],
          "track-count": 13,
          "media": [
            {
              "position": 1,
              "format": "CD",
              "track": [
                {
                  "id": "fa0baec9-ee0a-451c-845a-a67ffe770723",
                  "number": "6",
                  "title": "Blow Your Mind (Mwah)",
                  "length": 178583
                }
              ],
              "track-count": 13,
              "track-offset": 5
            }
          ]
        },
        {
          "id": "d3a6bd3c-41cc-46f5-bb96-b70427c81e5a",
          "count": 1,
          "title": "Dua Lipa",
          "status": "Official",
          "release-group": {
            "id": "93747376-b698-4610-a023-d256de075460",
            "type-id": "f529b476-6e62-324f-b0aa-1f3e33d313fc",
            "title": "Dua Lipa",
            "primary-type": "Album"
          },
          "date": "2017-06-02",
          "country": "DE",
          "release-events": [
            {
              "date": "2017-06-02",
              "area": {
                "id": "85752fda-13c4-31a3-bee5-0e5cb1f51dad",
                "name": "Germany",
                "sort-name": "Germany",
                "iso-3166-1-codes": [
                  "DE"
                ]
              }
            }
          ],
          "track-count": 17,
          "media": [
            {
              "position": 1,
              "format": "CD",
              "track": [
                {
                  "id": "70ed4fce-1677-4ec6-abc4-692354c2cbec",
                  "number": "6",
                  "title": "Blow Your Mind (Mwah)",
                  "length": 178583
                }
              ],
              "track-count": 17,
              "track-offset": 5
            }
          ]
        },
        {
          "id": "3f37b852-085c-47cc-a6ed-305204bd2f17",
          "count": 1,
          "title": "Dua Lipa",
          "status": "Official",
          "release-group": {
            "id": "93747376-b698-4610-a023-d256de075460",
            "type-id": "f529b476-6e62-324f-b0aa-1f3e33d313fc",
            "title": "Dua Lipa",
            "primary-type": "Album"
          },
          "date": "2017-06-02",
          "country": "XE",
          "release-events": [
            {
              "date": "2017-06-02",
              "area": {
                "id": "89a675c2-3e37-3518-b83c-418bad59a85a",
                "name": "Europe",
                "sort-name": "Europe",
                "iso-3166-1-codes": [
                  "XE"
                ]
              }
            }
          ],
          "track-count": 12,
          "media": [
            {
              "position": 1,
              "format": "CD",
              "track": [
                {
                  "id": "a1182982-babe-444d-8c01-871043fc1b2f",
                  "number": "6",
                  "title": "Blow Your Mind (Mwah)",
                  "length": 179000
                }
              ],
              "track-count": 12,
              "track-offset": 5
            }
          ]
        },
        {
          "id": "538792e7-e9af-4351-8753-15f70348538b",
          "count": 1,
          "title": "Dua Lipa (deluxe edition)",
          "status": "Official",
          "release-group": {
            "id": "93747376-b698-4610-a023-d256de075460",
            "type-id": "f529b476-6e62-324f-b0aa-1f3e33d313fc",
            "title": "Dua Lipa",
            "primary-type": "Album"
          },
          "date": "2017-06-02",
          "country": "XE",
          "release-events": [
            {
              "date": "2017-06-02",
              "area": {
                "id": "89a675c2-3e37-3518-b83c-418bad59a85a",
                "name": "Europe",
                "sort-name": "Europe",
                "iso-3166-1-codes": [
                  "XE"
                ]
              }
            }
          ],
          "track-count": 17,
          "media": [
            {
              "position": 1,
              "format": "CD",
              "track": [
                {
                  "id": "51ddda22-1ac8-4f2a-9dfe-0376e4c37f87",
                  "number": "6",
                  "title": "Blow Your Mind (Mwah)",
                  "length": 178583
                }
              ],
              "track-count": 17,
              "track-offset": 5
            }
          ]
        },
        {
          "id": "5084de65-a2f6-4b2f-91c3-4ff50dffaf8b",
          "count": 1,
          "title": "Dua Lipa (deluxe edition)",
          "status": "Official",
          "release-group": {
            "id": "93747376-b698-4610-a023-d256de075460",
            "type-id": "f529b476-6e62-324f-b0aa-1f3e33d313fc",
            "title": "Dua Lipa",
            "primary-type": "Album"
          },
          "date": "2017-06-02",
          "country": "MX",
          "release-events": [
            {
              "date": "2017-06-02",
              "area": {
                "id": "3e08b2cd-69f3-317c-b1e4-e71be581839e",
                "name": "Mexico",
                "sort-name": "Mexico",
                "iso-3166-1-codes": [
                  "MX"
                ]
              }
            },
            {
              "date": "2017-06-02",
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
          "track-count": 17,
          "media": [
            {
              "position": 1,
              "format": "Digital Media",
              "track": [
                {
                  "id": "b9b4d750-cb1b-4418-8392-f9543881ee6a",
                  "number": "6",
                  "title": "Blow Your Mind (Mwah)",
                  "length": 178583
                }
              ],
              "track-count": 17,
              "track-offset": 5
            }
          ]
        },
        {
          "id": "899215cd-1944-4309-8aae-2749db2a1a49",
          "count": 1,
          "title": "The Only EP",
          "status": "Official",
          "release-group": {
            "id": "5e0f5e51-42df-4c5f-a3b8-34e50f9ed495",
            "type-id": "6d0c5bf6-7a33-3420-a519-44fc63eedebf",
            "title": "The Only EP",
            "primary-type": "EP"
          },
          "date": "2017-04-21",
          "country": "US",
          "release-events": [
            {
              "date": "2017-04-21",
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
          "track-count": 5,
          "media": [
            {
              "position": 1,
              "format": "Vinyl",
              "track": [
                {
                  "id": "e56f7bce-df6c-4655-a519-0191c1ff592d",
                  "number": "1",
                  "title": "Blow Your Mind (Mwah)",
                  "length": 179000
                }
              ],
              "track-count": 5,
              "track-offset": 0
            }
          ]
        },
        {
          "id": "383be31c-37a0-4e08-8cda-cbcbbc587ae5",
          "count": 1,
          "title": "Blow Your Mind (Mwah)",
          "status": "Official",
          "release-group": {
            "id": "4a45bfa5-eb1e-49eb-a20c-1021389b2121",
            "type-id": "d6038452-8ee0-3f68-affc-2de9a1ede0b9",
            "title": "Blow Your Mind (Mwah)",
            "primary-type": "Single"
          },
          "date": "2016-08-26",
          "country": "XW",
          "release-events": [
            {
              "date": "2016-08-26",
              "area": {
                "id": "525d4e18-3d00-31b9-a58b-a146a916de8f",
                "name": "[Worldwide]",
                "sort-name": "[Worldwide]",
                "iso-3166-1-codes": [
                  "XW"
                ]
              }
            }
          ],
          "track-count": 1,
          "media": [
            {
              "position": 1,
              "format": "Digital Media",
              "track": [
                {
                  "id": "0ef6e647-4aeb-438e-8c8a-50c22c511203",
                  "number": "1",
                  "title": "Blow Your Mind (Mwah)",
                  "length": 179000
                }
              ],
              "track-count": 1,
              "track-offset": 0
            }
          ]
        }
      ],
      "isrcs": [
        "GBAHT1600302",
        "DEUM71601954",
        "GBAHT1600331"
      ],
      "tags": [
        {
          "count": 2,
          "name": "pop"
        },
        {
          "count": 1,
          "name": "electropop"
        },
        {
          "count": 1,
          "name": "dance-pop"
        },
        {
          "count": 1,
          "name": "contemporary r&b"
        }
      ]
    }
  ]
}
"""
