package com.ssync.controllers

import java.io.FileNotFoundException

import com.ssync.controllers.FileToolUtils._
import com.ssync.models.Settings

import scala.util.{Failure, Success, Try}

trait SettingsController extends JsonController with FileToolsController {

  def loadSettings: Settings = {
    val fileSettingString = openAndReadSettings(settingsPath)
    ConvertJsonToSettings(fileSettingString)
  }

  private def openAndReadSettings(filename: String): String = {
    val aFile = openAndReadFile(filename)
    aFile match {
      case Failure(exception) =>
        exception match {
          case exception: FileNotFoundException =>
            createSettingJson(filename)
            throw new FileNotFoundException("Settings file has just been created, please configure the source and destination paths")
          case _ => throw exception
        }
      case Success(fileString) => fileString
    }
  }

  private def createSettingJson(filename: String): Try[String] = {
    val json = ConvertSettingsToJson(defaultSettings)
    Try {
      createFile(filename, json.toString)
    } match {
      case Failure(err) => Failure(err)
      case _ => Success(s"Created setting file $filename")
    }
  }
}
