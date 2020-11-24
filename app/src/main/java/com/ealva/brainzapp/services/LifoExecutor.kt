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

package com.ealva.brainzapp.services

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.BlockingQueue
import java.util.concurrent.PriorityBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

private data class Pair(val runnable: Runnable, val timestamp: Long = System.currentTimeMillis())

private const val INITIAL_CAPACITY = 1024

fun LifecycleOwner.lifoSingleThreadExecutor(): ThreadPoolExecutor {
  return ThreadPoolExecutor(
    1,
    1,
    0L,
    TimeUnit.MILLISECONDS,
    object : BlockingQueue<Runnable> {
      private val backingQueue: BlockingQueue<Pair> =
        PriorityBlockingQueue(INITIAL_CAPACITY) { arg1, arg2 ->
          // compare in reverse to get oldest first. Could also do
          // -arg1.timestamp.compareTo(arg2.timestamp)
          arg2.timestamp.compareTo(arg1.timestamp)
        }

      override fun add(element: Runnable): Boolean {
        return backingQueue.add(Pair(element))
      }

      override fun offer(r: Runnable): Boolean {
        return backingQueue.offer(Pair(r))
      }

      override fun offer(r: Runnable, timeout: Long, unit: TimeUnit?): Boolean {
        return backingQueue.offer(Pair(r), timeout, unit)
      }

      override fun contains(element: Runnable): Boolean {
        return backingQueue.any { it.runnable === element }
      }

      override fun take(): Runnable {
        return backingQueue.take().runnable
      }

      override fun put(e: Runnable) {
        backingQueue.put(Pair(e))
      }

      override fun remove(element: Runnable): Boolean {
        return backingQueue.removeAll { it.runnable === element }
      }

      override fun drainTo(c: MutableCollection<in Runnable>): Int {
        val size = backingQueue.size
        backingQueue.forEach { c.add(it.runnable) }
        return size
      }

      override fun drainTo(c: MutableCollection<in Runnable>?, maxElements: Int): Int {
        TODO("Not yet implemented")
      }

      override fun remainingCapacity(): Int {
        return backingQueue.remainingCapacity()
      }

      override fun poll(timeout: Long, unit: TimeUnit): Runnable {
        return backingQueue.poll(timeout, unit).runnable
      }

      override fun remove(): Runnable {
        return backingQueue.remove().runnable
      }

      override fun poll(): Runnable? {
        return backingQueue.poll()?.runnable
      }

      override fun containsAll(elements: Collection<Runnable>): Boolean {
        return backingQueue.map { it.runnable }.containsAll(elements)
      }

      override fun addAll(elements: Collection<Runnable>): Boolean {
        elements.forEach { backingQueue.add(Pair(it)) }
        return true
      }

      override fun clear() {
        backingQueue.clear()
      }

      override fun element(): Runnable {
        return backingQueue.element().runnable
      }

      override val size: Int
        get() = backingQueue.size

      override fun isEmpty(): Boolean {
        return backingQueue.isEmpty()
      }

      override fun iterator(): MutableIterator<Runnable> {
        return backingQueue.map { it.runnable }.toMutableList().listIterator()
      }

      override fun removeAll(elements: Collection<Runnable>): Boolean {
        return backingQueue.removeAll { elements.contains(it.runnable) }
      }

      override fun peek(): Runnable? {
        return backingQueue.peek()?.runnable
      }

      override fun retainAll(elements: Collection<Runnable>): Boolean {
        return backingQueue.retainAll { elements.contains(it.runnable) }
      }
    }
  ).also { executor ->
    lifecycle.addObserver(
      object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
          executor.shutdownNow()
        }
      }
    )
  }
}
