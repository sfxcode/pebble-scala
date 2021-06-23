package com.sfxcode.templating.pebble.extension.node

import com.mitchellbosecke.pebble.extension.NodeVisitor
import com.mitchellbosecke.pebble.node.AbstractRenderableNode
import com.mitchellbosecke.pebble.node.expression.Expression
import com.mitchellbosecke.pebble.template.{EvaluationContextImpl, PebbleTemplateImpl}

import java.io.Writer

case class DoNode(lineNumber: Int, expression: Expression[_]) extends AbstractRenderableNode {
  override def render(self: PebbleTemplateImpl, writer: Writer, context: EvaluationContextImpl): Unit =
    expression.evaluate(self, context)

  override def accept(visitor: NodeVisitor): Unit =
    visitor.visit(this)
}
