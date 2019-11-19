package com.ssync.models

case class Settings(
                     Source: String,
                     Destination: String,
                     Extensions: Array[String],
                     SyncItems: Seq[SettingSyncItem]
                   )
