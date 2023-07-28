package com.sfxcode.templating.pebble.engine.extension

import com.sfxcode.templating.pebble.ScalaPebbleEngine
import munit.Location

import java.util
import scala.collection.mutable

class ExpressionSuite extends munit.FunSuite {
  val Engine: ScalaPebbleEngine = ScalaPebbleEngine(useStringLoader = true)

  case class Data[T](x: T)

  private def autobox(map: Map[String, Any]): Map[String, AnyRef] =
    map.map { case (k, v) => k -> v.asInstanceOf[AnyRef] }

  private def assertContains[A, B](left: A, right: B)(implicit loc: Location) =
    assertEquals(Engine.evaluateToString("{% if left contains right %}yes{% endif %}", autobox(Map("left" -> left, "right" -> right))), "yes")

  private def assertNotContains[A, B](left: A, right: B)(implicit loc: Location) =
    assertNotEquals(Engine.evaluateToString("{% if left contains right %}yes{% endif %}", autobox(Map("left" -> left, "right" -> right))), "yes")

  // needed for scala 2.12, which gets consfused by some of `util.List.of` signatures
  private def javaList[T](xs: T*) = util.List.of(xs: _*)

  test("simple collection contains expression") {
    assertContains(List(1, 2, 3), 2)
    assertNotContains(List(1, 2, 3), 4)
    assertNotContains(List(1, 2, 3), "2")

    assertContains(mutable.Set(1, 2, 3), 2)
    assertNotContains(mutable.Set(1, 2, 3), 4)
    assertNotContains(mutable.Set(1, 2, 3), "2")

    assertContains(javaList(1, 2, 3), 2)
    assertNotContains(javaList(1, 2, 3), 4)
    assertNotContains(javaList(1, 2, 3), "2")

    assertContains(List(Data("a")), Data("a"))
    assertNotContains(List(Data("a")), Data(1))

    // tuples are sub-classes of Product and are treated like other non-collections
    assertContains(List("a" -> 1, "b" -> 2), "a" -> 1)
    assertNotContains(List("a" -> 1, "b" -> 2), "a" -> 2)
  }

  test("collection contains collection expression") {
    assertContains(List(1, 2, 3), List(2))
    assertContains(List(1, 2, 3), List(1, 3))
    assertNotContains(List(1, 2, 3), List(1, 4))

    assertContains(List(1, 2, 3), mutable.Set(2))
    assertContains(List(1, 2, 3), mutable.Set(1, 3))
    assertNotContains(List(1, 2, 3), mutable.Set(1, 4))

    assertContains(javaList(1, 2, 3), Set(2))
    assertContains(javaList(1, 2, 3), Set(1, 3))
    assertNotContains(javaList(1, 2, 3), Set(1, 4))

    assertContains(List(1, 2, 3), javaList(2))
    assertContains(List(1, 2, 3), javaList(1, 3))
    assertNotContains(List(1, 2, 3), javaList(1, 4))

    assertContains(javaList(1, 2, 3), javaList(2))
    assertContains(javaList(1, 2, 3), javaList(1, 3))
    assertNotContains(javaList(1, 2, 3), javaList(1, 4))
  }

  test("simple map contains expression") {
    assertContains(Map(1 -> "a", 2 -> "b", 3 -> "c"), 2)
    assertNotContains(Map(1 -> "a", 2 -> "b", 3 -> "c"), 4)
    assertNotContains(Map(1 -> "a", 2 -> "b", 3 -> "c"), "2")

    assertContains(util.Map.of(1, "a", 2 , "b", 3, "c"), 2)
    assertNotContains(util.Map.of(1, "a", 2 , "b", 3, "c"), 4)
    assertNotContains(util.Map.of(1, "a", 2 , "b", 3, "c"), "2")

    // "contains all"-type comparisons between map and list are not supported by pebble's own "contains"
    assertNotContains(Map(1 -> "a", 2 -> "b", 3 -> "c"), List(2))
  }

  test("evaluated contains expressions") {
    assertEquals(Engine.evaluateToString("{% if a contains 1 %}yes{% endif %}", Map("a" -> List(1))), "yes")
    assertNotEquals(Engine.evaluateToString("{% if a contains 2 %}yes{% endif %}", Map("a" -> List(1))), "yes")

    assertEquals(Engine.evaluateToString("{% if [1, 2] contains a %}yes{% endif %}", Map("a" -> List(1))), "yes")
    assertEquals(Engine.evaluateToString("{% if [1, 2] contains a %}yes{% endif %}", Map("a" -> List(1, 2))), "yes")
    assertNotEquals(Engine.evaluateToString("{% if [1, 2] contains a %}yes{% endif %}", Map("a" -> List(3))), "yes")

    assertEquals(Engine.evaluateToString("""{% if {"a": 1} contains a %}yes{% endif %}""", Map("a" -> "a")), "yes")
    assertNotEquals(Engine.evaluateToString("""{% if {"a": 1} contains a %}yes{% endif %}""", Map("a" -> Int.box(7))), "yes")
  }
}
