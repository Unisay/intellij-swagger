package de.zalando.swagger.intellij.yaml.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import de.zalando.swagger.intellij.yaml.lexer.NeonLexer;
import de.zalando.swagger.intellij.yaml.lexer.NeonTokenTypes;
import de.zalando.swagger.intellij.yaml.psi.impl.*;
import org.jetbrains.annotations.NotNull;

public class NeonParserDefinition implements ParserDefinition {
  @NotNull
  @Override
  public Lexer createLexer(Project project) {
    return new NeonLexer();
  }

  @Override
  public PsiParser createParser(Project project) {
    return new NeonParser();
  }

  @Override
  public IFileElementType getFileNodeType() {
    return NeonElementTypes.FILE;
  }

  @NotNull
  @Override
  public TokenSet getWhitespaceTokens() {
    return NeonTokenTypes.WHITESPACES;
  }

  @NotNull
  @Override
  public TokenSet getCommentTokens() {
    return NeonTokenTypes.COMMENTS;
  }

  @NotNull
  @Override
  public TokenSet getStringLiteralElements() {
    return NeonTokenTypes.STRING_LITERALS;
  }

  @NotNull
  @Override
  public PsiElement createElement(ASTNode node) {
    IElementType type = node.getElementType();

    if (type == NeonElementTypes.KEY_VALUE_PAIR) return new NeonKeyValPairImpl(node);
    else if (type == NeonElementTypes.KEY) return new NeonKeyImpl(node);
    else if (type == NeonElementTypes.COMPOUND_VALUE) return new NeonArrayImpl(node);
    else if (type == NeonElementTypes.ARRAY) return new NeonArrayImpl(node);
    else if (type == NeonElementTypes.SEQUENCE) return new NeonSectionImpl(node);
    else if (type == NeonElementTypes.SCALAR_VALUE) return new NeonScalarImpl(node);
    else if (type == NeonElementTypes.ENTITY) return new NeonEntityImpl(node);
    else if (type == NeonElementTypes.JINJA) return new NeonJinjaImpl(node);
    else if (type == NeonElementTypes.REFERENCE) return new NeonReferenceImpl(node);
    else if (type == NeonElementTypes.ARGS) return new NeonArrayImpl(node); // FIXME: will it work?
    else return new NeonPsiElementImpl(node);
  }

  @Override
  public PsiFile createFile(FileViewProvider viewProvider) {
    return new NeonFileImpl(viewProvider);
  }

  @Override
  public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode astNode, ASTNode astNode1) {
    return SpaceRequirements.MAY;
  }
}
