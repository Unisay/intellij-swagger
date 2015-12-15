package de.zalando.swagger.intellij.yaml.editor;

import com.intellij.application.options.CodeStyleAbstractConfigurable;
import com.intellij.application.options.CodeStyleAbstractPanel;
import com.intellij.application.options.TabbedLanguageCodeStylePanel;
import com.intellij.openapi.options.Configurable;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;
import de.zalando.swagger.intellij.yaml.SwaggerYamlLanguage$;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class NeonCodeStyleSettingsProvider extends CodeStyleSettingsProvider {

    @Override
    public AnsibleCodeStyleSettings createCustomSettings(CodeStyleSettings settings) {
        return new AnsibleCodeStyleSettings(settings);
    }

    @NotNull
    @Override
    public Configurable createSettingsPage(CodeStyleSettings settings, CodeStyleSettings originalSetting) {
        return new CodeStyleAbstractConfigurable(settings, originalSetting, getConfigurableDisplayName()) {
            @Nullable
            @Override
            public String getHelpTopic() {
                return null;
            }

            @Override
            protected CodeStyleAbstractPanel createPanel(CodeStyleSettings settings) {
                return new SimpleCodeStyleMainPanel(getCurrentSettings(), settings);
            }
        };
    }

    private static class SimpleCodeStyleMainPanel extends TabbedLanguageCodeStylePanel {
        public SimpleCodeStyleMainPanel(CodeStyleSettings currentSettings, CodeStyleSettings settings) {
            super(SwaggerYamlLanguage$.MODULE$, currentSettings, settings);
        }
    }

    private class AnsibleCodeStyleSettings extends CustomCodeStyleSettings {
        public AnsibleCodeStyleSettings(CodeStyleSettings settings) {
            super("AnsibleCodeStyleSettings", settings);
        }
    }

    @Override
    public String getConfigurableDisplayName() {
        return SwaggerYamlLanguage$.MODULE$.getDisplayName();
    }
}

