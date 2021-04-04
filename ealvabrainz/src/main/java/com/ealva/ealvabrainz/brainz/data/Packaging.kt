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

import com.squareup.moshi.JsonClass

@Suppress("MaxLineLength")
/**
 * * **Book A book with a sleeve containing a medium (usually a CD).
 * * **Box A box with a lid or an opening that contains the medium and other packaging, like posters and booklet containing lyrics.
 * * **Cardboard/Paper Sleeve A sleeve made of paper, paperboard, or cardboard. Traditional packaging for records, also seen with CDs.
 * * **Cassette Case Regular plastic case for a cassette.
 * * **Digibook A bounded booklet usually in hardcover with a sleeve bound to the spine of the book that houses a CD.
 * * **Digipak	A folded case, typically made of coated paperboard, with one or more plastic trays glued into it. Cases that can only be folded in half usually have only one plastic tray glued to it, while those that can be folded into thirds or more folds usually have multiple plastic trays glued to it.
 * * **Discbox Slider A pouch-like package with an internal mechanism that pushes the contents (usually a CD) out of the case when the lid flap is opened.
 * * **Fatbox  A double-sided, double-width jewel case normally holding 2 to 4 CDs, but (with Smart Tray or Brilliant Box inside) capable of up to 6 CDs.
 * * **Gatefold Cover A cardboard sleeve that folds in halves, thirds, etc. It can hold multiple records or CDs as well as booklets, posters and other memorabilia.
 * * **Jewel case The traditional CD case, made of hard, brittle plastic.
 * * **Keep Case The traditional DVD case, made of soft plastic (usually) dark grey with a thin transparent plastic cover protecting the artwork.
 * * **Plastic sleeve A sleeve made entirely of plastic that holds the medium and other parts of an album (e.g. liner notes and track list). The plastic sleeve is usually transparent. For paper sleeves that have a plastic part that shows the medium, please use "Cardboard/Paper Sleeve".
 * * **Slidepack A box with openings at its two ends that contains a tray (usually plastic) holding the medium. The box usually has a cutout to help the user to pull out the tray.
 * * **Slim Jewel Case	A thinner jewel case, commonly used for CD singles.
 * * **Snap Case A digipak-like case held together with a "snapping" plastic closure.
 * * **Super Jewel Box/Case Similar to the regular jewel case, but with rounded corners and a latch closing mechanism. Usually used for SACDs.
 * * **Other Other forms of packaging that are not described by the prescribed list.
 * * **None No packaging at all. Common for digital media (downloads).
 */
