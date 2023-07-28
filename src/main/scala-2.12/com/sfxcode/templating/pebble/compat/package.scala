package com.sfxcode.templating.pebble

package object compat {

  type IterableOnce[T] = TraversableOnce[T]

  class IterableOnceOps[T](val x: IterableOnce[T]) extends AnyVal {
    def iterator: Iterator[T] = x.toIterator
  }

  implicit def iterableOnceOps[T](x: IterableOnce[T]) = new IterableOnceOps[T](x)

}