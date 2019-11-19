package com.ssync.controllers

import java.io.{File, FileNotFoundException}

import com.ssync.controllers.FileToolUtils._
import org.scalatest.Matchers._
import org.scalatest.{BeforeAndAfter, BeforeAndAfterEach, FlatSpec}

class SettingsControllerIntegrationTest extends FlatSpec
  with BeforeAndAfter
  with BeforeAndAfterEach
  with SettingsController {

  before {
    deleteSettings(settingsPath)
  }

  "loadSettings" should "throw an exception if there is no settings file" in {
    intercept[FileNotFoundException](loadSettings)
  }
  it should "should produce a Settings if settings file exists" in {
    intercept[FileNotFoundException](loadSettings)

    val result = loadSettings
    result.source.contains("source") shouldEqual true
    result.destination.contains("destination") shouldEqual true
    result.extensions shouldEqual Array("jpg")
  }

  def deleteSettings(settingsPath: String) = {
    new File(settingsPath).delete
  }
}
