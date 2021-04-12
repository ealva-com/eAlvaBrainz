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
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

public class EventListTest {
  private lateinit var moshi: Moshi

  @Before
  public fun setup() {
    moshi = theBrainzMoshi
  }

  @Test
  public fun `test event list polymorphic EventRelation`() {
    moshi.adapter(EventList::class.java).fromJson(wolfmotherEventListJson)?.run {
      expect(created).toBe("2020-02-12T18:34:36.302Z")
      expect(events).toHaveSize(12)
      events[0].run {
        expect(id).toBe("d5c05807-7091-4fb7-bff2-b5f3c2405f91")
        expect(type).toBe("Concert")
        expect(relations).toHaveSize(4)
        relations[0].run {
          expect(this).toBeInstanceOf<ArtistRelation> { relation ->
            expect(relation.type).toBe("main performer")
            expect(relation.typeId).toBe("936c7c95-3156-3889-a062-8a0cd57f8946")
            expect(relation.artist.id).toBe("e1f1e33e-2e4c-4d43-b91b-7064068d3283")
            expect(relation.artist.name).toBe("KISS")
            expect(relation.artist.disambiguation).toBe("US rock band")
          }
        }
        relations[3].run {
          expect(this).toBeInstanceOf<PlaceRelation> { relation ->
            expect(relation.type).toBe("held at")
            expect(relation.typeId).toBe("e2c6f697-07dc-38b1-be0b-83d740165532")
            expect(relation.place.id).toBe("ccf67590-2581-438e-afb7-473774b5111f")
            expect(relation.place.name).toBe("Lerkendal stadion")
          }
        }
      }
    } ?: fail("EventList was null")
  }
}

/**
 * https://musicbrainz.org/ws/2/event/?query=wolfmother&fmt=json
 */
