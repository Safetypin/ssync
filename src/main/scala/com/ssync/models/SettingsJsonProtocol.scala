package com.ssync.models

import spray.json.DefaultJsonProtocol
import com.ssync.models.SyncItemJsonProtocol._

object SettingsJsonProtocol extends DefaultJsonProtocol {
  implicit val settingsFormat = jsonFormat4(Settings)
}
