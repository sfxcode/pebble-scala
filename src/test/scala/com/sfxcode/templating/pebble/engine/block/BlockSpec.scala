package com.sfxcode.templating.pebble.engine.block

import com.sfxcode.templating.pebble.{BuildInfo, ScalaPebbleEngine}
import org.specs2.mutable.Specification

class BlockSpec extends Specification {
  val Engine: ScalaPebbleEngine = ScalaPebbleEngine(globalContext = Map("info" -> BuildInfo))
  sequential

  "ScalaPebbleEngine" should {

    "evaluate master" in {
      val context   = Map("list" -> List("Element1", "Element2"))
      val evaluated = Engine.evaluateToString("templates/block/master.peb", context)
      evaluated must contain("Master")
    }

    "evaluate base" in {
      val context   = Map("list" -> List("Element1", "Element2"))
      val evaluated = Engine.evaluateToString("templates/block/base.peb", context)
      evaluated must contain("Base")
    }

    "evaluate template" in {
      val context   = Map("list" -> List("Element1", "Element2"))
      val evaluated = Engine.evaluateToString("templates/block/template.peb", context)
      evaluated must contain("Base")
      evaluated must contain("Template")
    }
  }

}
