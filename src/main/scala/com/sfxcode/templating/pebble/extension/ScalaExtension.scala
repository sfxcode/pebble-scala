package com.sfxcode.templating.pebble.extension

import java.util

import com.mitchellbosecke.pebble.attributes.AttributeResolver

import scala.jdk.CollectionConverters._
import com.mitchellbosecke.pebble.extension.{AbstractExtension, Test}
import com.mitchellbosecke.pebble.tokenParser.TokenParser
import com.sfxcode.templating.pebble.extension.test.{EmptyTest, IterableTest, MapTest}
import com.sfxcode.templating.pebble.extension.tokenParser.ForTokenParser

case class ScalaExtension(globalContext: Map[String, AnyRef] = Map()) extends AbstractExtension {
  override def getGlobalVariables: util.Map[String, AnyRef] = globalContext.asJava

  override def getAttributeResolver: util.List[AttributeResolver] =
    List[AttributeResolver](ScalaResolver()).asJava

  override def getTests: util.Map[String, Test] =
    Map[String, Test]("empty" -> EmptyTest(), "map" -> MapTest(), "iterable" -> IterableTest()).asJava

  override def getTokenParsers: util.List[TokenParser] =
    List[TokenParser](ForTokenParser()).asJava
}
