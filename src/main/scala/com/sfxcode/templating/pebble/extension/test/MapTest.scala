package com.sfxcode.templating.pebble.extension.test

import java.util

import io.pebbletemplates.pebble.extension.Test
import io.pebbletemplates.pebble.template.{EvaluationContext, PebbleTemplate}

case class MapTest() extends Test {
  override def getArgumentNames: util.List[String] = null

  override def apply(
      input: Any,
      args: util.Map[String, AnyRef],
      self: PebbleTemplate,
      context: EvaluationContext,
      lineNumber: Int
  ): Boolean =
    input match {
      case _: java.util.Map[_, _]  => true
      case _: collection.Map[_, _] => true
      case _: Array[_]             => true
      case _                       => false
    }
}

object MapTest {
  val Name = "map"
}
