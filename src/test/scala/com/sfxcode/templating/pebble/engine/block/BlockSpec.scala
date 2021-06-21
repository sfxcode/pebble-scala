package com.sfxcode.templating.pebble.engine.block

import com.sfxcode.templating.pebble.{BuildInfo, ScalaPebbleEngine}

class BlockSpec extends munit.FunSuite {
  val Engine: ScalaPebbleEngine = ScalaPebbleEngine(globalContext = Map("info" -> BuildInfo))

  test("evaluate master") {
    val context   = Map("list" -> List("Element1", "Element2"))
    val evaluated = Engine.evaluateToString("templates/block/master.peb", context)
    assert(evaluated.contains("Master"))
  }

  test("evaluate base") {
    val context   = Map("list" -> List("Element1", "Element2"))
    val evaluated = Engine.evaluateToString("templates/block/base.peb", context)
    assert(evaluated.contains("Base"))

  }

  test("evaluate template") {
    val context   = Map("list" -> List("Element1", "Element2"))
    val evaluated = Engine.evaluateToString("templates/block/template.peb", context)
    assert(evaluated.contains("Base"))
    assert(evaluated.contains("Template"))

  }

}
