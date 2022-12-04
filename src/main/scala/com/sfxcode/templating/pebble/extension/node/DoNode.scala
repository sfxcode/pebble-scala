package com.sfxcode.templating.pebble.extension.node

import io.pebbletemplates.pebble.extension.NodeVisitor
import io.pebbletemplates.pebble.node.AbstractRenderableNode
import io.pebbletemplates.pebble.node.expression.Expression
import io.pebbletemplates.pebble.template.{EvaluationContextImpl, PebbleTemplateImpl}

import java.io.Writer

case class DoNode(lineNumber: Int, expression: Expression[_]) extends AbstractRenderableNode {
  override def render(self: PebbleTemplateImpl, writer: Writer, context: EvaluationContextImpl): Unit =
    expression.evaluate(self, context)

  override def accept(visitor: NodeVisitor): Unit =
    visitor.visit(this)
}
