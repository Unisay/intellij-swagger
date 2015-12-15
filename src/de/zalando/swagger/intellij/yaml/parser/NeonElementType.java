package de.zalando.swagger.intellij.yaml.parser;

import com.intellij.psi.tree.IElementType;
import de.zalando.swagger.intellij.yaml.SwaggerYamlLanguage$;
import org.jetbrains.annotations.NotNull;


public class NeonElementType extends IElementType {
  public NeonElementType(@NotNull String debugName) {
    super(debugName, SwaggerYamlLanguage$.MODULE$);
  }

  public String toString() {
    return "[Yaml] " + super.toString();
  }
}
