package com.sfxcode.templating.pebble.extension

import com.mitchellbosecke.pebble.attributes.{AttributeResolver, ResolvedAttribute}
import com.mitchellbosecke.pebble.node.ArgumentsNode
import com.mitchellbosecke.pebble.template.EvaluationContextImpl

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
      case anyObj: Any if anyObj.isInstanceOf[Product] =>
        new ResolvedAttribute(anyObj.getClass.getDeclaredFields.map(_.getName).zip(anyObj.asInstanceOf[Product].productIterator.toSeq).toMap)
      case _ => null
    }

}
