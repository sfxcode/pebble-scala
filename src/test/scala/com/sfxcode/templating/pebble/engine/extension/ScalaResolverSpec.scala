package com.sfxcode.templating.pebble.engine.extension

import java.util

import com.sfxcode.templating.pebble.ScalaPebbleEngine
import org.specs2.mutable.Specification

import scala.collection.mutable.ArrayBuffer

class ScalaResolverSpec extends Specification {
  val Engine: ScalaPebbleEngine = ScalaPebbleEngine(useStringLoader = true)

  sequential

  "ScalaPebbleEngine" should {

    "evaluate Option in" in {
      Engine.evaluateToString("{{ option[0] }}", Map("option" -> Some("Element1"))) mustEqual "Element1"
      Engine.evaluateToString("{{ option._ }}", Map("option"  -> Some("Element1"))) mustEqual "Element1"
      Engine.evaluateToString("{{ option[0] }}", Map("option" -> None)) mustEqual ""
      Engine.evaluateToString("{{ option._ }}", Map("option"  -> None)) mustEqual ""
    }

    "evaluate scala collections in" in {
      Engine.evaluateToString("{{ list[0] }}", Map("list"   -> List("Element1"))) mustEqual "Element1"
      Engine.evaluateToString("{{ list[1] }}", Map("list"   -> List("Element1"))) mustEqual ""
      Engine.evaluateToString("{{ set[0] }}", Map("set"     -> Set("Element1"))) mustEqual "Element1"
      Engine.evaluateToString("{{ array[0] }}", Map("array" -> Array("Element1"))) mustEqual "Element1"
      val buffer = new ArrayBuffer[String]()
      buffer.+=("Element1")
      Engine.evaluateToString("{{ buffer[0] }}", Map("buffer" -> buffer)) mustEqual "Element1"

    }

    "evaluate java collections in" in {
      val list = new util.ArrayList[String]()
      list.add("Element1")
      Engine.evaluateToString("{{ list[0] }}", Map("list" -> list)) mustEqual "Element1"
      Engine.evaluateToString("{{ list[1] }}", Map("list" -> list)) mustEqual ""

    }

    "evaluate scala maps in" in {
      Engine.evaluateToString("{{ map.key1 }}", Map("map" -> Map("key1"                    -> "Element1"))) mustEqual "Element1"
      Engine.evaluateToString("{{ map.key1 }}", Map("map" -> collection.mutable.Map("key1" -> "Element1"))) mustEqual "Element1"
      Engine.evaluateToString("{{ map.key2 }}", Map("map" -> Map("key1"                    -> "Element1"))) mustEqual ""
    }

    "evaluate java maps in" in {
      val map = new util.HashMap[String, String]()
      map.put("key1", "Element1")
      Engine.evaluateToString("{{ map.key1 }}", Map("map" -> map)) mustEqual "Element1"
      Engine.evaluateToString("{{ map.key2 }}", Map("map" -> map)) mustEqual ""
    }

    "evaluate scala BigInteger / BigDecimal in" in {
      Engine.evaluateToString("{{ bi }}", Map("bi" -> BigInt(42))) mustEqual "42"
      Engine.evaluateToString("{{ bd }}", Map("bd" -> BigDecimal(42.5))) mustEqual "42.5"
    }
    
    "evaluate scala case class in" in {

      case class Foo(bar: String)
      val result = Engine.evaluateToString("{{ caseClass.bar }}", Map("caseClass" -> Foo("FooBar")))
      result mustEqual "FooBar"
    }

    "evaluate scala class in" in {

      class Foo(val bar: String) {
        def getFooBarString = s"Foo is $bar"
      }
      
      val result = Engine.evaluateToString("{{ Class.getFooBarString }}", Map("Class" -> new Foo("FooBar")))
      result mustEqual "Foo is FooBar"
    }
  }

}
