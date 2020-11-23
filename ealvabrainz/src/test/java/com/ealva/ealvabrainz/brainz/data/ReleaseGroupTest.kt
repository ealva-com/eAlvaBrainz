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

import com.ealva.ealvabrainz.matchers.map.expect
import com.ealva.ealvabrainz.matchers.string.toBeEmpty
import com.nhaarman.expect.expect
import com.squareup.moshi.Moshi
import org.junit.Before
import org.junit.Test

public class ReleaseGroupTest {
  private lateinit var moshi: Moshi

  @Before
  public fun setup() {
    moshi = theMoshi
  }

  /**
   * Especially want to test Relations as there are quite a few types and require a custom
   * adapter because actual type is discovered within the json of the object
   */
  @Test
  public fun `test parse release all inc`() {
    moshi.adapter(ReleaseGroup::class.java).fromJson(editedSgtPeppersJson)?.run {
      expect(annotation).toBeEmpty()
      expect(title).toBe("Sgt. Pepper’s Lonely Hearts Club Band")
      expect(primaryTypeId).toBe("f529b476-6e62-324f-b0aa-1f3e33d313fc")
      expect(primaryType).toBe("Album")
      expect(secondaryTypes).toBeEmpty()
      expect(secondaryTypeIds).toBeEmpty()
      expect(tags).toHaveSize(3)
      expect(tags).toContain(
        listOf(
          Tag("60s", 1),
          Tag("psychedelic rock", 9),
          Tag("rock", 13)
        )
      )
      expect(relations).toHaveSize(4)
      expect(relations[0]).toBeInstanceOf<ReleaseGroupRelation> { groupRelation ->
        groupRelation.run {
          expect(typeId).toBe("cf02e524-9d5b-46b7-a88e-329737395818")
          expect(releaseGroup).toNotBeTheSameAs(ReleaseGroup.NullReleaseGroup)
          releaseGroup.run {
            expect(firstReleaseDate).toBe("2009-04-14")
            expect(id).toBe("3adcb2c5-2f70-35f1-9b4e-e8765acf3177")
            expect(title).toBe("Easy Star's Lonely Hearts Dub Band")
            expect(artistCredit).toHaveSize(1)
            artistCredit[0].run {
              expect(artist.name).toBe("Easy Star All‐Stars")
              expect(artist.id).toBe("0cee95e3-a61d-4cd1-a39c-75464a05b606")
              expect(artist.sortName).toBe("Easy Star All‐Stars")
              expect(name).toBe("Easy Star All‐Stars")
            }
            expect(releases).toBeEmpty()
          }
          expect(type).toBe("cover")
          expect(targetType).toBe("release_group")
          expect(direction).toBe("backward")
          expect(attributes).toBeEmpty()
          expect(attributeValues).toBeEmpty()
          expect(attributeIds).toBeEmpty()
          expect(ended).toBe(false)
        }
      }
      expect(relations[1]).toBeInstanceOf<SeriesRelation> { seriesRelation ->
        seriesRelation.run {
          expect(attributeIds).toContainKeyValue(
            Pair("number", "a59c5830-5ec7-38fe-9a21-c7ea54f6650a")
          )
          expect(series.id).toBe("96eef69c-02a8-4e07-8b0d-cc56a184bd1f")
          expect(series.name).toBe("Grammy Award: Best Pop Vocal Album")
          expect(series.disambiguation).toBeEmpty()
          expect(orderingKey).toBe(1)
          expect(attributeValues).toContainKeyValue(Pair("number", "1968"))
          expect(attributes).toContain("number")
          expect(typeId).toBe("01018437-91d8-36b9-bf89-3f885d53b5bd")
          expect(direction).toBe("forward")
          expect(targetType).toBe("series")
          expect(type).toBe("part of")
        }
      }

      expect(relations[2]).toBeInstanceOf<UrlRelation> { urlRelation ->
        urlRelation.run {
          expect(direction).toBe("forward")
          expect(targetType).toBe("url")
          expect(url.id).toBe("87560598-a9e8-4a8d-8ba8-c170cae809d7")
          expect(url.resource).toBe("https://www.allmusic.com/album/mw0000649874")
          expect(type).toBe("allmusic")
          expect(typeId).toBe("a50a1d20-2b20-4d2c-9a29-eb771dd78386")
          expect(attributes).toBeEmpty()
          expect(attributeValues).toBeEmpty()
          expect(attributeIds).toBeEmpty()
        }
      }

      expect(relations[3]).toBeInstanceOf<UrlRelation> { urlRelation ->
        urlRelation.run {
          expect(direction).toBe("forward")
          expect(targetType).toBe("url")
          expect(url.id).toBe("d88b11db-e096-4126-9ec9-264ac5d153ad")
          expect(url.resource).toBe("https://www.discogs.com/master/23934")
          expect(type).toBe("discogs")
          expect(typeId).toBe("99e550f3-5ab4-3110-b5b9-fe01d970b126")
          expect(attributes).toBeEmpty()
          expect(attributeValues).toBeEmpty()
          expect(attributeIds).toBeEmpty()
        }
      }

      expect(releases).toHaveSize(2)
      releases[0].run {
        expect(id).toBe("26d5e76b-1640-3883-887b-507e3a287116")
        expect(textRepresentation.language).toBe("eng")
        expect(textRepresentation.script).toBe("Latn")
        expect(media).toHaveSize(1)

        media[0].run {
          expect(trackCount).toBe(13)
          expect(formatId).toBe("3e9080b0-5e6c-34ab-bd15-f526b6306a64")
          expect(format).toBe("""12" Vinyl""")
          expect(position).toBe(1)
        }

        expect(packagingId).toBe("e724a489-a7e8-30a1-a17c-30dfd6831202")
        expect(packaging).toBe("Gatefold Cover")
        expect(statusId).toBe("4e304316-386d-3409-af2e-78857eec5cfe")
        expect(country).toBe("DE")
        expect(status).toBe("Official")
        expect(quality).toBe("normal")
        expect(title).toBe("Sgt. Pepper’s Lonely Hearts Club Band")
        expect(artistCredit).toHaveSize(1)

        artistCredit[0].run {
          expect(name).toBe("The Beatles")
          artist.run {
            expect(name).toBe("The Beatles")
            expect(id).toBe("b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d")
            expect(genres).toHaveSize(2)
            expect(genres).toContain(
              listOf(
                Genre("rock", 18),
                Genre("rock and roll", 0)
              )
            )
            expect(tags).toHaveSize(3)
            expect(tags).toContain(
              listOf(
                Tag("1", 0),
                Tag("60s", 1),
                Tag("united kingdom", 0)
              )
            )
            expect(aliases).toHaveSize(3)
            aliases[0].run {
              expect(sortName).toBe("B")
              expect(type).toBe("Search hint")
              expect(name).toBe("B")
              expect(typeId).toBe("1937e404-b981-3cb7-8151-4c86ebfc8d8e")
            }
            aliases[1].run {
              expect(locale).toBe("zh-Hant")
              expect(name).toBe("披頭四")
              expect(sortName).toBe("披頭四")
            }
            aliases[2].run {
              expect(typeId).toBe("894afba6-2816-3c24-8072-eadb66bd04bc")
              expect(primary).toBe(true)
              expect(type).toBe("Artist name")
            }
            expect(sortName).toBe("Beatles, The")
          }
          expect(joinphrase).toBeEmpty()
          expect(name).toBe("The Beatles")
        }

        expect(tags).toBeEmpty()
        expect(date).toBe("1967-06-01")
        expect(releaseEvents).toHaveSize(1)
        releaseEvents[0].run {
          expect(date).toBe("1967-06-01")
          area.run {
            expect(name).toBe("Germany")
            expect(id).toBe("85752fda-13c4-31a3-bee5-0e5cb1f51dad")
            expect(iso31661Codes).toHaveSize(1)
            expect(iso31661Codes[0]).toBe("DE")
          }
        }
        expect(barcode).toBeEmpty()
        expect(genres).toBeEmpty()
      }
      releases[1].run {
        expect(id).toBe("8c81adfa-54a9-3db6-893a-c07b3313e606")
        expect(media).toHaveSize(1)
        media[0].run {
          expect(title).toBeEmpty()
          expect(formatId).toBe("9712d52a-4509-3d4b-a1a2-67c88c643e31")
          expect(format).toBe("CD")
          expect(trackCount).toBe(13)
          expect(position).toBe(1)
          expect(discs).toHaveSize(19)
          discs[0].run {
            expect(id).toBe("1QnAa6WSqfV97nXXZv.m6WPM7ZQ-")
            expect(sectors).toBe(179450)
            expect(offsetCount).toBe(13)
            expect(offsets).toHaveSize(13)
            expect(offsets).toContain(
              listOf(
                150,
                9333,
                21633,
                37265,
                49853,
                61603,
                77728,
                89513,
                112390,
                124218,
                136375,
                148473,
                154390
              )
            )
          }
          discs[18].run {
            expect(sectors).toBe(179428)
            expect(offsetCount).toBe(13)
            expect(id).toBe("x8PPEluywUBHdvQt3WKFZfu6nxM-")
            expect(offsets).toHaveSize(13)
            expect(offsets).toContain(
              listOf(
                150,
                9333,
                21632,
                37266,
                49853,
                61602,
                77731,
                89513,
                112388,
                124215,
                136372,
                148469,
                154387
              )
            )
          }
        }
        expect(disambiguation).toBe("made in UK")
        expect(packaging).toBe("Jewel Case")
        expect(status).toBe("Official")
        expect(country).toBe("GB")

        expect(artistCredit).toHaveSize(1)
        artistCredit[0].run {
          expect(name).toBe("The Beatles")
          artist.run {
            expect(id).toBe("b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d")
            expect(name).toBe("The Beatles")
            expect(sortName).toBe("Beatles, The")
          }
        }
        expect(tags).toHaveSize(2)
        expect(tags).toContain(
          listOf(
            Tag("baroque pop", 1),
            Tag("sunshine pop", 1)
          )
        )
      }

      expect(rating).toBe(Rating(value = 4.55F, votesCount = 51))

      expect(tags).toHaveSize(3)
      expect(tags).toContain(
        listOf(
          Tag("60s", 1),
          Tag("psychedelic rock", 9),
          Tag("rock", 13)
        )
      )

      expect(aliases).toHaveSize(1)
      aliases[0].run {
        expect(ended).toBe(false)
        expect(end).toBeEmpty()
        expect(sortName)
          .toBe("The Beatles Collection, Volume 5: Sgt. Pepper's Lonely Hearts Club Band")
        expect(locale).toBeEmpty()
        expect(name).toBe("The Beatles Collection, Volume 5: Sgt. Pepper's Lonely Hearts Club Band")
        expect(primary).toBe(false)
        expect(begin).toBeEmpty()
        expect(type).toBeEmpty()
        expect(typeId).toBeEmpty()
      }
    }
  }
}

