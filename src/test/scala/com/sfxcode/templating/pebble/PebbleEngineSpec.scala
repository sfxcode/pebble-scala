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

    "evaluate template from string and case class and tuple from context" in {

      val someBigLongValue: Long     = 99999L
      val otherLongValue: Long       = 6666L
      val niceDoubleValue: Double    = 25.25
      val aDescriptionString: String = "This is a simple string"

      val data      = TestCaseClass(someBigLongValue, otherLongValue, niceDoubleValue, aDescriptionString)
      val tuple     = ("Wisdom is the daughter of experience", "Leonardo da Vinci", 1452, 1519)
      val context   = Map("data" -> data, "tuple" -> tuple)
      val evaluated = Engine.evaluateToString("templates/engine/basic_injection.peb", context)
      evaluated must contain("This is a simple string")
      evaluated must contain("1452 - 1519")
      evaluated must contain("Wisdom is the daughter of experience")
      evaluated must contain("Leonardo da Vinci")
    }

  }

}

/** Simple Helper Case Class for Testing injection of case classes into pebble templates
  * The implementation is not main part of test case
  *
  * @param someBigLongValue - just a long value
  * @param otherLongValue - just another long value
  * @param niceDoubleValue - just a double value
  * @param aDescriptionString - just a String value
  */
case class TestCaseClass(
    someBigLongValue: Long,
    otherLongValue: Long,
    niceDoubleValue: Double,
    aDescriptionString: String
)
