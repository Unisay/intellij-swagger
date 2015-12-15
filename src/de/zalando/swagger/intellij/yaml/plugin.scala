package de.zalando.swagger.intellij.yaml

import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.{FileTypeConsumer, FileTypeFactory, LanguageFileType}
import com.intellij.openapi.util.IconLoader

object SwaggerYamlLanguage extends Language("Swagger/YAML")

object SwaggerYamlIcons {
  val icon16 = IconLoader.getIcon("/icons/16x16.png")
  val icon32 = IconLoader.getIcon("/icons/32x32.png")
}

object SwaggerYamlFileType extends LanguageFileType(SwaggerYamlLanguage) {
  override val getName = "Swagger/YAML"
  override val getDescription = "Swagger descriptor in the YAML format"
  override val getDefaultExtension = "yml"
  override val getIcon = SwaggerYamlIcons.icon16
}

class SwaggerYamlTypeFactory extends FileTypeFactory {
  override def createFileTypes(consumer: FileTypeConsumer): Unit = consumer.consume(SwaggerYamlFileType, "yml;yaml")
}