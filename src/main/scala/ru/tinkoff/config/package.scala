package ru.tinkoff

import pureconfig.ConfigReader
import pureconfig.generic.semiauto.deriveReader
import pureconfig.{ConfigReader, ConfigSource}

package object config {
  implicit val srDec: ConfigReader[ServerConfig]      = deriveReader
  implicit val dbDec: ConfigReader[DatabaseConfig]    = deriveReader
  implicit val srcDec: ConfigReader[SourceConfig]     = deriveReader
  implicit val wkDec: ConfigReader[WikiServiceConfig] = deriveReader
}
