package com.ssync.controllers

import java.io.FileNotFoundException
import com.ssync.controllers.DataUtils._
import com.ssync.controllers.FileToolUtils._
import org.mockito.MockitoSugar
import org.scalatest.Matchers._
import org.scalatest.{BeforeAndAfter, BeforeAndAfterEach, FlatSpec}
import scala.util.Success

class SettingsControllerTest extends FlatSpec
  with BeforeAndAfter
  with BeforeAndAfterEach
  with MockitoSugar
  with SettingsController {

  before{
//    fileToolsController
  }

  val fileToolsController: FileToolsController = mock[FileToolsController]

  "loadSettings" should "throw an exception if there is no settings file" in {
    val randomSettingsPath = randomizeSettingsPath
    when(fileToolsController.openAndReadFile(settingsPath)) thenThrow new FileNotFoundException

    intercept[FileNotFoundException](loadSettings)
  }
  it should "should produce a Settings if settings file exists" in {
    val randomSettingsPath = randomizeSettingsPath
    when(fileToolsController.openAndReadFile(settingsPath)) thenReturn Success(testJSON.toString)

    val result = loadSettings
    result.source.contains("source") shouldEqual true
    result.destination.contains("destination") shouldEqual true
    result.extensions shouldEqual Array("jpg")
  }

}
