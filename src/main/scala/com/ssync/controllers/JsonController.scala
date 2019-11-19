package com.ssync.controllers

import com.ssync.models.SettingsJsonProtocol._
import com.ssync.models._
import spray.json._

trait JsonController {

  def convertSettingsToJson(content: Settings): JsValue = {
    content.toJson
  }

  def convertJsonToSettings(content: String): Settings = {
    content.parseJson.convertTo[Settings]
  }
}
