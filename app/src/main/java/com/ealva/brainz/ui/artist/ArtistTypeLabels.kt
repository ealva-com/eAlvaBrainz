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

package com.ealva.brainz.ui.artist

import androidx.annotation.StringRes
import com.ealva.ealvabrainz.brainz.data.ArtistType
import com.ealva.ealvabrainz.service.R

/**
 * Groups an ArtistType with the labels to be presented in the user interface representing the
 * lifespan.
 *
 * The begin and end dates indicate when an artist started and finished its existence. Its exact
 * meaning depends on the type of artist:
 * * For a **person**:
 * Begin date represents date of birth, and end date represents date of death.
 * * For a **group** (or **orchestra/choir**):
 * Begin date represents the date when the group first formed: if a group dissolved and then
 * reunited, the date is still that of when they first formed. End date represents the date when
 * the group last dissolved: if a group dissolved and then reunited, the date is that of when they
 * last dissolved (if they are together, it should be blank!). For listing other inactivity
 * periods, just use the annotation and the "member of" relationships.
 * * For a **character**:
 * Begin date represents the date (in real life) when the character concept was created. The End
 * date should not be set, since new media featuring a character can be created at any time. In
 * particular, the Begin and End date fields should not be used to hold the fictional birth or
 * death dates of a character. (This information can be put in the annotation.)
 * * For **other**:
 * There are no clear indications about how to use dates for artists of the type Other at the
 * moment.
 */
enum class ArtistTypeLabels(
  val type: ArtistType,
  @field:StringRes val typeName: Int,
  @field:StringRes val lifespanBeginLabel: Int = R.string.FoundedLabel,
  @field:StringRes val lifespanEndLabel: Int = R.string.DissolvedLabel,
  @field:StringRes val startAreaLabel: Int = R.string.FoundedInLabel,
  @field:StringRes val endAreaLabel: Int = R.string.DissolvedInLabel
) {
  PersonLabels(
    ArtistType.Person,
    R.string.Person,
    R.string.BornLabel,
    R.string.DiedLabel,
    R.string.BornInLabel,
    R.string.DiedInLabel
  ),
  GroupLabels(ArtistType.Group, R.string.Group),
  OrchestraLabels(ArtistType.Orchestra, R.string.Orchestra),
  ChoirLabels(ArtistType.Choir, R.string.Choir),
  CharacterLabels(
    ArtistType.Character,
    R.string.Character,
    R.string.CreatedLabel,
    R.string.empty,
    R.string.CreatedInLabel,
    R.string.empty
  ),
  OtherLabels(
    ArtistType.Other,
    R.string.Other,
    R.string.StartedLabel,
    R.string.EndedLabel,
    R.string.StartedInLabel,
    R.string.EndedInLabel
  ),
  UnknownLabels(
    ArtistType.Unknown,
    R.string.Unknown,
    R.string.empty,
    R.string.empty,
    R.string.empty,
    R.string.empty
  );

}

private val typeToLabelsMap = ArtistTypeLabels.values().associateBy { it.type }

/**
 * Get an ArtistType's labels.
 */
val ArtistType.labels: ArtistTypeLabels
  get() = typeToLabelsMap[this] ?: ArtistTypeLabels.UnknownLabels

