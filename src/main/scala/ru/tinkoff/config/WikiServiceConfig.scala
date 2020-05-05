package ru.tinkoff.config

final case class ServerConfig(host: String, port: Int)
final case class WikiServiceConfig(db: DatabaseConfig, server: ServerConfig, source: SourceConfig)
