package com.sfxcode.templating.pebble.engine.extension

import com.sfxcode.templating.pebble.ScalaPebbleEngine

import java.util
import scala.collection.mutable.ArrayBuffer

class ScalaResolverSpec extends munit.FunSuite {
  val Engine: ScalaPebbleEngine = ScalaPebbleEngine(useStringLoader = true)

  test("evaluate Option)") {
    assertEquals(Engine.evaluateToString("{{ option[0] }}", Map("option" -> Some("Element1"))), "Element1")
    assertEquals(Engine.evaluateToString("{{ option._ }}", Map("option" -> Some("Element1"))), "Element1")
    assertEquals(Engine.evaluateToString("{{ option[0] }}", Map("option" -> None)), "")
    assertEquals(Engine.evaluateToString("{{ option._ }}", Map("option" -> None)), "")
  }

  test("evaluate scala collections)") {
    assertEquals(Engine.evaluateToString("{{ list[0] }}", Map("list" -> List("Element1"))), "Element1")
    assertEquals(Engine.evaluateToString("{{ list[1] }}", Map("list" -> List("Element1"))), "")
    assertEquals(Engine.evaluateToString("{{ set[0] }}", Map("set" -> Set("Element1"))), "Element1")
    assertEquals(Engine.evaluateToString("{{ array[0] }}", Map("array" -> Array("Element1"))), "Element1")
    val buffer = new ArrayBuffer[String]()
    buffer.+=("Element1")
    assertEquals(Engine.evaluateToString("{{ buffer[0] }}", Map("buffer" -> buffer)), "Element1")

  }

  test("evaluate java collections)") {
    val list = new util.ArrayList[String]()
    list.add("Element1")
    assertEquals(Engine.evaluateToString("{{ list[0] }}", Map("list" -> list)), "Element1")
    assertEquals(Engine.evaluateToString("{{ list[1] }}", Map("list" -> list)), "")

  }

  test("evaluate scala maps)") {
    assertEquals(Engine.evaluateToString("{{ map.key1 }}", Map("map" -> Map("key1" -> "Element1"))), "Element1")
    assertEquals(
      Engine.evaluateToString(
        "{{ map.key1 }}",
        Map("map" -> collection.mutable.Map("key1" -> "Element1"))
      ),
      "Element1"
    )
    assertEquals(Engine.evaluateToString("{{ map.key2 }}", Map("map" -> Map("key1" -> "Element1"))), "")
  }

  test("evaluate java maps)") {
    val map = new util.HashMap[String, String]()
    map.put("key1", "Element1")
    assertEquals(Engine.evaluateToString("{{ map.key1 }}", Map("map" -> map)), "Element1")
    assertEquals(Engine.evaluateToString("{{ map.key2 }}", Map("map" -> map)), "")
  }

  test("evaluate scala BigInteger / BigDecimal)") {
    assertEquals(Engine.evaluateToString("{{ bi }}", Map("bi" -> BigInt(42))), "42")
    assertEquals(Engine.evaluateToString("{{ bd }}", Map("bd" -> BigDecimal(42.5))), "42.5")
  }

  test("evaluate scala case class)") {

    case class Foo(bar: String)
    val result = Engine.evaluateToString("{{ caseClass.bar }}", Map("caseClass" -> Foo("FooBar")))
    assertEquals(result, "FooBar")
  }

  test("evaluate scala class)") {

    class Foo(val bar: String) {
      def getFooBarString = s"Foo is $bar"
    }

    val result = Engine.evaluateToString("{{ Class.getFooBarString }}", Map("Class" -> new Foo("FooBar")))
    assertEquals(result, "Foo is FooBar")
  }

  test("evaluate scala tuple)") {

    val fooBar: (String, Int, Double) = ("Test String", 1, 3.14)
    val result                        = Engine.evaluateToString("{{ fooBar._1 }}: {{ fooBar._2 }}", Map("fooBar" -> fooBar))
    assertEquals(result, "Test String: 1")
  }

}
