package com.ssync.models

case class Settings(
                     source: String,
                     destination: String,
                     extensions: Array[String],
                     syncItems: Seq[SyncItem]
                   )
