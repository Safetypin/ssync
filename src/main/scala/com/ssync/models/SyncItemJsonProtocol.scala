package com.ssync.models

import spray.json.DefaultJsonProtocol

object SyncItemJsonProtocol extends DefaultJsonProtocol {
  implicit val syncItemFormat = jsonFormat2(SyncItem)
}
