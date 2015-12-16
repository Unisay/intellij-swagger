package de.zalando.swagger.intellij.yaml.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import de.zalando.swagger.intellij.yaml.lexer.NeonTokenTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Stack;

/**
 * Neon parser, convert tokens (output from lexer) into syntax tree
 */
public class NeonParser implements PsiParser, NeonTokenTypes, NeonElementTypes {
  private enum IndentType {TABS, SPACES}

  private PsiBuilder myBuilder;
  private boolean eolSeen = false;
  private int myIndent;
  private boolean myHasTabs = false; // FIXME: use this
  private PsiBuilder.Marker myAfterLastEolMarker;
  private int myInline;
  private Stack<IElementType> expectedClosings = new Stack<IElementType>();
  private IndentType myIndentType;


  @NotNull
  @Override
  public ASTNode parse(IElementType root, PsiBuilder builder) {
    builder.setDebugMode(true);

    myBuilder = builder;
    myIndent = 0;
    eolSeen = false;
    myIndentType = null;

    // begin
    PsiBuilder.Marker fileMarker = mark();

    passEmpty(); // process beginning of file
    if (!myBuilder.eof())
      parseValue(0);

    while (!myBuilder.eof()) {
      if (myBuilder.getTokenType() != NEON_INDENT) {
        myBuilder.error("unexpected token at end of file");
      }
      myBuilder.advanceLexer();
    }

    // end
    fileMarker.done(root);
    return builder.getTreeBuilt();
  }

  private void parseValue(int indent) {
    IElementType currentToken = myBuilder.getTokenType();
    IElementType nextToken = myBuilder.lookAhead(1);

    if (STRING_LITERALS.contains(currentToken) && nextToken == NEON_COLON || currentToken == NEON_ARRAY_BULLET) {
      // key: val || - key
      PsiBuilder.Marker val = mark();
      parseArray(indent);
      val.done(ARRAY);
    } else if (STRING_LITERALS.contains(currentToken) && nextToken == NEON_ASSIGNMENT) {
      PsiBuilder.Marker val = mark();
      myInline++;
      parseOpenArray(indent);
      myInline--;
      val.done(ARRAY);
    } else if (STRING_LITERALS.contains(currentToken) || currentToken == NEON_LBRACE_JINJA) {
      PsiBuilder.Marker val = mark();
      parseScalar(indent);
      val.done(SCALAR_VALUE);
    } else if (OPEN_BRACKET.contains(currentToken)) { // array
      PsiBuilder.Marker val = mark();
      myInline++;

      IElementType closing = closingBrackets.get(currentToken);
      expectedClosings.push(closing);

      advanceLexer(); // opening bracket
      parseArray(1000000);

      expectedClosings.pop();
      advanceLexer(closing); // closing bracket

      myInline--;

      val.done(ARRAY);

    } else if (currentToken == NEON_INDENT) {
      // no value -> null
    } else if (currentToken == SCALAR_FOLDED) {
      advanceLexer();

      // And skip next indent, if any
      if (nextToken == NEON_INDENT)
        advanceLexer();

      parseValue(indent);
    } else {
      // dunno
      myBuilder.error("unexpected token " + currentToken);
      advanceLexer();
    }
  }

  private void parseScalar(int indent) {
    IElementType currentToken = myBuilder.getTokenType();
    IElementType nextToken = myBuilder.lookAhead(1);

    if (STRING_LITERALS.contains(currentToken)) {
      // Continue scalar
      advanceLexerOnAllowedTokens(OPEN_STRING_ALLOWED);
      parseScalar(indent);

    } else if (currentToken == NEON_LBRACE_JINJA) { // Jinja code
      myInline++;
      PsiBuilder.Marker valJinja = mark();

      advanceLexer(NEON_LBRACE_JINJA); // opening bracket

      PsiBuilder.Marker valCode = mark();
      advanceLexerTill(NEON_RBRACE_JINJA); // closing bracket
      valCode.done(REFERENCE);

      advanceLexer(NEON_RBRACE_JINJA); // closing bracket

      valJinja.done(JINJA);
      myInline--;

      parseScalar(indent);

    } else if (currentToken == SCALAR_FOLDED) {
      advanceLexer();

      // And skip next indent, if any
      if (nextToken == NEON_INDENT)
        advanceLexer();

      parseScalar(indent);
    } else if (currentToken == SCALAR_LITERAL) {
      advanceLexer();

      // And skip next indent, if any
      if (nextToken == NEON_INDENT)
        advanceLexer();

      parseScalar(indent);
    } else if (CLOSING_BRACKET.contains(currentToken) && (expectedClosings.empty() || currentToken != expectedClosings.peek())) {
      // Eat closing brackets when they are not expected
      advanceLexer();
      parseScalar(indent);
    } else if (expectedClosings.empty() && currentToken == NEON_ITEM_DELIMITER) {
      // Eat commas when we are not in array (ugly detected by absence of expectedClosings)
      advanceLexer();
      parseScalar(indent);
    }
  }

