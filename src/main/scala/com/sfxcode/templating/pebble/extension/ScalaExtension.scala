package com.sfxcode.templating.pebble.extension

import java.util
import io.pebbletemplates.pebble.attributes.AttributeResolver

import scala.jdk.CollectionConverters._
import io.pebbletemplates.pebble.extension.{AbstractExtension, Test}
import io.pebbletemplates.pebble.tokenParser.TokenParser
import com.sfxcode.templating.pebble.extension.node.expression._
import com.sfxcode.templating.pebble.extension.test._
import com.sfxcode.templating.pebble.extension.tokenParser.{DoTokenParser, ForTokenParser}
import io.pebbletemplates.pebble.operator.{Associativity, BinaryOperator, BinaryOperatorImpl, BinaryOperatorType}

class ScalaExtension(globalContext: Map[String, AnyRef] = Map()) extends AbstractExtension {

  override def getGlobalVariables: util.Map[String, AnyRef] = globalContext.asJava

  override def getAttributeResolver: util.List[AttributeResolver] =
    List[AttributeResolver](new ScalaResolver()).asJava

  override def getTests: util.Map[String, Test] =
    Map[String, Test](
      EmptyTest.Name    -> EmptyTest(),
      MapTest.Name      -> MapTest(),
      IterableTest.Name -> IterableTest()
    ).asJava

  override def getTokenParsers: util.List[TokenParser] =
    List[TokenParser](ForTokenParser(), DoTokenParser()).asJava

  override def getBinaryOperators: util.List[BinaryOperator] =
    List[BinaryOperator](
      new BinaryOperatorImpl("contains", 20, () => new ScalaContainsExpression(), BinaryOperatorType.NORMAL, Associativity.LEFT)
    ).asJava
}
