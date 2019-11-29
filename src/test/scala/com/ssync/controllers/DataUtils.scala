package com.ssync.controllers


//import java.io.File

import better.files.File
import com.ssync.controllers.FileToolUtils._
import com.ssync.models.{SettingSyncItem, Settings}
import spray.json.{JsArray, JsObject, JsString}

object DataUtils {

  val sourcePath = getClass.getResource(getSeparator + "source").getPath
  val destinationPath = getClass.getResource(getSeparator + "destination").getPath

  val defaultSettings: Settings = Settings(
    sourcePath,
    destinationPath,
    Array("jpg"),
    Array(""),
    Seq(
      SettingSyncItem(
        "sub directory 1",
        "sub directory 1",
        Array("")
      )
    )
  )
  val testJSON = JsObject(
    "Source" -> JsString(sourcePath),
    "Destination" -> JsString(destinationPath),
    "Extensions" -> JsArray(JsString("jpg")),
    "IgnoredExtensions" -> JsArray(JsString("")),
    "SyncItems" -> JsArray(
      JsObject(
        "Name" -> JsString("sub directory 1"),
        "Path" -> JsString("sub directory 1"),
        "ProtectedDirectories" -> JsArray(JsString(""))
      )
    )
  )
  private val sourceCopyPath = sourcePath + "copy"
  private val destinationCopyPath = destinationPath + "copy"
  private val sourceCopy = File(sourceCopyPath)
  private val destinationCopy = File(destinationCopyPath)
  private val source = File(sourcePath)
  private val destination = File(destinationPath)

  def randomizedDestinationFilePath = {
    val filename = s"test_file_$randomString"
    s"$destinationPath$getSeparator$filename"
  }

  def randomizedDestinationDirectoryFilePath = {
    val filename = s"test_file_$randomString"
    s"$destinationPath$randomString$getSeparator$filename"
  }

  def deleteFile(path: String) = {
    val file = File(path)
    file.delete()
  }

  def setupTestResources: Unit = {
    destination.copyTo(destinationCopy, true)
    source.copyTo(sourceCopy, true)
  }

  def teardownTestResources: Unit = {
    destination.delete(false)
    destinationCopy.copyTo(destination, true)
    destinationCopy.delete(false)
    source.delete(false)
    sourceCopy.copyTo(source, true)
    sourceCopy.delete(false)
  }
}
