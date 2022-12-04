package com.sfxcode.templating.pebble.extension.tokenParser

import io.pebbletemplates.pebble.lexer.Token
import io.pebbletemplates.pebble.node.RenderableNode
import io.pebbletemplates.pebble.parser.Parser
import io.pebbletemplates.pebble.tokenParser.TokenParser
import com.sfxcode.templating.pebble.extension.node.DoNode

case class DoTokenParser() extends TokenParser {

  override def parse(token: Token, parser: Parser): RenderableNode = {
    val stream     = parser.getStream
    val lineNumber = token.getLineNumber

    // skip the 'do' token
    stream.next

    val expression = parser.getExpressionParser.parseExpression

    stream.expect(Token.Type.EXECUTE_END)

    DoNode(lineNumber, expression)
  }

  override def getTag: String = DoTokenParser.TagName
}

object DoTokenParser {
  val TagName = "do"
}
