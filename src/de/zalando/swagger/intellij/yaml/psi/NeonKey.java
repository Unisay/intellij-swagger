package de.zalando.swagger.intellij.yaml.psi;

import com.intellij.navigation.ItemPresentation;

/**
 * Key from key-value pair
 */
public interface NeonKey extends NeonPsiElement {
  String getKeyText();

  ItemPresentation getPresentation();
}
