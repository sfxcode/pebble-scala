package com.sfxcode.templating.pebble.engine.token

import com.sfxcode.templating.pebble._

class DoTokenSuite extends munit.FunSuite {
  val Engine: ScalaPebbleEngine = ScalaPebbleEngine()

  test("evaluate template from string)") {
    val evaluated: String = Engine.evaluateToString("templates/token/do_token.peb")
    assert(evaluated.contains("<li>item1</li>\n<li>item2</li>"))
  }

}
