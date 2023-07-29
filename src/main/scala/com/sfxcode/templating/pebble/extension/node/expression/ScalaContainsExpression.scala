package com.sfxcode.templating.pebble.extension.node.expression

import com.sfxcode.templating.pebble.compat._
import io.pebbletemplates.pebble.node.expression.ContainsExpression
import io.pebbletemplates.pebble.template.{EvaluationContextImpl, PebbleTemplateImpl}

import java.{lang, util}
import scala.jdk.CollectionConverters._

class ScalaContainsExpression extends ContainsExpression {
  override def evaluate(self: PebbleTemplateImpl, context: EvaluationContextImpl): lang.Boolean = {
    val leftValue = getLeftExpression.evaluate(self, context)
    if (leftValue == null) {
      return false
    }

    val rightValue = getRightExpression.evaluate(self, context)

    val (left, leftIsSeq) = leftValue match {
      case map: collection.Map[_, _] => (map.keys.toSeq, false)
      case map: util.Map[_, _]       => (map.keySet().asScala.toSeq, false)
      case seq: collection.Seq[_]    => (seq, true)
      case it: IterableOnce[_]       => (it.iterator.toSeq, true)
      case col: util.Collection[_]   => (col.asScala.toSeq, true)
      case xs                        => (xs, false)
    }

    val right = if (leftIsSeq) {
      rightValue match {
        case seq: collection.Seq[_]  => seq
        case it: IterableOnce[_]     => it.iterator.toSeq
        case col: util.Collection[_] => col.asScala.toSeq
        case xs                      => xs
      }
    } else {
      rightValue
    }

    (leftIsSeq, left, right) match {
      case (true, l: collection.Seq[_], r: collection.Seq[_]) => r.forall { l.contains }
      case (true, l: collection.Seq[_], r)                    => l.contains(r)
      case (false, l: collection.Seq[_], r)                   => l.contains(r)
      case _                                                  => super.evaluate(self, context)
    }
  }
}
