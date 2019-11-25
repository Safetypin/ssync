package com.ssync.models

case class SettingSyncItem(
                            Name: String,
                            Path: String,
                            ProtectedDirectories: Array[String]
                          )
