package com.sfxcode.templating.pebble.extension

import io.pebbletemplates.pebble.attributes.{AttributeResolver, ResolvedAttribute}
import io.pebbletemplates.pebble.node.ArgumentsNode
import io.pebbletemplates.pebble.template.EvaluationContextImpl

case class ScalaResolver() extends AttributeResolver {
  override def resolve(
      obj: Any,
      attributeNameValue: Any,
      argumentValues: Array[AnyRef],
      args: ArgumentsNode,
      context: EvaluationContextImpl,
      filename: String,
      lineNumber: Int
  ): ResolvedAttribute =
    obj match {

      case map: collection.Map[String, _] if map.keySet.contains(attributeNameValue.toString) =>
        new ResolvedAttribute(map(attributeNameValue.toString))
      case it: collection.Iterable[_]
          if !it.isInstanceOf[Map[String, Any]] && it.size > attributeNameValue.toString.toInt =>
        new ResolvedAttribute(it.toList(attributeNameValue.toString.toInt))
      case option: Option[_] if option.isDefined =>
        new ResolvedAttribute(option.get)
      case p: Product =>
        val declaredFieldsNames = p.getClass.getDeclaredFields.map(_.getName)
        val productSeq          = p.productIterator.toSeq
        val map                 = declaredFieldsNames.zip(productSeq).toMap
        resolve(map, attributeNameValue, argumentValues, args, context, filename, lineNumber)
      case _ => null
    }

}
