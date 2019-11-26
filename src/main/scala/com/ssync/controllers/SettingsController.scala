package com.ssync.controllers

import java.io.FileNotFoundException

import com.ssync.controllers.FileToolUtils._
import com.ssync.models.Settings
import com.typesafe.scalalogging.LazyLogging

import scala.util.{Failure, Success, Try}

trait SettingsController extends JsonController with FileToolsController with LazyLogging {

  def loadSettings: Settings = {
    val fileSettingString = openAndReadSettings(settingsPath)
    convertJsonToSettings(fileSettingString)
  }

  private def openAndReadSettings(filename: String): String = {
    val aFile = openAndReadFile(filename)
    aFile match {
      case Failure(exception) =>
        exception match {
          case exception: FileNotFoundException =>
            logger.error("Settings file has just been created, please configure the source and destination paths")
            createSettingJson(filename)
            logger.info(s"Settings file path $filename")
            throw new FileNotFoundException("Settings file has just been created, please configure the source and destination paths")
          case _ => throw exception
        }
      case Success(fileString) =>
        logger.info(s"Opened setting file $fileString")
        fileString
    }
  }

  private def createSettingJson(filename: String): Try[String] = {
    val json = convertSettingsToJson(defaultSettings)
    Try {
      createFile(filename, json.toString)
    } match {
      case Failure(err) =>
        logger.error(s"Error creating file: $err")
        Failure(err)
      case _ =>
        logger.info(s"Created setting file $filename")
        Success(s"Created setting file $filename")
    }
  }
}
