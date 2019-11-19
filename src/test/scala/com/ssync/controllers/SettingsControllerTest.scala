package com.ssync.controllers

import java.io.FileNotFoundException

import com.ssync.controllers.DataUtils._
import com.ssync.controllers.FileToolUtils._
import com.ssync.models.SettingSyncItem
import org.mockito.MockitoSugar
import org.scalatest.Matchers._
import org.scalatest.{BeforeAndAfter, BeforeAndAfterEach, FlatSpec}

import scala.util.{Success, Try}

class SettingsControllerTest extends FlatSpec
  with BeforeAndAfter
  with BeforeAndAfterEach
  with MockitoSugar {

  before {
    //    fileToolsController
  }

  "loadSettings" should "throw an exception if there is no settings file" in {
    val mockFileToolsController: FileToolsController = mock[FileToolsController]

    val settingsController = new SettingsController {
      override def openAndReadFile(filename: String): Try[String] = mockFileToolsController.openAndReadFile(filename)
    }
    when(mockFileToolsController.openAndReadFile(settingsPath)) thenThrow new FileNotFoundException

    intercept[FileNotFoundException](settingsController.loadSettings)
  }
  it should "should produce a Settings if settings file exists" in {
    val mockFileToolsController: FileToolsController = mock[FileToolsController]

    val settingsController = new SettingsController {
      override def openAndReadFile(filename: String): Try[String] = mockFileToolsController.openAndReadFile(filename)
    }
    when(mockFileToolsController.openAndReadFile(settingsPath)) thenReturn Success(testJSON.toString)

    val result = settingsController.loadSettings
    result.Source.contains("source") shouldEqual true
    result.Destination.contains("destination") shouldEqual true
    result.Extensions shouldEqual Array("jpg")
    result.SyncItems.length shouldEqual 1
    val syncItem = result.SyncItems.head
    syncItem shouldEqual SettingSyncItem("sub folder 1", "sub folder 1")
  }

}