  private void parseArray(int indent) {
    boolean isInline = myInline > 0;

    while (myBuilder.getTokenType() != null && !CLOSING_BRACKET.contains(myBuilder.getTokenType()) && (isInline ? myInline > 0 : myIndent >= indent)) {
      IElementType currentToken = myBuilder.getTokenType();
      IElementType nextToken = myBuilder.lookAhead(1);

      if (ASSIGNMENTS.contains(nextToken)) { // key-val pair
        parseKeyVal(indent);

      } else if (currentToken == NEON_ARRAY_BULLET) {
        PsiBuilder.Marker markItem = mark();
        advanceLexer();
        parseValue(indent + 1);
        markItem.done(ITEM);

      } else if (isInline) {
        parseValue(indent);

      } else {
        myBuilder.error("expected key-val pair or array item");
        advanceLexer();

      }

      if (myBuilder.getTokenType() == NEON_INDENT || (isInline && myBuilder.getTokenType() == NEON_ITEM_DELIMITER))
        advanceLexer();
    }
  }

  // Like item: foo=something baz=something_else
  private void parseOpenArray(int indent) {
    while (myBuilder.getTokenType() != null && myBuilder.getTokenType() != NEON_INDENT) {
      IElementType currentToken = myBuilder.getTokenType();
      IElementType nextToken = myBuilder.lookAhead(1);

      if (ASSIGNMENTS.contains(nextToken)) { // key-val pair
        parseKeyVal(indent);
      } else {
        myBuilder.error("expected key-val pair or array item");
        advanceLexer();
      }
    }
  }

  private void parseKeyVal(int indent) {
    myAssert(STRING_LITERALS.contains(myBuilder.getTokenType()), "Expected literal or string");

    PsiBuilder.Marker keyValPair = mark();
    parseKey();
    eolSeen = false;

    // key colon value
    myAssert(ASSIGNMENTS.contains(myBuilder.getTokenType()), "Expected assignment operator");
    advanceLexer();

    // value
    if (myBuilder.getTokenType() == NEON_INDENT) {
      advanceLexer(); // read indent
      if (myIndent > indent) {
        PsiBuilder.Marker val = mark();
        parseArray(myIndent);
        val.done(ARRAY);
      } else {
        // myBuilder.error("value missing"); // actually not an error, but null
      }
    } else {
      parseValue(indent);
    }

    keyValPair.done(KEY_VALUE_PAIR);
  }

  private void parseKey() {
    myAssert(STRING_LITERALS.contains(myBuilder.getTokenType()), "Expected literal or string");

    PsiBuilder.Marker key = mark();
    advanceLexer();
    key.done(KEY);
  }


  /***  helpers ***/

  /**
   * Go to next token; if there is more whitespace, skip to the last
   */
  private void advanceLexer() {
    if (myBuilder.eof()) return;

    do {
      IElementType type = myBuilder.getTokenType();
      this.eolSeen = this.eolSeen || type == NEON_INDENT;
      if (type == NEON_INDENT) {
        validateTabsSpaces();
        myIndent = myBuilder.getTokenText().length() - 1;
      }

      myBuilder.advanceLexer();
    }
    while (myBuilder.getTokenType() == NEON_INDENT && myBuilder.lookAhead(1) == NEON_INDENT); // keep going if we're still indented
  }

  private void advanceLexer(IElementType expectedToken) {
    if (myBuilder.getTokenType() == expectedToken) {
      advanceLexer();

    } else {
      myBuilder.error("unexpected token " + myBuilder.getTokenType() + ", expected " + expectedToken);

    }
  }

  private void advanceLexerTill(IElementType expectedToken) {
    while (myBuilder.getTokenType() != expectedToken && !myBuilder.eof()) {
      advanceLexer();
    }
  }

  private void advanceLexerOnAllowedTokens(TokenSet allowedTokens) {
    while (allowedTokens.contains(myBuilder.getTokenType()) && !myBuilder.eof()) {
      advanceLexer();
    }
  }

  /**
   * Check that only tabs or only spaces are used for indent
   */
  private void validateTabsSpaces() {
    assert myBuilder.getTokenType() == NEON_INDENT;
    String text = myBuilder.getTokenText().replace("\n", "");
    if (text.length() == 0) return; // no real indent

    // first indet -> detect
    if (myIndentType == null) {
      myIndentType = text.charAt(0) == '\t' ? IndentType.TABS : IndentType.SPACES;

    } else {
      if (text.contains(myIndentType == IndentType.TABS ? " " : "\t")) {
        myBuilder.error("tab/space mixing");
      }
    }
  }

  private void myAssert(boolean condition, String message) {
    if (!condition) {
      myBuilder.error(message + ", got " + myBuilder.getTokenType());
      advanceLexer();
    }
  }

  private void dropEolMarker() {
    if (this.myAfterLastEolMarker != null) {
      this.myAfterLastEolMarker.drop();
      this.myAfterLastEolMarker = null;
    }
  }

  private void rollBackToEol() {
    if ((this.eolSeen) && (this.myAfterLastEolMarker != null)) {
      this.eolSeen = false;
      this.myAfterLastEolMarker.rollbackTo();
      this.myAfterLastEolMarker = null;
    }
  }

  private PsiBuilder.Marker mark() {
    dropEolMarker();
    return myBuilder.mark();
  }

  private void passEmpty() {
    while (!myBuilder.eof() && (myBuilder.getTokenType() == NEON_INDENT || myBuilder.getTokenType() == NEON_UNKNOWN
            || myBuilder.getTokenType() == NEON_TAG || myBuilder.getTokenType() == NEON_HEADER)) {
      advanceLexer();
    }
  }
}
