package com.ssync.models

import com.ssync.models.SettingSyncItemJsonProtocol._
import spray.json.DefaultJsonProtocol

object SettingsJsonProtocol extends DefaultJsonProtocol {
  implicit val settingsFormat = jsonFormat4(Settings)
}
