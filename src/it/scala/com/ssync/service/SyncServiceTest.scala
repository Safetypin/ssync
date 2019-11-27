package com.ssync.service

import java.io.FileNotFoundException

import better.files.File
import com.ssync.controllers.FileToolUtils.getSeparator
import com.ssync.controllers.ItDataUtils._
import com.ssync.models.{SettingSyncItem, Settings}
import org.scalatest.Matchers._
import org.scalatest.{BeforeAndAfter, BeforeAndAfterEach, FlatSpec}

class SyncServiceTest extends FlatSpec
  with BeforeAndAfter
  with BeforeAndAfterEach
  with SyncService {

  override def beforeEach {
    setupTestResources
  }

  override def afterEach {
    teardownTestResources
  }

  "run" should "throw an exception on first run" in {
    intercept[FileNotFoundException](runSyncService())
  }
  it should "return a move all files from source-sub 1 to destination-sub" in {

    val exampleSettings: Settings = Settings(
      sourcePath,
      destinationPath,
      Array("*"),
      Array(),
      Seq(SettingSyncItem(
        "sub folder 1",
        "sub 1",
        Array())
      )
    )

    createSettingJson(settingsPath, exampleSettings)
    runSyncService()
    val subSourceDirectory = File(s"$sourcePath$getSeparator" + "sub 1")
    val subDestinationDirectory = File(s"$destinationPath$getSeparator" + "sub 1")
    subSourceDirectory.exists shouldEqual true
    subSourceDirectory.isEmpty shouldEqual true
    subDestinationDirectory.exists shouldEqual true
    val subDestinationFiles = subDestinationDirectory.list.toList
    subDestinationFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("first.txt")) shouldEqual 1
    subDestinationFiles.exists(f => f.name.equals("firstj.jpg")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("firstj.jpg")) shouldEqual 1
  }

  private def createSettingJson(filename: String, settings: Settings) = {
    val json = convertSettingsToJson(settings)
    createFile(filename, json.toString)
  }

}
