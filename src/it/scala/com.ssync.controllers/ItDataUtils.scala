package com.ssync.controllers


import java.util.UUID
import better.files.File
import com.ssync.controllers.FileToolUtils._
import com.ssync.models.{SettingSyncItem, Settings}
import spray.json.{JsArray, JsObject, JsString}

object ItDataUtils {

  val sourcePath = getClass.getResource(getSeparator + "source").getPath
  val destinationPath = getClass.getResource(getSeparator + "destination").getPath

  val defaultSettings: Settings = new Settings(
    sourcePath,
    destinationPath,
    Array("jpg"),
    Array(""),
    Seq(SettingSyncItem(
      "sub folder 1",
      "sub folder 1",
      Array(""))
    )
  )

  val exampleSettings: Settings = Settings(
    sourcePath,
    destinationPath,
    Array("*"),
    Array(),
    Seq(SettingSyncItem(
      "sub folder 1",
      "sub1",
      Array())
    )
  )

  val testJSON = JsObject(
    "Source" -> JsString(sourcePath),
    "Destination" -> JsString(destinationPath),
    "Extensions" -> JsArray(JsString("jpg")),
    "IgnoredExtensions" -> JsArray(JsString("")),
    "SyncItems" -> JsArray(
      JsObject(
        "Name" -> JsString("sub folder 1"),
        "Path" -> JsString("sub folder 1"),
        "ProtectedDirectories" -> JsArray(JsString(""))
      )
    )
  )

  val exampleJSON = JsObject(
    "Source" -> JsString(sourcePath),
    "Destination" -> JsString(destinationPath),
    "Extensions" -> JsArray(JsString("*")),
    "IgnoredExtensions" -> JsArray(),
    "SyncItems" -> JsArray(
      JsObject(
        "Name" -> JsString("sub folder 1"),
        "Path" -> JsString("sub1"),
        "ProtectedDirectories" -> JsArray()
      )
    )
  )

  def randomizeSettingsPath = {
    val randomString = UUID.randomUUID.toString.substring(0, 4)
    settingsPath.replace("settings", s"settings_$randomString")
  }

  private val sourceCopyPath = sourcePath + "copy"
  private val destinationCopyPath = destinationPath + "copy"
  private val sourceCopy = File(sourceCopyPath)
  private val destinationCopy = File(destinationCopyPath)
  private val source = File(sourcePath)
  private val destination = File(destinationPath)

  def setupTestResources: Unit = {
    destination.copyTo(destinationCopy, true)
    source.copyTo(sourceCopy, true)
    deleteSettings
  }

  def teardownTestResources: Unit = {
    destination.delete(false)
    destinationCopy.copyTo(destination, true)
    destinationCopy.delete(false)
    source.delete(false)
    sourceCopy.copyTo(source, true)
    sourceCopy.delete(false)
    deleteSettings
  }

  val settingsPath = sourcePath
    .replace("test-classes/", "")
    .replace("source", "settings.json")

  def deleteSettings() = {

    val file = File(settingsPath)
    file.delete(true)
  }
}
