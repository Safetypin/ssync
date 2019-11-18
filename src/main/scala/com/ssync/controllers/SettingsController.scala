package com.ssync.controllers

import java.io.FileNotFoundException

import com.ssync.controllers.FileToolUtils._
import com.ssync.models.Settings

import scala.util.{Failure, Success, Try}

trait SettingsController extends JsonController {

  val fileToolsController: FileToolsController
  private def createSettingJson(filename: String): Try[String] = {
    val json = ConvertSettingsToJson(defaultSettings)
    Try {
      fileToolsController createFile(filename, json.toString)
    } match {
      case Failure(err) => Failure(err)
      case _ => Success(s"Created setting file $filename")
    }
  }

  private def openAndReadSettings(filename: String): String = {
    val aFile = fileToolsController openAndReadFile(filename)
    println("open and read file")
   aFile match {
      case Failure(exception) =>
        exception match {
          case e: FileNotFoundException =>
            println("can't find file and create json file")
            createSettingJson(filename)
            println("Settings file has just been created, please configure the source and destination paths")
            throw new FileNotFoundException("Settings file has just been created, please configure the source and destination paths")
          case _ => throw exception
        }
      case Success(fileString) => fileString
    }
  }

  def loadSettings: Settings = {
    val fileSettingString = openAndReadSettings(settingsPath)
    ConvertJsonToSettings (fileSettingString)
  }

}
