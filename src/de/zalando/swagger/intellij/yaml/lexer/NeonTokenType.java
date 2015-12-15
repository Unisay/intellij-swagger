package de.zalando.swagger.intellij.yaml.lexer;

import com.intellij.psi.tree.IElementType;
import de.zalando.swagger.intellij.yaml.SwaggerYamlLanguage$;
import org.jetbrains.annotations.NotNull;


public class NeonTokenType extends IElementType {
  public NeonTokenType(@NotNull String debugName) {
    super(debugName, SwaggerYamlLanguage$.MODULE$);
  }

  public String toString() {
    return "[Yaml] " + super.toString();
  }
}
