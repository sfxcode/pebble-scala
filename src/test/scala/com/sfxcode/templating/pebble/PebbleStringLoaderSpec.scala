package com.sfxcode.templating.pebble

class PebbleStringLoaderSpec extends munit.FunSuite {
  val Engine: ScalaPebbleEngine = ScalaPebbleEngine(useStringLoader = true)

  test("evaluate template from string in") {
    val context   = Map("set" -> Set("Element1", "Element2"))
    val evaluated = Engine.evaluateToString("{{ set[0] }}", context)
    assertEquals(evaluated, "Element1")
  }

}
