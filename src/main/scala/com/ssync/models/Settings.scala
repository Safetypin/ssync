package com.ssync.models

case class Settings(
                     Source: String,
                     Destination: String,
                     Extensions: Array[String],
                     IgnoredExtensions: Array[String],
                     SyncItems: Seq[SettingSyncItem]
                   )
