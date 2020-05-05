package ru.tinkoff.config

import java.io.File
import java.nio.file.{Files, Path}

import cats.effect.IO

import scala.io.Source

case class SourceConfig(paths: Seq[String])

object SourceConfig {

  def readContent(file: File): IO[String] = {
    val source      = Source.fromFile(file)
    val fileContent = source.mkString.dropWhile(_ != '{')
    source.close()
    IO(fileContent)
  }

  def getFiles(file: File): List[File] =
    if (file.isDirectory) file.listFiles().toList.flatMap(getFiles)
    else if (file.isFile) List(file)
    else Nil
}
