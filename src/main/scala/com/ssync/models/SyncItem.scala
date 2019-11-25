package com.ssync.models

case class SyncItem(
                     Name: String,
                     SourcePath: String,
                     DestinationPath: String,
                     Extensions: List[String],
                     IgnoredExtensions: List[String],
                     ProtectedDirectories: List[String],
                     SyncFileItems: List[SyncFileItem] = List()
                   )
