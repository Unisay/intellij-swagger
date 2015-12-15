package de.zalando.swagger.intellij.yaml.parser;

import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import de.zalando.swagger.intellij.yaml.SwaggerYamlLanguage$;
import de.zalando.swagger.intellij.yaml.lexer.NeonTokenTypes;

/**
 * Types of elements returned from parser
 */
public interface NeonElementTypes {
  IFileElementType FILE = new IFileElementType(SwaggerYamlLanguage$.MODULE$);

  NeonElementType KEY_VALUE_PAIR = new NeonElementType("Key value pair");
  NeonElementType KEY = new NeonElementType("Key");
  NeonElementType COMPOUND_KEY = new NeonElementType("Compound key");
  NeonElementType HASH = new NeonElementType("Hash");
  NeonElementType ITEM = new NeonElementType("Item");
  NeonElementType ENTITY = new NeonElementType("Entity");
  NeonElementType JINJA = new NeonElementType("Jinja2");

  NeonElementType ARRAY = new NeonElementType("Array");
  NeonElementType ARGS = new NeonElementType("Args");
  NeonElementType SEQUENCE = new NeonElementType("Sequence");
  NeonElementType COMPOUND_VALUE = new NeonElementType("Compound value");
  NeonElementType SCALAR_VALUE = new NeonElementType("Scalar value");
  NeonElementType REFERENCE = new NeonElementType("Reference");

  TokenSet SCALAR_VALUES = TokenSet.create(
          NeonTokenTypes.NEON_STRING,
          NeonTokenTypes.NEON_NUMBER,
          NeonTokenTypes.NEON_REFERENCE,
          NeonTokenTypes.NEON_IDENTIFIER,
          NeonTokenTypes.NEON_LITERAL,
          NeonTokenTypes.NEON_VARIABLE,
          NeonTokenTypes.NEON_DOUBLE_COLON, NeonTokenTypes.NEON_DOLLAR, NeonTokenTypes.NEON_AT
  );
}
