package com.ssync.controllers

import java.io.File
import java.util.UUID

import com.ssync.models.{SettingSyncItem, Settings}

object FileToolUtils {
  val settingsPath = getCurrentDirectory + getSeparator + "settings.json"
  val defaultSettings =
    Settings(
      getCurrentDirectory + getSeparator + "source",
      getCurrentDirectory + getSeparator + "destination",
      Array("jpg"),
      Array(""),
      Seq(
        SettingSyncItem(
          "sub directory 1",
          "sub directory 1",
          Array("")
        )
      )
    )

  def getCurrentDirectory = new File(jarDir).toString

  def jarDir = jarFile.getParentFile().getPath()

  def jarFile = new File(codeSource.getLocation().toURI().getPath())

  def codeSource = getClass.getProtectionDomain().getCodeSource()

  def getSeparator = File.separator

  def randomString: String = {
    val randomString = UUID.randomUUID.toString.substring(0, 4)
    randomString
  }
}
