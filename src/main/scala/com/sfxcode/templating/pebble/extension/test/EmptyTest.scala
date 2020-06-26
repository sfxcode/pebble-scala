package com.sfxcode.templating.pebble.extension.test

import java.util
import java.util.Map

import com.mitchellbosecke.pebble.extension.Test
import com.mitchellbosecke.pebble.template.{EvaluationContext, PebbleTemplate}

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
      case option: Option[_]       => option.isEmpty
      case col: util.Collection[_] => col.isEmpty
      case map: util.Map[_, _]     => map.isEmpty
      case it: Iterator[_]         => it.isEmpty
      case map: Map[_, _]          => map.isEmpty
      case Nil                     => true
      case _: Any                  => false
      case _                       => true
    }
}
