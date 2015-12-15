package de.zalando.swagger.intellij.yaml.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import de.zalando.swagger.intellij.yaml.psi.NeonReference;
import org.jetbrains.annotations.NotNull;

public class NeonReferenceImpl extends NeonPsiElementImpl implements NeonReference {
  public NeonReferenceImpl(@NotNull ASTNode astNode) {
    super(astNode);
  }

  @Override
  public PsiElement setName(String name) throws IncorrectOperationException {
    return null;
  }
}
