package com.ssync.models

object FileState extends Enumeration {
  type FileState = Value
  val INITIAL, MOVED, UNABLE_TO_MOVE, RENAMED = Value
}