/**
 * Sgt. Pepper's Lonely Hearts Club Band - edited response of 21786 lines of pretty printed json
 *
 * http://musicbrainz.org/ws/2/release-group/9f7a4c28-8fa2-3113-929c-c47a9f7982c3?inc=artists+releases+area-rels+artist-rels+event-rels+instrument-rels+label-rels+place-rels+recording-rels+release-rels+release-group-rels+series-rels+url-rels+work-rels+discids+media+artist-credits+aliases+annotation+tags+ratings+genres&fmt=json
 */
private const val editedSgtPeppersJson: String = """
{
  "annotation": null,
  "title": "Sgt. Pepper’s Lonely Hearts Club Band",
  "relations": [
    {
      "type-id": "cf02e524-9d5b-46b7-a88e-329737395818",
      "release_group": {
        "first-release-date": "2009-04-14",
        "secondary-types": [],
        "primary-type-id": null,
        "secondary-type-ids": [],
        "disambiguation": "",
        "primary-type": null,
        "artist-credit": [
          {
            "artist": {
              "name": "Easy Star All‐Stars",
              "id": "0cee95e3-a61d-4cd1-a39c-75464a05b606",
              "sort-name": "Easy Star All‐Stars",
              "disambiguation": ""
            },
            "joinphrase": "",
            "name": "Easy Star All‐Stars"
          }
        ],
        "releases": [],
        "id": "3adcb2c5-2f70-35f1-9b4e-e8765acf3177",
        "title": "Easy Star's Lonely Hearts Dub Band"
      },
      "target-credit": "",
      "type": "cover",
      "target-type": "release_group",
      "begin": null,
      "direction": "backward",
      "source-credit": "",
      "attributes": [],
      "attribute-values": {},
      "end": null,
      "ended": false,
      "attribute-ids": {}
    },
    {
      "attribute-ids": {
        "number": "a59c5830-5ec7-38fe-9a21-c7ea54f6650a"
      },
      "series": {
        "id": "96eef69c-02a8-4e07-8b0d-cc56a184bd1f",
        "name": "Grammy Award: Best Pop Vocal Album",
        "disambiguation": ""
      },
      "end": null,
      "ended": false,
      "source-credit": "",
      "ordering-key": 1,
      "attribute-values": {
        "number": "1968"
      },
      "attributes": [
        "number"
      ],
      "target-credit": "",
      "type-id": "01018437-91d8-36b9-bf89-3f885d53b5bd",
      "direction": "forward",
      "target-type": "series",
      "begin": null,
      "type": "part of"
    },
    {
      "attribute-ids": {},
      "ended": false,
      "end": null,
      "url": {
        "resource": "https://www.allmusic.com/album/mw0000649874",
        "id": "87560598-a9e8-4a8d-8ba8-c170cae809d7"
      },
      "attributes": [],
      "attribute-values": {},
      "source-credit": "",
      "target-type": "url",
      "begin": null,
      "type": "allmusic",
      "direction": "forward",
      "target-credit": "",
      "type-id": "a50a1d20-2b20-4d2c-9a29-eb771dd78386"
    },
    {
      "direction": "forward",
      "target-type": "url",
      "begin": null,
      "type": "discogs",
      "type-id": "99e550f3-5ab4-3110-b5b9-fe01d970b126",
      "target-credit": "",
      "attributes": [],
      "attribute-values": {},
      "url": {
        "id": "d88b11db-e096-4126-9ec9-264ac5d153ad",
        "resource": "https://www.discogs.com/master/23934"
      },
      "source-credit": "",
      "ended": false,
      "end": null,
      "attribute-ids": {}
    }
  ],
  "releases": [
    {
      "text-representation": {
        "language": "eng",
        "script": "Latn"
      },
      "id": "26d5e76b-1640-3883-887b-507e3a287116",
      "media": [
        {
          "track-count": 13,
          "format-id": "3e9080b0-5e6c-34ab-bd15-f526b6306a64",
          "title": "",
          "position": 1,
          "discs": [],
          "format": "12\" Vinyl"
        }
      ],
      "disambiguation": "",
      "packaging-id": "e724a489-a7e8-30a1-a17c-30dfd6831202",
      "packaging": "Gatefold Cover",
      "status-id": "4e304316-386d-3409-af2e-78857eec5cfe",
      "country": "DE",
      "status": "Official",
      "title": "Sgt. Pepper’s Lonely Hearts Club Band",
      "quality": "normal",
      "artist-credit": [
        {
          "artist": {
            "name": "The Beatles",
            "id": "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
            "genres": [
              {
                "count": 18,
                "name": "rock"
              },
              {
                "name": "rock and roll",
                "count": 0
              }
            ],
            "tags": [
              {
                "count": 0,
                "name": "1"
              },
              {
                "count": 1,
                "name": "60s"
              },
              {
                "count": 0,
                "name": "united kingdom"
              }
            ],
            "aliases": [
              {
                "sort-name": "B",
                "end": null,
                "ended": false,
                "type-id": "1937e404-b981-3cb7-8151-4c86ebfc8d8e",
                "begin": null,
                "primary": null,
                "type": "Search hint",
                "name": "B",
                "locale": null
              },
              {
                "type": "Artist name",
                "begin": null,
                "primary": true,
                "type-id": "894afba6-2816-3c24-8072-eadb66bd04bc",
                "locale": "zh-Hant",
                "name": "披頭四",
                "ended": false,
                "end": null,
                "sort-name": "披頭四"
              },
              {
                "ended": false,
                "sort-name": "더 비틀즈",
                "end": null,
                "locale": "ko",
                "name": "더 비틀즈",
                "begin": null,
                "primary": true,
                "type": "Artist name",
                "type-id": "894afba6-2816-3c24-8072-eadb66bd04bc"
              }
            ],
            "sort-name": "Beatles, The",
            "disambiguation": ""
          },
          "joinphrase": "",
          "name": "The Beatles"
        }
      ],
      "tags": [],
      "aliases": [],
      "date": "1967-06-01",
      "release-events": [
        {
          "area": {
            "name": "Germany",
            "id": "85752fda-13c4-31a3-bee5-0e5cb1f51dad",
            "sort-name": "Germany",
            "iso-3166-1-codes": [
              "DE"
            ],
            "disambiguation": ""
          },
          "date": "1967-06-01"
        }
      ],
      "barcode": "",
      "genres": []
    },
    {
      "id": "8c81adfa-54a9-3db6-893a-c07b3313e606",
      "text-representation": {
        "language": "eng",
        "script": "Latn"
      },
      "media": [
        {
          "title": "",
          "format-id": "9712d52a-4509-3d4b-a1a2-67c88c643e31",
          "track-count": 13,
          "format": "CD",
          "discs": [
            {
              "sectors": 179450,
              "offsets": [
                150,
                9333,
                21633,
                37265,
                49853,
                61603,
                77728,
                89513,
                112390,
                124218,
                136375,
                148473,
                154390
              ],
              "id": "1QnAa6WSqfV97nXXZv.m6WPM7ZQ-",
              "offset-count": 13
            },
            {
              "offset-count": 13,
              "id": "2hSvsxQjHcXkAVsJlgLh1vH0FCE-",
              "offsets": [
                150,
                9332,
                21632,
                37084,
                49623,
                61211,
                77289,
                88967,
                111847,
                123675,
                135832,
                147930,
                153847
              ],
              "sectors": 178887
            },
            {
              "sectors": 179468,
              "offsets": [
                183,
                9365,
                21665,
                37298,
                49888,
                61635,
                77763,
                89545,
                112425,
                124253,
                136410,
                148508,
                154425
              ],
              "id": "5cVTKwEtQPTiqT4A3ktiYJ14WzQ-",
              "offset-count": 13
            },
            {
              "offsets": [
                150,
                9888,
                21523,
                37175,
                49755,
                61513,
                77648,
                89463,
                112303,
                124113,
                136268,
                148453,
                154338
              ],
              "offset-count": 13,
              "id": "B1GeiGZF4AAO6T6mEjWvs90yGtQ-",
              "sectors": 179325
            },
            {
              "sectors": 180785,
              "offsets": [
                150,
                9332,
                21632,
                37415,
                50155,
                62052,
                78330,
                90262,
                113292,
                125270,
                137577,
                149825,
                155742
              ],
              "id": "Isjz0TeMoB9W74byJSwyyNdnjsg-",
              "offset-count": 13
            },
            {
              "sectors": 179467,
              "offsets": [
                182,
                9365,
                21665,
                37297,
                49887,
                61635,
                77762,
                89545,
                112425,
                124252,
                136410,
                148507,
                154425
              ],
              "offset-count": 13,
              "id": "O_qYiVIeEXzeajXTANP0dGSaTZo-"
            },
            {
              "id": "RBYk0Uz_7yguNdW94mCY95j9XXw-",
              "offset-count": 13,
              "offsets": [
                150,
                9332,
                21632,
                37088,
                49631,
                61223,
                77305,
                88988,
                111868,
                123696,
                135853,
                147951,
                153868
              ],
              "sectors": 178909
            },
            {
              "id": "SMMs5viGUE.7yAuhBmX9FWmVNo4-",
              "offset-count": 13,
              "offsets": [
                182,
                9360,
                21662,
                37297,
                49887,
                61635,
                77765,
                89547,
                112422,
                124250,
                136407,
                148507,
                154542
              ],
              "sectors": 179437
            },
            {
              "sectors": 180912,
              "offset-count": 13,
              "id": "_wUaCo1YZdR9nwVaV3WOse83X8M-",
              "offsets": [
                150,
                9482,
                21932,
                37604,
                50344,
                62142,
                78410,
                90265,
                113295,
                125273,
                137580,
                149828,
                155895
              ]
            },
            {
              "offsets": [
                150,
                9332,
                21632,
                37265,
                49855,
                61602,
                77730,
                89512,
                112392,
                124220,
                136377,
                148475,
                154392
              ],
              "offset-count": 13,
              "id": "l5aKHkFRHg_ia4ZPSACC7j2wj8o-",
              "sectors": 179435
            },
            {
              "sectors": 179436,
              "offsets": [
                150,
                9332,
                21632,
                37265,
                49855,
                61602,
                77730,
                89512,
                112392,
                124220,
                136377,
                148475,
                154392
              ],
              "id": "m4PotWXfSEoG8llRe8SebLXYEmI-",
              "offset-count": 13
            },
            {
              "sectors": 179380,
              "offset-count": 13,
              "id": "nNMzeoXeE36teN5BGUROAktiHvo-",
              "offsets": [
                150,
                9273,
                21575,
                37195,
                49790,
                61530,
                77688,
                89485,
                112343,
                124160,
                136318,
                148443,
                154355
              ]
            },
            {
              "sectors": 179382,
              "id": "op8FAlbKkfqAKIKzGu0P1hF9nbQ-",
              "offset-count": 13,
              "offsets": [
                150,
                9282,
                21580,
                37212,
                49802,
                61550,
                77677,
                89460,
                112345,
                124167,
                136325,
                148435,
                154340
              ]
            },
            {
              "offsets": [
                183,
                9278,
                21578,
                37218,
                49793,
                61555,
                77683,
                89465,
                112343,
                124163,
                136318,
                148425,
                154325
              ],
              "id": "pqOYDmFInrMT9YlKobU1VA2uX7s-",
              "offset-count": 13,
              "sectors": 179360
            },
            {
              "sectors": 181287,
              "offsets": [
                150,
                9487,
                21941,
                37729,
                50473,
                62374,
                78656,
                90591,
                113627,
                125608,
                137920,
                150172,
                156242
              ],
              "offset-count": 13,
              "id": "pxebGFw6DkZVoKBfJFStsNRmQ_w-"
            },
            {
              "offsets": [
                150,
                9992,
                21637,
                37215,
                49813,
                61482,
                77594,
                89424,
                112255,
                124102,
                136262,
                148383,
                154751
              ],
              "id": "s7ouM6UkKjlhExSdS4BKwFT57tw-",
              "offset-count": 13,
              "sectors": 179301
            },
            {
              "id": "tmpuQkKTgTRuVgJl8uKHc6nvVik-",
              "offset-count": 13,
              "offsets": [
                182,
                9365,
                21665,
                37300,
                49887,
                61637,
                77767,
                89550,
                112425,
                124252,
                136410,
                148507,
                154425
              ],
              "sectors": 179467
            },
            {
              "sectors": 179323,
              "offsets": [
                150,
                9208,
                21505,
                37140,
                49728,
                61478,
                77603,
                89388,
                112268,
                124095,
                136253,
                148350,
                154268
              ],
              "offset-count": 13,
              "id": "vTLI6ZWNja9rIxQiiav0uKBAOCU-"
            },
            {
              "sectors": 179428,
              "offset-count": 13,
              "id": "x8PPEluywUBHdvQt3WKFZfu6nxM-",
              "offsets": [
                150,
                9333,
                21632,
                37266,
                49853,
                61602,
                77731,
                89513,
                112388,
                124215,
                136372,
                148469,
                154387
              ]
            }
          ],
          "position": 1
        }
      ],
      "disambiguation": "made in UK",
      "packaging-id": "ec27701a-4a22-37f4-bfac-6616e0f9750a",
      "packaging": "Jewel Case",
      "status-id": "4e304316-386d-3409-af2e-78857eec5cfe",
      "country": "GB",
      "title": "Sgt. Pepper’s Lonely Hearts Club Band",
      "status": "Official",
      "quality": "normal",
      "artist-credit": [
        {
          "joinphrase": "",
          "artist": {
            "id": "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
            "name": "The Beatles",
            "disambiguation": "",
            "genres": [
              {
                "name": "rock",
                "count": 18
              },
              {
                "name": "rock and roll",
                "count": 0
              }
            ],
            "aliases": [
              {
                "name": "B",
                "locale": null,
                "type-id": "1937e404-b981-3cb7-8151-4c86ebfc8d8e",
                "begin": null,
                "primary": null,
                "type": "Search hint",
                "sort-name": "B",
                "end": null,
                "ended": false
              },
              {
                "type-id": "894afba6-2816-3c24-8072-eadb66bd04bc",
                "primary": true,
                "begin": null,
                "type": "Artist name",
                "name": "더 비틀즈",
                "locale": "ko",
                "end": null,
                "sort-name": "더 비틀즈",
                "ended": false
              }
            ],
            "tags": [
              {
                "name": "united kingdom",
                "count": 0
              }
            ],
            "sort-name": "Beatles, The"
          },
          "name": "The Beatles"
        }
      ],
      "date": "1987-06-01",
      "tags": [
        {
          "count": 1,
          "name": "baroque pop"
        },
        {
          "count": 1,
          "name": "sunshine pop"
        }
      ],
      "aliases": [],
      "release-events": [
        {
          "date": "1987-06-01",
          "area": {
            "iso-3166-1-codes": [
              "GB"
            ],
            "disambiguation": "",
            "sort-name": "United Kingdom",
            "id": "8a754a16-0027-3a29-b6d7-2b40ea0481ed",
            "name": "United Kingdom"
          }
        }
      ],
      "barcode": "077774644228",
      "genres": []
    }
  ],
  "rating": {
    "votes-count": 51,
    "value": 4.55
  },
  "artist-credit": [
    {
      "joinphrase": "",
      "artist": {
        "id": "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
        "name": "The Beatles",
        "disambiguation": "",
        "tags": [
          {
            "name": "60s",
            "count": 1
          },
          {
            "name": "united kingdom",
            "count": 0
          }
        ],
        "sort-name": "Beatles, The",
        "aliases": [
          {
            "type-id": "1937e404-b981-3cb7-8151-4c86ebfc8d8e",
            "primary": null,
            "begin": null,
            "type": "Search hint",
            "name": "B",
            "locale": null,
            "end": null,
            "sort-name": "B",
            "ended": false
          },
          {
            "ended": false,
            "sort-name": "Be",
            "end": null,
            "begin": null,
            "primary": null,
            "type": "Search hint",
            "type-id": "1937e404-b981-3cb7-8151-4c86ebfc8d8e",
            "locale": null,
            "name": "Be"
          },
          {
            "ended": false,
            "sort-name": "더 비틀즈",
            "end": null,
            "type": "Artist name",
            "begin": null,
            "primary": true,
            "type-id": "894afba6-2816-3c24-8072-eadb66bd04bc",
            "locale": "ko",
            "name": "더 비틀즈"
          }
        ],
        "genres": [
          {
            "name": "pop",
            "count": 10
          },
          {
            "name": "pop rock",
            "count": 5
          },
          {
            "name": "psychedelic pop",
            "count": 2
          },
          {
            "count": 2,
            "name": "psychedelic rock"
          },
          {
            "name": "rock",
            "count": 18
          }
        ]
      },
      "name": "The Beatles"
    }
  ],
  "tags": [
    {
      "count": 1,
      "name": "60s"
    },
    {
      "name": "psychedelic rock",
      "count": 9
    },
    {
      "name": "rock",
      "count": 13
    }
  ],
  "aliases": [
    {
      "ended": false,
      "end": null,
      "sort-name": "The Beatles Collection, Volume 5: Sgt. Pepper's Lonely Hearts Club Band",
      "locale": null,
      "name": "The Beatles Collection, Volume 5: Sgt. Pepper's Lonely Hearts Club Band",
      "primary": null,
      "begin": null,
      "type": null,
      "type-id": null
    }
  ],
  "primary-type-id": "f529b476-6e62-324f-b0aa-1f3e33d313fc",
  "secondary-type-ids": [],
  "primary-type": "Album"
}
"""
