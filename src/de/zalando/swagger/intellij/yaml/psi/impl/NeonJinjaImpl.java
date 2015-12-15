package de.zalando.swagger.intellij.yaml.psi.impl;

import com.intellij.lang.ASTNode;
import de.zalando.swagger.intellij.yaml.psi.NeonJinja;
import org.jetbrains.annotations.NotNull;

public class NeonJinjaImpl extends NeonPsiElementImpl implements NeonJinja {

  public NeonJinjaImpl(@NotNull ASTNode astNode) {
    super(astNode);
  }

  public String toString() {
    return "Yaml Jinja2";
  }

}