@JsonClass(generateAdapter = true)
public class Packaging(
  public val id: String = "",
  public val name: String = ""
) {
  public companion object {
    public val NullPackaging: Packaging = Packaging(id = NullObject.ID, name = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> = Packaging::class.java.name to NullPackaging
  }
}

public inline val Packaging.isNullObject: Boolean
  get() = this === Packaging.NullPackaging

public sealed class PackagingType(
  public val value: String,
  public val mbid: PackagingMbid
) {
  /** Book A book with a sleeve containing a medium (usually a CD). */
  public object Book : PackagingType("Book", PackagingMbid("d60b6157-79fe-3913-ab8b-23b32de8690d"))

  /**
   * Box A box with a lid or an opening that contains the medium and other packaging, like posters
   * and booklet containing lyrics.
   */
  public object Box : PackagingType("Box", PackagingMbid("c1668fc7-8944-4a00-bc3e-46e8d861d211"))

  /**
   * Cardboard/Paper Sleeve A sleeve made of paper, paperboard, or cardboard. Traditional packaging
   * for records, also seen with CDs.
   */
  public object CardboardPaperSleeve :
    PackagingType("Cardboard/Paper Sleeve", PackagingMbid("f7101ce3-0384-39ce-9fde-fbbd0044d35f"))

  /** Cassette Case Regular plastic case for a cassette. */
  public object CassetteCase :
    PackagingType("Cassette Case", PackagingMbid("c70b737a-0114-39a9-88f7-82843e54f906"))

  /**
   * Digibook A bounded booklet usually in hardcover with a sleeve bound to the spine of the book
   * that houses a CD.
   */
  public object Digibook :
    PackagingType("Digibook", PackagingMbid("9f2e13bc-f84f-428a-8342-fd86ece7fc4d"))

  /**
   * Digipak	A folded case, typically made of coated paperboard, with one or more plastic trays
   * glued into it. Cases that can only be folded in half usually have only one plastic tray glued
   * to it, while those that can be folded into thirds or more folds usually have multiple plastic
   * trays glued to it.
   */
  public object Digipak :
    PackagingType("Digipak", PackagingMbid("8f931351-d2e2-310f-afc6-37b89ddba246"))

  /**
   * Discbox Slider A pouch-like package with an internal mechanism that pushes the contents
   * (usually a CD) out of the case when the lid flap is opened.
   */
  public object DiscboxSlider :
    PackagingType("Discbox Slider", PackagingMbid("21179778-2f98-3d11-816e-42b469a0c924"))

  /**
   * Fatbox  A double-sided, double-width jewel case normally holding 2 to 4 CDs, but (with Smart
   * Tray or Brilliant Box inside) capable of up to 6 CDs.
   */
  public object Fatbox :
    PackagingType("Fatbox", PackagingMbid("57429523-ffe6-3336-9381-32565c142c18"))

  /**
   * Gatefold Cover A cardboard sleeve that folds in halves, thirds, etc. It can hold multiple
   * records or CDs as well as booklets, posters and other memorabilia.
   */
  public object GatefoldCover :
    PackagingType("Gatefold Cover", PackagingMbid("e724a489-a7e8-30a1-a17c-30dfd6831202"))

  /** Jewel case The traditional CD case, made of hard, brittle plastic. */
  public object Jewelcase :
    PackagingType("Jewel case", PackagingMbid("ec27701a-4a22-37f4-bfac-6616e0f9750a"))

  /**
   * Keep Case The traditional DVD case, made of soft plastic (usually) dark grey with a thin
   * transparent plastic cover protecting the artwork.
   */
  public object KeepCase :
    PackagingType("Keep Case", PackagingMbid("bb14bb17-e8ad-375f-a3c6-b1f82fd2bcc4"))

  /**
   * Plastic sleeve A sleeve made entirely of plastic that holds the medium and other parts of an
   * album (e.g. liner notes and track list). The plastic sleeve is usually transparent. For paper
   * sleeves that have a plastic part that shows the medium, please use "Cardboard/Paper Sleeve".
   */
  public object Plasticsleeve :
    PackagingType("Plastic sleeve", PackagingMbid("bf996342-d111-4d37-b9d6-d759f0787533"))

  /**
   * Slidepack A box with openings at its two ends that contains a tray (usually plastic) holding
   * the medium. The box usually has a cutout to help the user to pull out the tray.
   */
  public object Slidepack :
    PackagingType("Slidepack", PackagingMbid("2aee93e9-8acb-476c-807e-6a4a3974e1cb"))

  /** Slim Jewel Case	A thinner jewel case, commonly used for CD singles. */
  public object SlimJewelCase :
    PackagingType("Slim Jewel Case", PackagingMbid("36327bc2-f691-3d66-80e5-bd03cec6060a"))

  /** Snap Case A digipak-like case held together with a "snapping" plastic closure. */
  public object SnapCase :
    PackagingType("Snap Case", PackagingMbid("935f2847-8083-3422-8f0d-d7516fcda682"))

  /**
   * Super Jewel Box/Case Similar to the regular jewel case, but with rounded corners and a latch
   * closing mechanism. Usually used for SACDs.
   */
  public object SuperJewelBox :
    PackagingType("Super Jewel Box", PackagingMbid("dfb7da53-866f-4dfd-a016-80bafaeff3db"))

  /** Other Other forms of packaging that are not described by the prescribed list. */
  public object Other :
    PackagingType("Other", PackagingMbid("815b7785-8284-3926-8f04-e48bc6c4d102"))

  /** None No packaging at all. Common for digital media (downloads). */
  public object None : PackagingType("None", PackagingMbid("119eba76-b343-3e02-a292-f0f00644bb9b"))

  public class Unrecognized(name: String) :
    PackagingType(name, PackagingMbid(""))
}
