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

import com.ealva.ealvabrainz.brainz.data.Mbid.Companion.MBID_LOG
import com.squareup.moshi.JsonClass
import com.ealva.ealvalog.invoke
import com.ealva.ealvalog.w

@Suppress("MaxLineLength")
/**
 * * **Book** A book with a sleeve containing a medium (usually a CD).
 * * **Box** A box with a lid or an opening that contains the medium and other packaging, like posters and booklet containing lyrics.
 * * **Cardboard/Paper Sleeve** A sleeve made of paper, paperboard, or cardboard. Traditional packaging for records, also seen with CDs.
 * * **Cassette Case** Regular plastic case for a cassette.
 * * **Digibook** A bounded booklet usually in hardcover with a sleeve bound to the spine of the book that houses a CD.
 * * **Digipak**	A folded case, typically made of coated paperboard, with one or more plastic trays glued into it. Cases that can only be folded in half usually have only one plastic tray glued to it, while those that can be folded into thirds or more folds usually have multiple plastic trays glued to it.
 * * **Discbox Slider** A pouch-like package with an internal mechanism that pushes the contents (usually a CD) out of the case when the lid flap is opened.
 * * **Fatbox**  A double-sided, double-width jewel case normally holding 2 to 4 CDs, but (with Smart Tray or Brilliant Box inside) capable of up to 6 CDs.
 * * **Gatefold Cover** A cardboard sleeve that folds in halves, thirds, etc. It can hold multiple records or CDs as well as booklets, posters and other memorabilia.
 * * **Jewel case** The traditional CD case, made of hard, brittle plastic.
 * * **Keep Case** The traditional DVD case, made of soft plastic (usually) dark grey with a thin transparent plastic cover protecting the artwork.
 * * **Plastic sleeve** A sleeve made entirely of plastic that holds the medium and other parts of an album (e.g. liner notes and track list). The plastic sleeve is usually transparent. For paper sleeves that have a plastic part that shows the medium, please use "Cardboard/Paper Sleeve".
 * * **Slidepack** A box with openings at its two ends that contains a tray (usually plastic) holding the medium. The box usually has a cutout to help the user to pull out the tray.
 * * **Slim Jewel Case**	A thinner jewel case, commonly used for CD singles.
 * * **Snap Case** A digipak-like case held together with a "snapping" plastic closure.
 * * **Super Jewel Box/Case** Similar to the regular jewel case, but with rounded corners and a latch closing mechanism. Usually used for SACDs.
 * * **Other** Other forms of packaging that are not described by the prescribed list.
 * * **None** No packaging at all. Common for digital media (downloads).
 */
@JsonClass(generateAdapter = true)
public class Packaging(
  public var id: String = "",
  public var name: String = ""
) {
  public companion object {
    public val NullPackaging: Packaging = Packaging(id = NullObject.ID, name = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> = Packaging::class.java.name to NullPackaging
  }
}

public inline val Packaging.isNullObject: Boolean
  get() = this === Packaging.NullPackaging

public inline class PackagingMbid(override val value: String) : Mbid

public inline val Packaging.mbid: PackagingMbid
  get() = id.toPackagingMbid()

@Suppress("NOTHING_TO_INLINE")
public inline fun String.toPackagingMbid(): PackagingMbid {
  if (Mbid.logInvalidMbid && isInvalidMbid()) MBID_LOG.w { it("Invalid PackagingMbid") }
  return PackagingMbid(this)
}
