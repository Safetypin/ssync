package com.ssync.models

import spray.json.DefaultJsonProtocol

object SettingsJsonProtocol extends DefaultJsonProtocol {
  implicit val settingsFormat = jsonFormat3(Settings)
}
