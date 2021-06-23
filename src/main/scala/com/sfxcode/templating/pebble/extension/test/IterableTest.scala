package com.sfxcode.templating.pebble.extension.test

import java.util

import com.mitchellbosecke.pebble.extension.Test
import com.mitchellbosecke.pebble.template.{EvaluationContext, PebbleTemplate}

case class IterableTest() extends Test {
  override def getArgumentNames: util.List[String] = null

  override def apply(
      input: Any,
      args: util.Map[String, AnyRef],
      self: PebbleTemplate,
      context: EvaluationContext,
      lineNumber: Int
  ): Boolean =
    input match {
      case _: java.lang.Iterable[_]  => true
      case _: collection.Iterable[_] => true
      case _: Array[_]               => true
      case _                         => false
    }
}

object IterableTest {
  val Name = "iterable"
}
