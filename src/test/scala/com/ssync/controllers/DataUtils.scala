package com.ssync.controllers

import java.io.File

import com.ssync.models.Settings
import spray.json.{JsArray, JsObject, JsString}

object DataUtils {

  val getSeparator = File.separator
  val source = getClass.getResource(getSeparator + "source").getPath
  val destination = getClass.getResource(getSeparator + "destination").getPath

  val defaultSettings: Settings = new Settings(source, destination, Array("jpg"))
  val testJSON = JsObject("source" -> JsString(source),
    "destination" -> JsString(destination), "extensions" -> JsArray(JsString("jpg")))
}
