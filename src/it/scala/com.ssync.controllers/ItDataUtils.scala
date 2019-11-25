package com.ssync.controllers


import java.util.UUID

import com.ssync.controllers.FileToolUtils._
import com.ssync.models.{SettingSyncItem, Settings}
import spray.json.{JsArray, JsObject, JsString}

object ItDataUtils {

  val source = getClass.getResource(getSeparator + "source").getPath
  val destination = getClass.getResource(getSeparator + "destination").getPath

  val defaultSettings: Settings = new Settings(
    source,
    destination,
    Array("jpg"),
    Array(""),
    Seq(SettingSyncItem(
      "sub folder 1",
      "sub folder 1",
      Array(""))
    )
  )
  val testJSON = JsObject(
    "Source" -> JsString(source),
    "Destination" -> JsString(destination),
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

  def randomizeSettingsPath = {
    val randomString = UUID.randomUUID.toString.substring(0, 4)
    settingsPath.replace("settings", s"settings_$randomString")
  }
}
