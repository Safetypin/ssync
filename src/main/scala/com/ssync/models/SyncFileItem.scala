package com.ssync.models

import better.files.File

case class SyncFileItem(
                       FileItem: File,
                       Destination: File
                       )
