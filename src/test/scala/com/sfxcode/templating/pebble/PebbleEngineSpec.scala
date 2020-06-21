package com.sfxcode.templating.pebble

import org.specs2.mutable.Specification

class PebbleEngineSpec extends Specification {
  val Engine: ScalaPebbleEngine = ScalaPebbleEngine(globalContext = Map("info" -> BuildInfo))
  sequential

  "ScalaPebbleEngine" should {

    "evaluate template from string in" in {
      val context   = Map("list" -> List("Element1", "Element2"))
      val evaluated = Engine.evaluateToString("templates/engine/basic.peb", context)
      evaluated must contain("<title>pebble-scala</title>")
      evaluated must contain("<li>Element1</li>")
    }
  }

}
