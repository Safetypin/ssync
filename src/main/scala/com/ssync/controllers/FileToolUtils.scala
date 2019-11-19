package com.ssync.controllers

import java.io.File

import com.ssync.models.Settings

object FileToolUtils {
  val settingsPath = getCurrentDirectory + getSeparator + "settings.json"
  val defaultSettings =
    Settings(getCurrentDirectory + getSeparator + "source",
      getCurrentDirectory + getSeparator + "destination", Array("jpg"))

  def getCurrentDirectory = new File(jarDir).toString

  def jarDir = jarFile.getParentFile().getPath()

  def jarFile = new File(codeSource.getLocation().toURI().getPath())

  def codeSource = getClass.getProtectionDomain().getCodeSource()

  def getSeparator = File.separator

}
