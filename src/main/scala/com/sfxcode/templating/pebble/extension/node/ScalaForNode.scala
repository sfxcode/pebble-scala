package com.sfxcode.templating.pebble.extension.node

import java.io.Writer
import java.lang.reflect.Array
import java.util

import com.mitchellbosecke.pebble.error.PebbleException
import com.mitchellbosecke.pebble.node.expression.Expression
import com.mitchellbosecke.pebble.node.fornode.{LazyLength, LazyRevIndex}
import com.mitchellbosecke.pebble.node.{BodyNode, ForNode}
import com.mitchellbosecke.pebble.template.{EvaluationContextImpl, PebbleTemplateImpl, ScopeChain}

import scala.jdk.CollectionConverters._

case class ScalaForNode(
    lineNumber: Int,
    variableName: String,
    iterableExpression: Expression[_],
    body: BodyNode,
    elseBody: BodyNode
) extends ForNode(lineNumber, variableName, iterableExpression, body, elseBody) {

  override def render(self: PebbleTemplateImpl, writer: Writer, context: EvaluationContextImpl): Unit = {
    val iterableEvaluation: Any = this.iterableExpression.evaluate(self, context)

    var iterable: java.lang.Iterable[_] = null
    if (iterableEvaluation != null) {

      iterable = this.toIterable(iterableEvaluation)
      if (iterable == null)
        throw new PebbleException(
          null,
          "Not an iterable object. Value = [" + iterableEvaluation.toString + "]",
          this.getLineNumber,
          self.getName
        )
      val iterator: java.util.Iterator[_] = iterable.iterator
      if (iterator.hasNext) {
        val scopeChain: ScopeChain = context.getScopeChain
        scopeChain.pushScope()
        val length: LazyLength            = new LazyLength(iterableEvaluation)
        var index: Int                    = 0
        var loop: util.Map[String, Any]   = new util.HashMap[String, Any]()
        val usingExecutorService: Boolean = context.getExecutorService != null
        while (iterator.hasNext) {
          if (index == 0 || usingExecutorService) {
            loop = new util.HashMap[String, Any]
            loop.put("first", index == 0)
            loop.put("last", !iterator.hasNext)
            loop.put("length", length)
          }
          else if (index == 1) // second iteration
            loop.put("first", false)
          loop.put("revindex", new LazyRevIndex(index, length))
          index = index + 1
          loop.put("index", index - 1)
          scopeChain.put("loop", loop)
          scopeChain.put(this.variableName, iterator.next)
          // last iteration
          if (!iterator.hasNext)
            loop.put("last", true)
          this.body.render(self, writer, context)
        }
        scopeChain.popScope()
      }
      else if (this.elseBody != null)
        this.elseBody.render(self, writer, context)

    }
  }

  private def toIterable(obj: Any): java.lang.Iterable[_] =
    obj match {
      case it: java.lang.Iterable[_]        => it
      case it: collection.Iterable[_]       => it.asJava
      case map: java.util.Map[_, _]         => map.entrySet()
      case map: collection.Map[_, _]        => map.keySet.map(key => map(key)).asJava
      case array: Array                     => ArrayIterable(array)
      case enumeration: util.Enumeration[_] => EnumerationIterable(enumeration)
      case _                                => null
    }

  private case class EnumerationIterable(var obj: util.Enumeration[_]) extends java.lang.Iterable[Any] {

    override def iterator: util.Iterator[Any] =
      new util.Iterator[Any]() {
        override def hasNext: Boolean = obj.hasMoreElements

        override def next: Any = obj.nextElement

        override def remove(): Unit =
          throw new UnsupportedOperationException
      }
  }

  case class ArrayIterable private[node] (var obj: Any) extends java.lang.Iterable[Any] {
    override def iterator: util.Iterator[Any] =
      new util.Iterator[Any]() {
        private var index        = 0
        final private val length = Array.getLength(obj)

        override def hasNext: Boolean = index < length

        override def next: Any = {
          index = index + 1
          Array.get(obj, index - 1)
        }

        override def remove(): Unit =
          throw new UnsupportedOperationException
      }
  }

}
