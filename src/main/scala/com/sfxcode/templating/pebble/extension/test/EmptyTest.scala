package com.sfxcode.templating.pebble.extension.test

import java.util

import com.sfxcode.templating.pebble.compat._
import io.pebbletemplates.pebble.extension.Test
import io.pebbletemplates.pebble.template.{EvaluationContext, PebbleTemplate}

case class EmptyTest() extends Test {
  override def getArgumentNames: util.List[String] = null

  override def apply(
      input: Any,
      args: util.Map[String, AnyRef],
      self: PebbleTemplate,
      context: EvaluationContext,
      lineNumber: Int
  ): Boolean =
    input match {
      case s: String               => s.isEmpty
      case opt: Option[_]          => opt.isEmpty
      case col: util.Collection[_] => col.isEmpty
      case map: util.Map[_, _]     => map.isEmpty
      case it: IterableOnce[_]     => it.iterator.isEmpty
      case _: Any                  => false
      case _                       => true
    }
}

object EmptyTest {
  val Name = "empty"
}
