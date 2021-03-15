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

package com.ealva.ealvabrainz.lucene

public interface Expression {
  public fun appendTo(builder: StringBuilder): StringBuilder
}

public fun StringBuilder.appendExpression(expression: Expression): StringBuilder =
  expression.appendTo(this)

public abstract class BaseExpression : Expression {
  public override fun toString(): String = buildString { appendExpression(this@BaseExpression) }

  /*
   * Identity equality seems OK for now. Current implementation fields are stored in a
   * ReferenceSet
   */

//  override fun equals(other: Any?): Boolean {
//    return when {
//      this === other -> true
//      javaClass != other?.javaClass -> false
//      else -> toString() == other.toString()
//    }
//  }
//
//  override fun hashCode(): Int {
//    return toString().hashCode()
//  }
}
