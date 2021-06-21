package com.sfxcode.templating.pebble

class PebbleEngineSpec extends munit.FunSuite {
  val Engine: ScalaPebbleEngine = ScalaPebbleEngine(globalContext = Map("info" -> BuildInfo))

  test("evaluate template from string)") {
    val context   = Map("list" -> List("Element1", "Element2"))
    val evaluated = Engine.evaluateToString("templates/engine/basic.peb", context)
    assert(evaluated.contains("<title>pebble-scala</title>"))
    assert(evaluated.contains("<li>Element1</li>"))
  }

  test("evaluate template from string and case class and tuple from context") {

    val someBigLongValue: Long     = 99999L
    val otherLongValue: Long       = 6666L
    val niceDoubleValue: Double    = 25.25
    val aDescriptionString: String = "This is a simple string"

    val data      = TestCaseClass(someBigLongValue, otherLongValue, niceDoubleValue, aDescriptionString)
    val tuple     = ("Wisdom is the daughter of experience", "Leonardo da Vinci", 1452, 1519)
    val context   = Map("data" -> data, "tuple" -> tuple)
    val evaluated = Engine.evaluateToString("templates/engine/basic_injection.peb", context)
    assert(evaluated.contains("This is a simple string"))
    assert(evaluated.contains("1452 - 1519"))
    assert(evaluated.contains("Wisdom is the daughter of experience"))
    assert(evaluated.contains("Leonardo da Vinci"))
  }

}

/** Simple Helper Case Class for Testing)jection of case classes)to pebble templates
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
