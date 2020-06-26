package com.sfxcode.templating.pebble.engine.test

import com.sfxcode.templating.pebble.{BuildInfo, ScalaPebbleEngine}
import org.specs2.mutable.Specification

import scala.collection.mutable.ArrayBuffer

class PebbleTestSpec extends Specification {
  val Engine: ScalaPebbleEngine = ScalaPebbleEngine(globalContext = Map("info" -> BuildInfo))
  sequential

  "ScalaPebbleEngine" should {

    "evaluate master" in {
      val buffer: ArrayBuffer[String] = new ArrayBuffer()
      val context                     = Map("list" -> List(), "map" -> List(), "option" -> None, "buffer" -> buffer)
      val evaluated                   = Engine.evaluateToString("templates/test/empty.peb", context)
      evaluated must contain("emptyList")
      evaluated must contain("emptyMap")
      evaluated must contain("emptyOption")
      evaluated must contain("emptyBuffer")
    }

  }

}
