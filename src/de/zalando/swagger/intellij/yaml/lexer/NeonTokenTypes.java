package de.zalando.swagger.intellij.yaml.lexer;

import com.google.common.collect.ImmutableMap;
import com.intellij.embedding.EmbeddedLazyParseableElementType;
import com.intellij.openapi.fileTypes.PlainTextLanguage;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.ILazyParseableElementType;
import com.intellij.psi.tree.TokenSet;

import java.util.Map;

/**
 * Types of tokens returned form lexer
 *
 * @author Jan Dolecek - juzna.cz@gmail.com
 */
public interface NeonTokenTypes {
  IElementType NEON_STRING = new NeonTokenType("string");
  IElementType NEON_SYMBOL = new NeonTokenType("symbol"); // use a symbol or brace instead (see below)
  IElementType NEON_COMMENT = new NeonTokenType("comment");
  IElementType NEON_INDENT = new NeonTokenType("indent");
  IElementType NEON_LITERAL = new NeonTokenType("literal");
  IElementType NEON_KEYWORD = new NeonTokenType("keyword");
  IElementType NEON_WHITESPACE = TokenType.WHITE_SPACE; // new NeonTokenType("whitespace");
  IElementType NEON_UNKNOWN = TokenType.BAD_CHARACTER; // new NeonTokenType("error");

  IElementType NEON_TAG = new NeonTokenType("tag");
  IElementType NEON_HEADER = new NeonTokenType("header");

  // symbols
  IElementType NEON_COLON = new NeonTokenType(":");
  IElementType NEON_PIPE = new NeonTokenType("|");
  IElementType NEON_ASSIGNMENT = new NeonTokenType("=");
  IElementType NEON_ARRAY_BULLET = new NeonTokenType("-");
  IElementType NEON_ITEM_DELIMITER = new NeonTokenType(",");
  IElementType SCALAR_FOLDED = new NeonTokenType(">");
  IElementType SCALAR_LITERAL = new NeonTokenType("|");

  // braces
  IElementType NEON_LPAREN = new NeonTokenType("(");
  IElementType NEON_RPAREN = new NeonTokenType(")");
  IElementType NEON_LBRACE_CURLY = new NeonTokenType("{");
  IElementType NEON_RBRACE_CURLY = new NeonTokenType("}");
  IElementType NEON_LBRACE_JINJA = new NeonTokenType("{{");
  IElementType NEON_RBRACE_JINJA = new NeonTokenType("}}");
  IElementType NEON_LBRACE_SQUARE = new NeonTokenType("[");
  IElementType NEON_RBRACE_SQUARE = new NeonTokenType("]");

  // the rest are deprecated and will be removed
  IElementType NEON_IDENTIFIER = new NeonTokenType("identifier");
  IElementType NEON_EOL = new NeonTokenType("eol");
  IElementType NEON_VARIABLE = new NeonTokenType("variable");
  IElementType NEON_NUMBER = new NeonTokenType("number");
  IElementType NEON_REFERENCE = new NeonTokenType("reference");
  IElementType NEON_BLOCK_INHERITENCE = new NeonTokenType("<");
  IElementType NEON_QUOTE = new NeonTokenType("\"");
  IElementType NEON_DOUBLE_COLON = new NeonTokenType("::");
  IElementType NEON_DOLLAR = new NeonTokenType("$");
  IElementType NEON_AT = new NeonTokenType("@");


  // special tokens (identifier in block header or as array key)
  IElementType NEON_KEY = new NeonTokenType("key");

//  ILazyParseableElementType SCALAR_BLOCK =
//          new EmbeddedLazyParseableElementType("scalar_block", PlainTextLanguage.INSTANCE);

  // sets
  TokenSet WHITESPACES = TokenSet.create(NEON_WHITESPACE);
  TokenSet COMMENTS = TokenSet.create(NEON_COMMENT);
  TokenSet STRING_LITERALS = TokenSet.create(NEON_LITERAL, NEON_STRING);
  TokenSet ASSIGNMENTS = TokenSet.create(NEON_ASSIGNMENT, NEON_COLON);
  TokenSet OPEN_BRACKET = TokenSet.create(NEON_LPAREN, NEON_LBRACE_CURLY, NEON_LBRACE_SQUARE);
  TokenSet CLOSING_BRACKET = TokenSet.create(NEON_RPAREN, NEON_RBRACE_CURLY, NEON_RBRACE_SQUARE);
  TokenSet SYMBOLS = TokenSet.create(
          NEON_COLON, NEON_ASSIGNMENT, NEON_ARRAY_BULLET, NEON_ITEM_DELIMITER,
          NEON_LPAREN, NEON_RPAREN,
          NEON_LBRACE_CURLY, NEON_RBRACE_CURLY,
          NEON_LBRACE_SQUARE, NEON_RBRACE_SQUARE
  );

  TokenSet OPEN_STRING_ALLOWED = TokenSet.create(
          NEON_COLON, NEON_ASSIGNMENT, NEON_ARRAY_BULLET,
          NEON_LPAREN, NEON_RPAREN,

          NEON_WHITESPACE, NEON_LITERAL, NEON_STRING, NEON_QUOTE,

          // Match brackets, as they would be inside the literal
          NEON_LPAREN, NEON_LBRACE_CURLY, NEON_LBRACE_SQUARE
  );

  // brackets
  Map<IElementType, IElementType> closingBrackets = ImmutableMap.of(
          NEON_LPAREN, NEON_RPAREN,
          NEON_LBRACE_CURLY, NEON_RBRACE_CURLY,
          NEON_LBRACE_SQUARE, NEON_RBRACE_SQUARE
  );

}
