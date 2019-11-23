package com.ssync.models

import better.files.File
import com.ssync.models.FileState._

case class SyncFileItem(
                       FileItem: File,
                       Destination: File,
                       State: FileState = INITIAL,
                       MoveError: Option[Throwable] = None
                       )
