package com.sfxcode.templating.pebble

import java.io.{StringWriter, Writer}
import java.util.Locale

import com.mitchellbosecke.pebble.PebbleEngine
import com.mitchellbosecke.pebble.extension.Extension
import com.mitchellbosecke.pebble.loader.StringLoader
import com.mitchellbosecke.pebble.template.PebbleTemplate
import com.sfxcode.templating.pebble.extension.ScalaExtension

import scala.jdk.CollectionConverters._

case class ScalaPebbleEngine(useStringLoader: Boolean = false, globalContext: Map[String, AnyRef] = Map()) {

  private val PebbleBuilder             = new PebbleEngine.Builder()
  private val scalaExtension: Extension = ScalaExtension(globalContext)

  PebbleBuilder.extension(scalaExtension)
  private var engine: Option[PebbleEngine] = None

  def getBuilder: PebbleEngine.Builder = {
    if (engine.isDefined) {
      throw new IllegalAccessError("access ot builder after engine initialization is not permitted")
    }
    PebbleBuilder
  }

  def getEngine: PebbleEngine = {
    if (engine.isEmpty) {
      if (useStringLoader) {
        getBuilder.loader(new StringLoader())
      }
      engine = Some(PebbleBuilder.build())
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
