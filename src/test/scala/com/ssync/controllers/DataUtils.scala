package com.ssync.controllers


import java.util.UUID

import com.ssync.controllers.FileToolUtils._
import com.ssync.models.{Settings, SettingSyncItem}
import spray.json.{JsArray, JsObject, JsString}

object DataUtils {

  val source = getClass.getResource(getSeparator + "source").getPath
  val destination = getClass.getResource(getSeparator + "destination").getPath

  val defaultSettings: Settings = Settings(source, destination, Array("jpg"),
    Seq(SettingSyncItem("sub folder 1", "sub folder 1")))
  val testJSON = JsObject("Source" -> JsString(source),
    "Destination" -> JsString(destination), "Extensions" -> JsArray(JsString("jpg")),
    "SyncItems" -> JsArray(JsObject("Name" -> JsString("sub folder 1"), "Path" -> JsString("sub folder 1"))))

  def randomizeSettingsPath = {
    val randomString = UUID.randomUUID.toString.substring(0, 4)
    settingsPath.replace("settings", s"settings_$randomString")
  }
}
