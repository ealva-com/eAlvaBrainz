/*
 * Copyright (c) 2021  Eric A. Snell
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

package com.ealva.brainzsvc.common

@Suppress("NOTHING_TO_INLINE")
private inline fun StringBuilder.appendPlus() = apply {
  append('+')
}

/**
 * A builder for the "toc" parameter of a discid lookup.
 */
public class TocParam private constructor(
  private val leadoutSectorOffset: Int,
  private val firstSectorOffset: Int,
  private val sectorOffsets: IntArray
) {
  /**
   * The TOC consists of the following:
   * * First track (always 1)
   * * total number of tracks
   * * sector offset of the leadout (end of the disc)
   * * a list of sector offsets for each track, beginning with track 1 (generally 150 sectors)
   *
   * Example:1+12+267257+150+22767+41887+58317+72102+91375+104652+115380+132165+143932+159870+174597
   */
  override fun toString(): String = buildString {
    append(1).appendPlus()
    append(sectorOffsets.size + 1).appendPlus()
    append(leadoutSectorOffset).appendPlus()
    append(firstSectorOffset)
    if (sectorOffsets.isNotEmpty()) {
      sectorOffsets.joinTo(buffer = this, separator = "+", prefix = "+") { it.toString() }
    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as TocParam

    if (leadoutSectorOffset != other.leadoutSectorOffset) return false
    if (!sectorOffsets.contentEquals(other.sectorOffsets)) return false

    return true
  }

  override fun hashCode(): Int {
    var result = leadoutSectorOffset
    result = 31 * result + sectorOffsets.contentHashCode()
    return result
  }

  public companion object {
    /**
     * Make TocParam with [leadoutSectorOffset] (end of disc), the [firstSectorOffset] which is
     * required, and any remaining [sectorOffsets].
     *
     * The TOC consists of the following:
     * * First track (always 1)
     * * total number of tracks (taken from sectorOffsets.size + 1
     * * sector offset of the leadout (end of the disc) [leadoutSectorOffset]
     * * a list of sector offsets for each track, beginning with track 1 (generally 150 sectors)
     * (taken from [firstSectorOffset] and any remaining [sectorOffsets]
     */
    public operator fun invoke(
      leadoutSectorOffset: Int,
      firstSectorOffset: Int,
      vararg sectorOffsets: Int
    ): TocParam {
      require(leadoutSectorOffset > 0) { "leadout sector offset must be a positive number" }
      return TocParam(leadoutSectorOffset, firstSectorOffset, sectorOffsets)
    }
  }
}
