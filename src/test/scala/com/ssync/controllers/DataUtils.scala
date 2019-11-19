package com.ssync.controllers


import java.util.UUID

import com.ssync.controllers.FileToolUtils._
import com.ssync.models.{Settings, SyncItem}
import spray.json.{JsArray, JsObject, JsString}

object DataUtils {

  val source = getClass.getResource(getSeparator + "source").getPath
  val destination = getClass.getResource(getSeparator + "destination").getPath

  val defaultSettings: Settings = new Settings(source, destination, Array("jpg"),
    Seq(SyncItem("sub folder 1", "sub folder 1")))
  val testJSON = JsObject("source" -> JsString(source),
    "destination" -> JsString(destination), "extensions" -> JsArray(JsString("jpg")),
    "syncItems" -> JsArray(JsObject("name" -> JsString("sub folder 1"), "path" -> JsString("sub folder 1"))))

  def randomizeSettingsPath = {
    val randomString = UUID.randomUUID.toString.substring(0, 4)
    settingsPath.replace("settings", s"settings_$randomString")
  }
}
