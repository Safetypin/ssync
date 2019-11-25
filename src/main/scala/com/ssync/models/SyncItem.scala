package com.ssync.models

case class SyncItem(
                     Name: String,
                     SourcePath: String,
                     DestinationPath: String,
                     Extensions: List[String],
                     IgnoredExtensions: List[String],
                     ProtectedFolders: List[String],
                     SyncFileItems: List[SyncFileItem] = List()
                   )
