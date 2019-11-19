package com.ssync.controllers


import java.util.UUID

import com.ssync.controllers.FileToolUtils._
import com.ssync.models.Settings
import spray.json.{JsArray, JsObject, JsString}

object ItDataUtils {

  val source = getClass.getResource(getSeparator + "source").getPath
  val destination = getClass.getResource(getSeparator + "destination").getPath

  val defaultSettings: Settings = new Settings(source, destination, Array("jpg"))
  val testJSON = JsObject("source" -> JsString(source),
    "destination" -> JsString(destination), "extensions" -> JsArray(JsString("jpg")))

  def randomizeSettingsPath = {
    val randomString = UUID.randomUUID.toString.substring(0, 4)
    settingsPath.replace("settings", s"settings_$randomString")
  }
}
