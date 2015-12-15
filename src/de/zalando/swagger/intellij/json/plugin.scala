package de.zalando.swagger.intellij

import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.{FileTypeConsumer, FileTypeFactory, LanguageFileType}
import com.intellij.openapi.util.IconLoader

object SwaggerJsonLanguage extends Language("Swagger/JSON")

object SwaggerJsonFileType extends LanguageFileType(SwaggerJsonLanguage) {
  override val getName = "Swagger JSON file"
  override val getDescription = "Swagger descriptor in the JSON format"
  override val getDefaultExtension = "json"
  override val getIcon = IconLoader.getIcon("/icons/16x16.png")
}

class SwaggerJsonTypeFactory extends FileTypeFactory {
  override def createFileTypes(consumer: FileTypeConsumer): Unit = {
    consumer.consume(SwaggerJsonFileType, "json")
    consumer.consume(SwaggerJsonFileType, "swagger")
  }
}