package com.sfxcode.templating.pebble

import org.specs2.mutable.Specification

class PebbleStringLoaderSpec extends Specification {
  val Engine: ScalaPebbleEngine = ScalaPebbleEngine(useStringLoader = true)

  sequential

  "ScalaPebbleEngine" should {

    "evaluate template from string in" in {
      val context   = Map("set" -> Set("Element1", "Element2"))
      val evaluated = Engine.evaluateToString("{{ set[0] }}", context)
      evaluated mustEqual "Element1"
    }
  }

}
