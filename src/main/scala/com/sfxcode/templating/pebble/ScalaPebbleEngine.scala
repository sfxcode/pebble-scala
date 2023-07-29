package com.sfxcode.templating.pebble

import java.io.{StringWriter, Writer}
import java.util.Locale

import io.pebbletemplates.pebble.PebbleEngine
import io.pebbletemplates.pebble.extension.Extension
import io.pebbletemplates.pebble.loader.StringLoader
import io.pebbletemplates.pebble.template.PebbleTemplate
import com.sfxcode.templating.pebble.extension.ScalaExtension

import scala.jdk.CollectionConverters._

class ScalaPebbleEngine(useStringLoader: Boolean, globalContext: Map[String, AnyRef]) {

  private val PebbleBuilder             = new PebbleEngine.Builder()
  private var engine: Option[PebbleEngine] = None

  PebbleBuilder
    .extension(getExtension)
    .allowOverrideCoreOperators(true)

  if (useStringLoader) {
    PebbleBuilder.loader(new StringLoader())
  }

  def getExtension: Extension =
    new ScalaExtension(globalContext)

  def getBuilder: PebbleEngine.Builder = {
    if (engine.isDefined)
      throw new IllegalAccessError("access ot builder after engine initialization is not permitted")
    PebbleBuilder
  }

  def getEngine: PebbleEngine = {
    if (engine.isEmpty) {
      engine = Some(getBuilder.build())
    }
    engine.get
  }

  def loadTemplate(template: String): PebbleTemplate =
    getEngine.getTemplate(template)

  def evaluateToString(
      template: String,
      context: Map[String, AnyRef] = Map(),
      locale: Locale = getEngine.getDefaultLocale
  ): String = {
    val writer: Writer = evaluate(new StringWriter, template, context, locale)
    writer.toString
  }

  def evaluate(
      writer: Writer,
      template: String,
      context: Map[String, AnyRef] = Map(),
      locale: Locale = getEngine.getDefaultLocale
  ): Writer = {
    val javaContext = context.asJava
    loadTemplate(template).evaluate(writer, javaContext, locale)
    writer
  }

}

object ScalaPebbleEngine {
  def apply(useStringLoader: Boolean = false, globalContext: Map[String, AnyRef] = Map()) =
    new ScalaPebbleEngine(useStringLoader, globalContext)
}
