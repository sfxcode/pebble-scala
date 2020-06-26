package com.sfxcode.templating.pebble.extension.tokenParser

import com.mitchellbosecke.pebble.error.ParserException
import com.mitchellbosecke.pebble.lexer.Token
import com.mitchellbosecke.pebble.node.{BodyNode, RenderableNode}
import com.mitchellbosecke.pebble.parser.Parser
import com.mitchellbosecke.pebble.tokenParser.TokenParser
import com.sfxcode.templating.pebble.extension.node.ScalaForNode

case class ForTokenParser() extends TokenParser {

  override def parse(token: Token, parser: Parser): RenderableNode = {
    val stream     = parser.getStream
    val lineNumber = token.getLineNumber

    // skip the 'for' token
    stream.next

    // get the iteration variable
    val iterationVariable = parser.getExpressionParser.parseNewVariableName
    stream.expect(Token.Type.NAME, "in")

    // get the iterable variable
    val iterable = parser.getExpressionParser.parseExpression
    stream.expect(Token.Type.EXECUTE_END)

    val body               = parser.subparse((tkn: Token) => tkn.test(Token.Type.NAME, "else", "endfor"))
    var elseBody: BodyNode = null

    if (stream.current.test(Token.Type.NAME, "else")) {
      // skip the 'else' token
      stream.next
      stream.expect(Token.Type.EXECUTE_END)
      elseBody = parser.subparse((tkn: Token) => tkn.test(Token.Type.NAME, "endfor"))
    }

    if (stream.current.getValue == null) {
      throw new ParserException(
        null,
        "Unexpected end of template. Pebble was looking for the \"endfor\" tag",
        stream.current.getLineNumber,
        stream.getFilename
      )
    }

    // skip the 'endfor' token
    stream.next
    stream.expect(Token.Type.EXECUTE_END)

    ScalaForNode(lineNumber, iterationVariable, iterable, body, elseBody)
  }

  override def getTag: String = "for"
}
