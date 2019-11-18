package com.ssync.controllers

import java.io.File

import com.ssync.models.Settings

object FileToolUtils {
  def codeSource = getClass.getProtectionDomain().getCodeSource()

  def jarFile = new File(codeSource.getLocation().toURI().getPath())

  def jarDir = jarFile.getParentFile().getPath()

  def getCurrentDirectory = new File(jarDir).toString

  def getSeparator = File.separator
  val settingsPath = getCurrentDirectory + getSeparator + "settings.json"
  val defaultSettings =
    Settings(getCurrentDirectory + getSeparator + "source",
      getCurrentDirectory + getSeparator + "destination", Array("jpg"))

}
