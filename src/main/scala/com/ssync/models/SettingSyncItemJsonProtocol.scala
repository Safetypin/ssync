package com.ssync.models

import spray.json.DefaultJsonProtocol

object SettingSyncItemJsonProtocol extends DefaultJsonProtocol {
  implicit val settingSyncItemFormat = jsonFormat3(SettingSyncItem)
}
