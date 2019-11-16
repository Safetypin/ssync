package com.ssync.controllers

import com.ssync.models.SettingsJsonProtocol._
import com.ssync.models._
import spray.json._

trait JsonController {

  def ConvertSettingsToJson(content: Settings): JsValue = {
    content.toJson
  }

  def ConvertJsonToSettings(content: String): Settings = {
    content.parseJson.convertTo[Settings]
  }
}
