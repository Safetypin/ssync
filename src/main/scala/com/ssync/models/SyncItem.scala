package com.ssync.models

case class SyncItem(
                     Name: String,
                     SourcePath: String,
                     DestinationPath: String,
                     Extensions: List[String],
                     IgnoredExtensions: List[String] = List(),
                     ProtectedDirectories: List[String] = List(),
                     SyncFileItems: List[SyncFileItem] = List()
                   )