private const val wolfmotherEventListJson =
  """
{
  "created": "2020-02-12T18:34:36.302Z",
  "count": 12,
  "offset": 0,
  "events": [
    {
      "id": "d5c05807-7091-4fb7-bff2-b5f3c2405f91",
      "type": "Concert",
      "score": 100,
      "name": "KISS at Lerkendal",
      "life-span": {
        "begin": "2010-06-08",
        "end": "2010-06-08"
      },
      "relations": [
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "e1f1e33e-2e4c-4d43-b91b-7064068d3283",
            "name": "KISS",
            "sort-name": "KISS",
            "disambiguation": "US rock band"
          }
        },
        {
          "type": "support act",
          "type-id": "492a850e-97eb-306a-a85e-4b6d98527796",
          "direction": "backward",
          "artist": {
            "id": "7a53094c-962f-4f48-8e99-c6298b7ea9d5",
            "name": "Stage Dolls",
            "sort-name": "Stage Dolls"
          }
        },
        {
          "type": "support act",
          "type-id": "492a850e-97eb-306a-a85e-4b6d98527796",
          "direction": "backward",
          "artist": {
            "id": "f0c8f75f-8736-4b1d-b312-9349fcff321d",
            "name": "Wolfmother",
            "sort-name": "Wolfmother"
          }
        },
        {
          "type": "held at",
          "type-id": "e2c6f697-07dc-38b1-be0b-83d740165532",
          "direction": "backward",
          "place": {
            "id": "ccf67590-2581-438e-afb7-473774b5111f",
            "name": "Lerkendal stadion"
          }
        }
      ]
    },
    {
      "id": "36e7d147-d4da-4075-9a72-c35c783d2716",
      "type": "Concert",
      "score": 99,
      "name": "KISS at Arena Leipzig",
      "life-span": {
        "begin": "2010-05-25",
        "end": "2010-05-25"
      },
      "relations": [
        {
          "type": "support act",
          "type-id": "492a850e-97eb-306a-a85e-4b6d98527796",
          "direction": "backward",
          "artist": {
            "id": "f0c8f75f-8736-4b1d-b312-9349fcff321d",
            "name": "Wolfmother",
            "sort-name": "Wolfmother"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "e1f1e33e-2e4c-4d43-b91b-7064068d3283",
            "name": "KISS",
            "sort-name": "KISS",
            "disambiguation": "US rock band"
          }
        },
        {
          "type": "support act",
          "type-id": "492a850e-97eb-306a-a85e-4b6d98527796",
          "direction": "backward",
          "artist": {
            "id": "5398d761-d75e-4fbf-ba73-013347eb1a17",
            "name": "Devils Run",
            "sort-name": "Devils Run"
          }
        },
        {
          "type": "held at",
          "type-id": "e2c6f697-07dc-38b1-be0b-83d740165532",
          "direction": "backward",
          "place": {
            "id": "9be7e699-8d48-4680-a09a-e22e78172de3",
            "name": "Arena Leipzig"
          }
        }
      ]
    },
    {
      "id": "c744d584-982a-477e-a3b8-9e6687a93291",
      "type": "Concert",
      "score": 99,
      "name": "KISS at O₂ World Berlin",
      "life-span": {
        "begin": "2010-05-26",
        "end": "2010-05-26"
      },
      "relations": [
        {
          "type": "support act",
          "type-id": "492a850e-97eb-306a-a85e-4b6d98527796",
          "direction": "backward",
          "artist": {
            "id": "f0c8f75f-8736-4b1d-b312-9349fcff321d",
            "name": "Wolfmother",
            "sort-name": "Wolfmother"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "e1f1e33e-2e4c-4d43-b91b-7064068d3283",
            "name": "KISS",
            "sort-name": "KISS",
            "disambiguation": "US rock band"
          }
        },
        {
          "type": "support act",
          "type-id": "492a850e-97eb-306a-a85e-4b6d98527796",
          "direction": "backward",
          "artist": {
            "id": "5398d761-d75e-4fbf-ba73-013347eb1a17",
            "name": "Devils Run",
            "sort-name": "Devils Run"
          }
        },
        {
          "type": "held at",
          "type-id": "e2c6f697-07dc-38b1-be0b-83d740165532",
          "direction": "backward",
          "place": {
            "id": "ef7f86c6-23a7-4f2c-92b7-517362682799",
            "name": "Mercedes‐Benz Arena"
          }
        }
      ]
    },
    {
      "id": "46bc68bf-bcd0-4a7a-8027-3b0ec836569c",
      "type": "Festival",
      "score": 97,
      "name": "Tons of Rock 2019, Day 2: Main Stage",
      "life-span": {
        "begin": "2019-06-28",
        "end": "2019-06-28"
      },
      "relations": [
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "4753fcb7-9270-493a-974d-8daca4e49125",
            "name": "Volbeat",
            "sort-name": "Volbeat",
            "disambiguation": "Danish rock band"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "bdacc37b-8633-4bf8-9dd5-4662ee651aec",
            "name": "Slayer",
            "sort-name": "Slayer",
            "disambiguation": "US thrash metal band"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "f0c8f75f-8736-4b1d-b312-9349fcff321d",
            "name": "Wolfmother",
            "sort-name": "Wolfmother"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "5f72c22e-8b66-4df7-9566-e3b4a04ec9db",
            "name": "Conception",
            "sort-name": "Conception",
            "disambiguation": "Norwegian power metal band"
          }
        },
        {
          "type": "held at",
          "type-id": "e2c6f697-07dc-38b1-be0b-83d740165532",
          "direction": "backward",
          "place": {
            "id": "ff664d79-52de-4bc8-92e3-5347df42f5e8",
            "name": "Main Stage"
          }
        }
      ]
    },
    {
      "id": "919c2d0e-1dbb-45fa-9a03-ebafaa285041",
      "type": "Festival",
      "score": 96,
      "name": "SXSW 2006, day 1: Eternal",
      "life-span": {
        "begin": "2006-03-15",
        "end": "2006-03-15"
      },
      "relations": [
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "2a9ea5bf-fc08-4fbc-ab73-17afa7906a52",
            "name": "Annie",
            "sort-name": "Annie",
            "disambiguation": "Norwegian singer and DJ Anne Lilia Berge Strand"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "f0c8f75f-8736-4b1d-b312-9349fcff321d",
            "name": "Wolfmother",
            "sort-name": "Wolfmother"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "f5b8ea5f-c269-45dd-9936-1fedf3c56851",
            "name": "The Presets",
            "sort-name": "Presets, The"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "cd8c5019-5d75-4d5c-bc28-e1e26a7dd5c8",
            "name": "José González",
            "sort-name": "González, José",
            "disambiguation": "indie folk singer-songwriter and guitarist"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "625618e2-5ed9-43b5-a771-e969d1ea7666",
            "name": "Pink Nasty",
            "sort-name": "Nasty, Pink",
            "disambiguation": "Austin‐based indie singer–songwriter"
          }
        },
        {
          "type": "held at",
          "type-id": "e2c6f697-07dc-38b1-be0b-83d740165532",
          "direction": "backward",
          "place": {
            "id": "5238845f-0efb-4d7e-b0b4-37db4d5e78bf",
            "name": "Eternal"
          }
        }
      ]
    },
    {
      "id": "df8b0178-0fcb-4fc5-837f-13bf68e72c12",
      "type": "Concert",
      "score": 96,
      "name": "KISS at O₂ World Hamburg",
      "life-span": {
        "begin": "2010-05-31",
        "end": "2010-05-31"
      },
      "relations": [
        {
          "type": "support act",
          "type-id": "492a850e-97eb-306a-a85e-4b6d98527796",
          "direction": "backward",
          "artist": {
            "id": "f0c8f75f-8736-4b1d-b312-9349fcff321d",
            "name": "Wolfmother",
            "sort-name": "Wolfmother"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "e1f1e33e-2e4c-4d43-b91b-7064068d3283",
            "name": "KISS",
            "sort-name": "KISS",
            "disambiguation": "US rock band"
          }
        },
        {
          "type": "support act",
          "type-id": "492a850e-97eb-306a-a85e-4b6d98527796",
          "direction": "backward",
          "artist": {
            "id": "5398d761-d75e-4fbf-ba73-013347eb1a17",
            "name": "Devils Run",
            "sort-name": "Devils Run"
          }
        },
        {
          "type": "support act",
          "type-id": "492a850e-97eb-306a-a85e-4b6d98527796",
          "direction": "backward",
          "artist": {
            "id": "3f418fb5-3235-4a5f-add7-14e48d42b266",
            "name": "Five and the Red One",
            "sort-name": "Five and Red One, The"
          }
        },
        {
          "type": "held at",
          "type-id": "e2c6f697-07dc-38b1-be0b-83d740165532",
          "direction": "backward",
          "place": {
            "id": "04b7787d-db6c-4a29-af2f-ec3a89d961c4",
            "name": "Barclaycard Arena"
          }
        }
      ]
    },
    {
      "id": "b04d3cd5-2e68-41ae-a787-d4c3589badbd",
      "type": "Concert",
      "score": 96,
      "name": "KISS at König‐Pilsener‐Arena",
      "life-span": {
        "begin": "2010-06-01",
        "end": "2010-06-01"
      },
      "relations": [
        {
          "type": "support act",
          "type-id": "492a850e-97eb-306a-a85e-4b6d98527796",
          "direction": "backward",
          "artist": {
            "id": "f0c8f75f-8736-4b1d-b312-9349fcff321d",
            "name": "Wolfmother",
            "sort-name": "Wolfmother"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "e1f1e33e-2e4c-4d43-b91b-7064068d3283",
            "name": "KISS",
            "sort-name": "KISS",
            "disambiguation": "US rock band"
          }
        },
        {
          "type": "support act",
          "type-id": "492a850e-97eb-306a-a85e-4b6d98527796",
          "direction": "backward",
          "artist": {
            "id": "5398d761-d75e-4fbf-ba73-013347eb1a17",
            "name": "Devils Run",
            "sort-name": "Devils Run"
          }
        },
        {
          "type": "support act",
          "type-id": "492a850e-97eb-306a-a85e-4b6d98527796",
          "direction": "backward",
          "artist": {
            "id": "3f418fb5-3235-4a5f-add7-14e48d42b266",
            "name": "Five and the Red One",
            "sort-name": "Five and Red One, The"
          }
        },
        {
          "type": "held at",
          "type-id": "e2c6f697-07dc-38b1-be0b-83d740165532",
          "direction": "backward",
          "place": {
            "id": "fdc538c6-ab17-457a-acbd-7c989c89c45f",
            "name": "König‐Pilsener‐Arena"
          }
        }
      ]
    },
    {
      "id": "de4a553f-e405-4376-935e-7cd10d96a4c1",
      "type": "Festival",
      "score": 94,
      "name": "Southside 2006, Day 1, Red Stage",
      "life-span": {
        "begin": "2006-06-23",
        "end": "2006-06-23"
      },
      "relations": [
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "f0c8f75f-8736-4b1d-b312-9349fcff321d",
            "name": "Wolfmother",
            "sort-name": "Wolfmother"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "cd0e7310-3197-40f6-a861-dc57cc1f6511",
            "name": "Boozed",
            "sort-name": "Boozed"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "7ed96efe-1ff0-442d-9356-f36bf8599f8d",
            "name": "She-Male Trouble",
            "sort-name": "She-Male Trouble"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "d415628d-8c84-453b-8a75-4816b088dbc9",
            "name": "blackmail",
            "sort-name": "blackmail",
            "disambiguation": "German indie rock band"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "25a757f1-9fbe-4c52-be2e-1a5294fb25b9",
            "name": "Shout Out Louds",
            "sort-name": "Shout Out Louds"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "c428180a-2871-4788-9815-e09f7390c6ec",
            "name": "Backyard Babies",
            "sort-name": "Backyard Babies"
          }
        }
      ]
    },
    {
      "id": "bf075ddd-7cc8-4b9f-850a-18e3964c3937",
      "type": "Festival",
      "score": 92,
      "name": "Southside 2012, Day 3, Green Stage",
      "life-span": {
        "begin": "2012-06-24",
        "end": "2012-06-24"
      },
      "relations": [
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "8a0c3b86-af55-42d2-85b9-6372e7430986",
            "name": "Madsen",
            "sort-name": "Madsen"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "f0c8f75f-8736-4b1d-b312-9349fcff321d",
            "name": "Wolfmother",
            "sort-name": "Wolfmother"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "606bf117-494f-4864-891f-09d63ff6aa4b",
            "name": "Rise Against",
            "sort-name": "Rise Against"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "0743b15a-3c32-48c8-ad58-cb325350befa",
            "name": "blink‐182",
            "sort-name": "blink‐182"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "433e330b-43ad-4c60-ab0a-5f024707eaf8",
            "name": "All Mankind",
            "sort-name": "All Mankind"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "40a10c54-06a1-46ad-9161-53e989c4ca95",
            "name": "Young Guns",
            "sort-name": "Young Guns",
            "disambiguation": "UK rock band"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "e54e065f-0ce6-4779-9a9a-afd7b997471f",
            "name": "Less Than Jake",
            "sort-name": "Less Than Jake"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "de11b037-d880-40e0-8901-0397c768c457",
            "name": "Eagles of Death Metal",
            "sort-name": "Eagles of Death Metal"
          }
        }
      ]
    },
    {
      "id": "447bd891-1e9a-403c-bcc6-8d72da3bc1f9",
      "type": "Festival",
      "score": 89,
      "name": "Sasquatch! Music Festival 2006, Day 1",
      "life-span": {
        "begin": "2006-05-26",
        "end": "2006-05-26"
      },
      "relations": [
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "b7ffd2af-418f-4be2-bdd1-22f8b48613da",
            "name": "Nine Inch Nails",
            "sort-name": "Nine Inch Nails"
          }
        },
        {
          "type": "support act",
          "type-id": "492a850e-97eb-306a-a85e-4b6d98527796",
          "direction": "backward",
          "artist": {
            "id": "9c1ff574-2ae4-4fea-881f-83293d0d5881",
            "name": "…And You Will Know Us by the Trail of Dead",
            "sort-name": "And You Will Know Us by the Trail of Dead"
          }
        },
        {
          "type": "support act",
          "type-id": "492a850e-97eb-306a-a85e-4b6d98527796",
          "direction": "backward",
          "artist": {
            "id": "0688add2-c282-4ee2-ba61-223ffdb3c201",
            "name": "Bauhaus",
            "sort-name": "Bauhaus",
            "disambiguation": "UK gothic rock band"
          }
        },
        {
          "type": "support act",
          "type-id": "492a850e-97eb-306a-a85e-4b6d98527796",
          "direction": "backward",
          "artist": {
            "id": "19f00315-8c23-4d69-8f02-9cee1987b40b",
            "name": "deadboy & the Elephantmen",
            "sort-name": "deadboy & the Elephantmen"
          }
        },
        {
          "type": "support act",
          "type-id": "492a850e-97eb-306a-a85e-4b6d98527796",
          "direction": "backward",
          "artist": {
            "id": "eb872766-98f6-453d-883f-2ae908a18315",
            "name": "TV on the Radio",
            "sort-name": "TV on the Radio"
          }
        },
        {
          "type": "support act",
          "type-id": "492a850e-97eb-306a-a85e-4b6d98527796",
          "direction": "backward",
          "artist": {
            "id": "4cc51547-88ae-49a2-bf7e-0ef65ee7a94c",
            "name": "HIM",
            "sort-name": "HIM",
            "disambiguation": "Finnish rock band"
          }
        },
        {
          "type": "support act",
          "type-id": "492a850e-97eb-306a-a85e-4b6d98527796",
          "direction": "backward",
          "artist": {
            "id": "1876ccbe-cac2-459f-9c0e-6b78546bdbb0",
            "name": "The Trucks",
            "sort-name": "Trucks, The",
            "disambiguation": "US electronic rock band"
          }
        },
        {
          "type": "support act",
          "type-id": "492a850e-97eb-306a-a85e-4b6d98527796",
          "direction": "backward",
          "artist": {
            "id": "f0c8f75f-8736-4b1d-b312-9349fcff321d",
            "name": "Wolfmother",
            "sort-name": "Wolfmother"
          }
        },
        {
          "type": "held at",
          "type-id": "e2c6f697-07dc-38b1-be0b-83d740165532",
          "direction": "backward",
          "place": {
            "id": "f12f9ea5-94f5-4f24-b7e6-7c674da082c4",
            "name": "The Gorge Amphitheatre"
          }
        }
      ]
    },
    {
      "id": "3ade4b23-d109-4a6d-8564-1aacb4139fc5",
      "type": "Festival",
      "score": 86,
      "name": "Coachella Valley Music and Arts Festival 2006, Day 1: Mojave Tent",
      "life-span": {
        "begin": "2006-04-29",
        "end": "2006-04-29"
      },
      "relations": [
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "05755bf1-380c-487f-983f-d1a02401fa28",
            "name": "Cat Power",
            "sort-name": "Cat Power"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "8e05a404-3f8d-4b0a-9fc2-b7ab821b75f0",
            "name": "The Rakes",
            "sort-name": "Rakes, The",
            "disambiguation": "UK indie rock band"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "600f0c0d-ddf0-447e-b282-38aaa97f76dc",
            "name": "Living Things",
            "sort-name": "Living Things"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "b45335d1-5219-4262-a44d-936aa36eeaed",
            "name": "Ladytron",
            "sort-name": "Ladytron",
            "disambiguation": "UK electronic band"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "eb872766-98f6-453d-883f-2ae908a18315",
            "name": "TV on the Radio",
            "sort-name": "TV on the Radio"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "4b2d6a23-034d-4a29-9bb9-d2462796da4e",
            "name": "Clap Your Hands Say Yeah",
            "sort-name": "Clap Your Hands Say Yeah"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "f0c8f75f-8736-4b1d-b312-9349fcff321d",
            "name": "Wolfmother",
            "sort-name": "Wolfmother"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "75a881d8-80de-4f60-b56a-4eaaa29530d9",
            "name": "Nine Black Alps",
            "sort-name": "Nine Black Alps"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "890f2a9a-6488-409c-8684-d28b283b5b80",
            "name": "White Rose Movement",
            "sort-name": "White Rose Movement"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "0cd8acc6-59b7-450b-a202-661092391a79",
            "name": "Rob Dickinson",
            "sort-name": "Dickinson, Rob"
          }
        },
        {
          "type": "held at",
          "type-id": "e2c6f697-07dc-38b1-be0b-83d740165532",
          "direction": "backward",
          "place": {
            "id": "783f2240-8c0b-46f8-a4e4-2db0151fad71",
            "name": "Empire Polo Club"
          }
        }
      ]
    },
    {
      "id": "ba6bb81c-bdba-499a-a3da-4924d3a62b05",
      "type": "Festival",
      "score": 75,
      "name": "Voodoo Music Experience 2009, Day 2",
      "life-span": {
        "begin": "2009-10-31",
        "end": "2009-10-31"
      },
      "relations": [
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "7a0ae10d-c51f-434a-9d5a-551d2ae93a9d",
            "name": "Parliament-Funkadelic",
            "sort-name": "Parliament-Funkadelic"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "64e04566-3e71-4dd7-9632-8e6143365cd1",
            "name": "American Bang",
            "sort-name": "American Bang"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "71f17703-cd11-4579-b08a-1b1c8e6f5ac2",
            "name": "As Tall as Lions",
            "sort-name": "As Tall as Lions"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "578b5d70-3e66-4308-baa5-62e79e501a23",
            "name": "Big Sam’s Funky Nation",
            "sort-name": "Big Sam’s Funky Nation"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "e940d7a3-01d0-468c-86ea-5dc4d89dcf80",
            "name": "Black Lips",
            "sort-name": "Black Lips"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "23b6482c-5ca1-4d0f-9e66-7b6e2a04ad8b",
            "name": "Dan Dyer",
            "sort-name": "Dyer, Dan"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "4c01333f-e8e8-43bd-9923-8de83ef6f63d",
            "name": "Down",
            "sort-name": "Down",
            "disambiguation": "US metal supergroup"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "8eae1e0a-1696-4532-9e3c-0a072217ef4c",
            "name": "Drive-By Truckers",
            "sort-name": "Drive-By Truckers"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "c0b1be20-6ae2-43b6-9e08-6f2793dee62a",
            "name": "Fatter Than Albert",
            "sort-name": "Fatter Than Albert"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "84683370-5eae-418b-acd8-883ac028a8a0",
            "name": "George Clinton",
            "sort-name": "Clinton, George",
            "disambiguation": "US funk musician"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "f47fc54d-b334-4321-8218-00c5b11d4dd1",
            "name": "Gogol Bordello",
            "sort-name": "Gogol Bordello"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "e3434cc7-d348-491a-9dc8-325af3d9086d",
            "name": "Jane’s Addiction",
            "sort-name": "Jane’s Addiction"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "ab72eff4-3e2d-46c1-803f-d0913ab45878",
            "name": "Jello Biafra and The Guantanamo School of Medicine",
            "sort-name": "Jello Biafra and Guantanamo School of Medicine, The"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "ee421ee4-f861-405d-8730-29cb512dfdbd",
            "name": "K’naan",
            "sort-name": "K’naan",
            "disambiguation": "Somali Canadian poet, rapper, singer, songwriter and instrumentalist"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "e1f1e33e-2e4c-4d43-b91b-7064068d3283",
            "name": "KISS",
            "sort-name": "KISS",
            "disambiguation": "US rock band"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "fefbec80-0f2c-49cd-9f51-f22bff9e7f4b",
            "name": "Leroy Jones",
            "sort-name": "Jones, Leroy",
            "disambiguation": "reggae, aka Jah Dave"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "6e855495-c063-4052-a26d-959c3f226c87",
            "name": "MUTEMATH",
            "sort-name": "MUTEMATH"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "e9c97fdb-9fa0-4955-9a6c-1c43e1ac3f64",
            "name": "Mates of State",
            "sort-name": "Mates of State"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "b0714be4-6696-4a8e-86a5-863d1716dc97",
            "name": "Morning 40 Federation",
            "sort-name": "Morning 40 Federation"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "484bca89-c1cf-47eb-967a-0eea93239657",
            "name": "New Orleans Klezmer All Stars",
            "sort-name": "New Orleans Klezmer All Stars"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "3163d643-596f-4890-8f39-d16c08740bea",
            "name": "Rotary Downs",
            "sort-name": "Rotary Downs"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "1157b9e1-df8d-4415-b070-865bad33f190",
            "name": "Sam & Ruby",
            "sort-name": "Sam & Ruby"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "9b6f4081-a3c4-4c5d-8c47-745e34145197",
            "name": "The New Orleans Bingo! Show",
            "sort-name": "New Orleans Bingo! Show, The"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "3e5cfcf1-4bd5-4b2e-9649-904a010b5659",
            "name": "Walter “Wolfman” Washington",
            "sort-name": "Washington, Walter “Wolfman”"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "f0c8f75f-8736-4b1d-b312-9349fcff321d",
            "name": "Wolfmother",
            "sort-name": "Wolfmother"
          }
        },
        {
          "type": "main performer",
          "type-id": "936c7c95-3156-3889-a062-8a0cd57f8946",
          "direction": "backward",
          "artist": {
            "id": "3357cc0c-478b-4e43-8783-5c5894cb0119",
            "name": "Zydepunks",
            "sort-name": "Zydepunks"
          }
        },
        {
          "type": "held at",
          "type-id": "e2c6f697-07dc-38b1-be0b-83d740165532",
          "direction": "backward",
          "place": {
            "id": "13576de7-4ad5-4995-aa9d-945a6de684a2",
            "name": "City Park"
          }
        }
      ]
    }
  ]
}"""
