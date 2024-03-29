package com.sfxcode.templating.pebble.engine.test

import com.sfxcode.templating.pebble.{BuildInfo, ScalaPebbleEngine}

import scala.collection.mutable.ArrayBuffer

class PebbleTestSuite extends munit.FunSuite {
  val Engine: ScalaPebbleEngine = ScalaPebbleEngine(globalContext = Map("info" -> BuildInfo))

  test("evaluate master") {
    val buffer: ArrayBuffer[String] = new ArrayBuffer()
    val context                     = Map("list" -> List(), "map" -> Map(), "option" -> None, "buffer" -> buffer, "set" -> Set())
    val evaluated                   = Engine.evaluateToString("templates/test/empty.peb", context)
    assert(evaluated.contains("emptyList"))
    assert(evaluated.contains("emptyMap"))
    assert(evaluated.contains("emptyOption"))
    assert(evaluated.contains("emptyBuffer"))
    assert(evaluated.contains("emptySet"))
  }

}
