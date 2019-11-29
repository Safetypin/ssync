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

  "runSyncService" should "throw an exception on first run" in {
    intercept[FileNotFoundException](runSyncService())
  }
  it should "return a move all files from source-sub 1 to destination-sub 1" in {

    val sub = "sub 1"
    val exampleSettings: Settings = Settings(
      sourcePath,
      destinationPath,
      Array("*"),
      Array(),
      Seq(SettingSyncItem(
        "sub directory 1",
        sub,
        Array())
      )
    )

    createSettingJson(settingsPath, exampleSettings)
    runSyncService()
    val subSourceDirectory = File(s"$sourcePath$getSeparator$sub")
    val subDestinationDirectory = File(s"$destinationPath$getSeparator$sub")
    subSourceDirectory.exists shouldEqual true
    subSourceDirectory.isEmpty shouldEqual true
    subDestinationDirectory.exists shouldEqual true
    val subDestinationFiles = subDestinationDirectory.list.toList
    subDestinationFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("first.txt")) shouldEqual 1
    subDestinationFiles.exists(f => f.name.equals("firstj.jpg")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("firstj.jpg")) shouldEqual 1
  }
  it should "return a move all files from source-sub 2 to destination-sub 2" in {

    val sub = "sub 2"
    val exampleSettings: Settings = Settings(
      sourcePath,
      destinationPath,
      Array("*"),
      Array(),
      Seq(SettingSyncItem(
        "sub directory 2",
        sub,
        Array())
      )
    )

    createSettingJson(settingsPath, exampleSettings)
    runSyncService()
    val subSourceDirectory = File(s"$sourcePath$getSeparator$sub")
    val subDestinationDirectory = File(s"$destinationPath$getSeparator$sub")
    subSourceDirectory.exists shouldEqual true
    subSourceDirectory.isEmpty shouldEqual true
    subDestinationDirectory.exists shouldEqual true
    val subDestinationFiles = subDestinationDirectory.list.toList
    subDestinationFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("first.txt")) shouldEqual 1
    subDestinationFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("second.TXT")) shouldEqual 1
    subDestinationFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("test.jpg")) shouldEqual 1
  }
  it should "return a move all files from source-sub 3 to destination-sub 3" in {

    val sub = "sub 3"
    val exampleSettings: Settings = Settings(
      sourcePath,
      destinationPath,
      Array("*"),
      Array(),
      Seq(SettingSyncItem(
        "sub directory 3",
        sub,
        Array())
      )
    )

    createSettingJson(settingsPath, exampleSettings)
    runSyncService()
    val subSourceDirectory = File(s"$sourcePath$getSeparator$sub")
    val subDestinationDirectory = File(s"$destinationPath$getSeparator$sub")
    subSourceDirectory.exists shouldEqual true
    subSourceDirectory.isEmpty shouldEqual true
    subDestinationDirectory.exists shouldEqual true
    val subDestinationFiles = subDestinationDirectory.list.toList
    subDestinationFiles.exists(f => f.name.equals("sub")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("sub")) shouldEqual 1
    subDestinationFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("first.txt")) shouldEqual 1
    subDestinationFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("second.TXT")) shouldEqual 1
    subDestinationFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("test.jpg")) shouldEqual 1
    val subSubDestinationFiles = subDestinationFiles.filter(f => f.name.equals("sub")).head.list.toList
    subSubDestinationFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    subSubDestinationFiles.count(f => f.name.equals("first.txt")) shouldEqual 1
    subSubDestinationFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    subSubDestinationFiles.count(f => f.name.equals("second.TXT")) shouldEqual 1
    subSubDestinationFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
    subSubDestinationFiles.count(f => f.name.equals("test.jpg")) shouldEqual 1

  }
  it should "return a move all files from source-sub 4 to destination-sub 4" in {

    val sub = "sub 4"
    val exampleSettings: Settings = Settings(
      sourcePath,
      destinationPath,
      Array("*"),
      Array(),
      Seq(SettingSyncItem(
        "sub directory 4",
        sub,
        Array())
      )
    )

    createSettingJson(settingsPath, exampleSettings)
    runSyncService()
    val subSourceDirectory = File(s"$sourcePath$getSeparator$sub")
    val subDestinationDirectory = File(s"$destinationPath$getSeparator$sub")
    subSourceDirectory.exists shouldEqual true
    subSourceDirectory.isEmpty shouldEqual true
    subDestinationDirectory.exists shouldEqual true
    val subDestinationFiles = subDestinationDirectory.list.toList
    subDestinationFiles.exists(f => f.name.equals("sub")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("sub")) shouldEqual 1
    subDestinationFiles.exists(f => f.name.equals("sub 1")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("sub 1")) shouldEqual 1
    subDestinationFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("first.txt")) shouldEqual 1
    subDestinationFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("second.TXT")) shouldEqual 1
    subDestinationFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("test.jpg")) shouldEqual 1
    val subSubDestinationFiles = subDestinationFiles
      .filter(f => f.name.equals("sub")).head.list.toList
    subSubDestinationFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    subSubDestinationFiles.count(f => f.name.equals("first.txt")) shouldEqual 1
    subSubDestinationFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    subSubDestinationFiles.count(f => f.name.equals("second.TXT")) shouldEqual 1
    subSubDestinationFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
    subSubDestinationFiles.count(f => f.name.equals("test.jpg")) shouldEqual 1
    val subSubSub1DestinationFiles = subSubDestinationFiles
      .filter(f => f.name.equals("sub 1")).head.list.toList
    subSubSub1DestinationFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    subSubSub1DestinationFiles.count(f => f.name.equals("first.txt")) shouldEqual 1
    subSubSub1DestinationFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    subSubSub1DestinationFiles.count(f => f.name.equals("second.TXT")) shouldEqual 1
    subSubSub1DestinationFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
    subSubSub1DestinationFiles.count(f => f.name.equals("test.jpg")) shouldEqual 1
    val subSub1DestinationFiles = subDestinationFiles
      .filter(f => f.name.equals("sub 1")).head.list.toList
    subSub1DestinationFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    subSub1DestinationFiles.count(f => f.name.equals("first.txt")) shouldEqual 1
    subSub1DestinationFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    subSub1DestinationFiles.count(f => f.name.equals("second.TXT")) shouldEqual 1
    subSub1DestinationFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
    subSub1DestinationFiles.count(f => f.name.equals("test.jpg")) shouldEqual 1
  }
  it should "return a move all files from source-sub 5 to destination-sub 5" in {

    val sub = "sub 5"
    val exampleSettings: Settings = Settings(
      sourcePath,
      destinationPath,
      Array("*"),
      Array(),
      Seq(SettingSyncItem(
        "sub directory 5",
        sub,
        Array())
      )
    )

    createSettingJson(settingsPath, exampleSettings)
    runSyncService()
    val subSourceDirectory = File(s"$sourcePath$getSeparator$sub")
    val subDestinationDirectory = File(s"$destinationPath$getSeparator$sub")
    subSourceDirectory.exists shouldEqual true
    subSourceDirectory.isEmpty shouldEqual true
    subDestinationDirectory.exists shouldEqual true
    val subDestinationFiles = subDestinationDirectory.list.toList
    subDestinationFiles.exists(f => f.name.equals("sub")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("sub")) shouldEqual 1
    subDestinationFiles.exists(f => f.name.equals("sub 1")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("sub 1")) shouldEqual 1
    subDestinationFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("first.txt")) shouldEqual 1
    subDestinationFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("second.TXT")) shouldEqual 1
    subDestinationFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("test.jpg")) shouldEqual 1
    val subSubDestinationFiles = subDestinationFiles
      .filter(f => f.name.equals("sub")).head.list.toList
    subSubDestinationFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    subSubDestinationFiles.count(f => f.name.equals("first.txt")) shouldEqual 1
    subSubDestinationFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    subSubDestinationFiles.count(f => f.name.equals("second.TXT")) shouldEqual 1
    subSubDestinationFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
    subSubDestinationFiles.count(f => f.name.equals("test.jpg")) shouldEqual 1
    val subSubSub1DestinationFiles = subSubDestinationFiles
      .filter(f => f.name.equals("sub 1")).head.list.toList
    subSubSub1DestinationFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    subSubSub1DestinationFiles.count(f => f.name.equals("first.txt")) shouldEqual 1
    subSubSub1DestinationFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    subSubSub1DestinationFiles.count(f => f.name.equals("second.TXT")) shouldEqual 1
    subSubSub1DestinationFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
    subSubSub1DestinationFiles.count(f => f.name.equals("test.jpg")) shouldEqual 1
    val subSub1DestinationFiles = subDestinationFiles
      .filter(f => f.name.equals("sub 1")).head.list.toList
    subSub1DestinationFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    subSub1DestinationFiles.count(f => f.name.equals("first.txt")) shouldEqual 1
    subSub1DestinationFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    subSub1DestinationFiles.count(f => f.name.equals("second.TXT")) shouldEqual 1
    subSub1DestinationFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
    subSub1DestinationFiles.count(f => f.name.equals("test.jpg")) shouldEqual 1
  }
  it should "return a move just .txt extension files from source-sub 1 to destination-sub 1" in {

    val sub = "sub 1"
    val exampleSettings: Settings = Settings(
      sourcePath,
      destinationPath,
      Array("txt"),
      Array(),
      Seq(SettingSyncItem(
        "sub directory 1",
        sub,
        Array())
      )
    )

    createSettingJson(settingsPath, exampleSettings)
    runSyncService()
    val subSourceDirectory = File(s"$sourcePath$getSeparator$sub")
    val subDestinationDirectory = File(s"$destinationPath$getSeparator$sub")
    subSourceDirectory.exists shouldEqual true
    subSourceDirectory.isEmpty shouldEqual false
    subDestinationDirectory.exists shouldEqual true
    val subDestinationFiles = subDestinationDirectory.list.toList
    subDestinationFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("first.txt")) shouldEqual 1
    val subSourceFiles = subSourceDirectory.list.toList
    subSourceFiles.exists(f => f.name.equals("firstj.jpg")) shouldEqual true
    subSourceFiles.count(f => f.name.equals("firstj.jpg")) shouldEqual 1
  }
  it should "return a move just .txt extension files from source-sub 2 to destination-sub 2" in {

    val sub = "sub 2"
    val exampleSettings: Settings = Settings(
      sourcePath,
      destinationPath,
      Array("txt"),
      Array(),
      Seq(SettingSyncItem(
        "sub directory 2",
        sub,
        Array())
      )
    )

    createSettingJson(settingsPath, exampleSettings)
    runSyncService()
    val subSourceDirectory = File(s"$sourcePath$getSeparator$sub")
    val subDestinationDirectory = File(s"$destinationPath$getSeparator$sub")
    subSourceDirectory.exists shouldEqual true
    subSourceDirectory.isEmpty shouldEqual false
    subDestinationDirectory.exists shouldEqual true
    val subDestinationFiles = subDestinationDirectory.list.toList
    subDestinationFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("first.txt")) shouldEqual 1
    subDestinationFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("second.TXT")) shouldEqual 1
    val subSourceFiles = subSourceDirectory.list.toList
    subSourceFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
    subSourceFiles.count(f => f.name.equals("test.jpg")) shouldEqual 1
  }
  it should "return a move just .txt extension files from source-sub 3 to destination-sub 3" in {

    val sub = "sub 3"
    val exampleSettings: Settings = Settings(
      sourcePath,
      destinationPath,
      Array("txt"),
      Array(),
      Seq(SettingSyncItem(
        "sub directory 3",
        sub,
        Array())
      )
    )

    createSettingJson(settingsPath, exampleSettings)
    runSyncService()
    val subSourceDirectory = File(s"$sourcePath$getSeparator$sub")
    val subDestinationDirectory = File(s"$destinationPath$getSeparator$sub")
    subSourceDirectory.exists shouldEqual true
    subSourceDirectory.isEmpty shouldEqual false
    subDestinationDirectory.exists shouldEqual true
    val subDestinationFiles = subDestinationDirectory.list.toList
    subDestinationFiles.exists(f => f.name.equals("sub")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("sub")) shouldEqual 1
    subDestinationFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("first.txt")) shouldEqual 1
    subDestinationFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("second.TXT")) shouldEqual 1
    val subSourceFiles = subSourceDirectory.list.toList
    subSourceFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
    subSourceFiles.count(f => f.name.equals("test.jpg")) shouldEqual 1
    val subSubDestinationFiles = subDestinationFiles.filter(f => f.name.equals("sub")).head.list.toList
    subSubDestinationFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    subSubDestinationFiles.count(f => f.name.equals("first.txt")) shouldEqual 1
    subSubDestinationFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    subSubDestinationFiles.count(f => f.name.equals("second.TXT")) shouldEqual 1
    val subSubSourceFiles = subSourceFiles.filter(f => f.name.equals("sub")).head.list.toList
    subSubSourceFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
    subSubSourceFiles.count(f => f.name.equals("test.jpg")) shouldEqual 1

  }
  it should "return a move all files from source-sub 3 to destination-sub 3 and leave source sub directory" in {

    val sub = "sub 3"
    val exampleSettings: Settings = Settings(
      sourcePath,
      destinationPath,
      Array("*"),
      Array(),
      Seq(SettingSyncItem(
        "sub directory 3",
        sub,
        Array("sub")
      )
      )
    )

    createSettingJson(settingsPath, exampleSettings)
    runSyncService()
    val subSourceDirectory = File(s"$sourcePath$getSeparator$sub")
    val subDestinationDirectory = File(s"$destinationPath$getSeparator$sub")
    subSourceDirectory.exists shouldEqual true
    subSourceDirectory.isEmpty shouldEqual false
    subDestinationDirectory.exists shouldEqual true
    val subDestinationFiles = subDestinationDirectory.list.toList
    subDestinationFiles.exists(f => f.name.equals("sub")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("sub")) shouldEqual 1
    subDestinationFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("first.txt")) shouldEqual 1
    subDestinationFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("second.TXT")) shouldEqual 1
    subDestinationFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("test.jpg")) shouldEqual 1
    val subSubDestinationFiles = subDestinationFiles.filter(f => f.name.equals("sub")).head.list.toList
    subSubDestinationFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    subSubDestinationFiles.count(f => f.name.equals("first.txt")) shouldEqual 1
    subSubDestinationFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    subSubDestinationFiles.count(f => f.name.equals("second.TXT")) shouldEqual 1
    subSubDestinationFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
    subSubDestinationFiles.count(f => f.name.equals("test.jpg")) shouldEqual 1
    val subSubSourceDirectory = subSourceDirectory.list.toList.filter(f => f.name.equals("sub")).head
    subSubSourceDirectory.isDirectory shouldEqual true
    subSubSourceDirectory.isEmpty shouldEqual true
  }
  it should "return a move all files from source-sub 4 to destination-sub 4 and leave source sub and sub 1 directory" in {

    val sub = "sub 4"
    val exampleSettings: Settings = Settings(
      sourcePath,
      destinationPath,
      Array("*"),
      Array(),
      Seq(SettingSyncItem(
        "sub directory 4",
        sub,
        Array("sub", "sub 1"))
      )
    )

    createSettingJson(settingsPath, exampleSettings)
    runSyncService()
    val subSourceDirectory = File(s"$sourcePath$getSeparator$sub")
    val subDestinationDirectory = File(s"$destinationPath$getSeparator$sub")
    subSourceDirectory.exists shouldEqual true
    subSourceDirectory.isEmpty shouldEqual false
    subDestinationDirectory.exists shouldEqual true
    val subDestinationFiles = subDestinationDirectory.list.toList
    subDestinationFiles.exists(f => f.name.equals("sub")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("sub")) shouldEqual 1
    subDestinationFiles.exists(f => f.name.equals("sub 1")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("sub 1")) shouldEqual 1
    subDestinationFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("first.txt")) shouldEqual 1
    subDestinationFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("second.TXT")) shouldEqual 1
    subDestinationFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("test.jpg")) shouldEqual 1
    val subSubDestinationFiles = subDestinationFiles
      .filter(f => f.name.equals("sub")).head.list.toList
    subSubDestinationFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    subSubDestinationFiles.count(f => f.name.equals("first.txt")) shouldEqual 1
    subSubDestinationFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    subSubDestinationFiles.count(f => f.name.equals("second.TXT")) shouldEqual 1
    subSubDestinationFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
    subSubDestinationFiles.count(f => f.name.equals("test.jpg")) shouldEqual 1
    val subSubSub1DestinationFiles = subSubDestinationFiles
      .filter(f => f.name.equals("sub 1")).head.list.toList
    subSubSub1DestinationFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    subSubSub1DestinationFiles.count(f => f.name.equals("first.txt")) shouldEqual 1
    subSubSub1DestinationFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    subSubSub1DestinationFiles.count(f => f.name.equals("second.TXT")) shouldEqual 1
    subSubSub1DestinationFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
    subSubSub1DestinationFiles.count(f => f.name.equals("test.jpg")) shouldEqual 1
    val subSub1DestinationFiles = subDestinationFiles
      .filter(f => f.name.equals("sub 1")).head.list.toList
    subSub1DestinationFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    subSub1DestinationFiles.count(f => f.name.equals("first.txt")) shouldEqual 1
    subSub1DestinationFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    subSub1DestinationFiles.count(f => f.name.equals("second.TXT")) shouldEqual 1
    subSub1DestinationFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
    subSub1DestinationFiles.count(f => f.name.equals("test.jpg")) shouldEqual 1
    val subSubSourceDirectory = subSourceDirectory.list.toList.filter(f => f.name.equals("sub")).head
    subSubSourceDirectory.isDirectory shouldEqual true
    subSubSourceDirectory.isEmpty shouldEqual false
    subSubSourceDirectory.list.toList.length shouldEqual 1
    val subSub1SourceDirectory = subSourceDirectory.list.toList.filter(f => f.name.equals("sub 1")).head
    subSub1SourceDirectory.isDirectory shouldEqual true
    subSub1SourceDirectory.isEmpty shouldEqual true
    val subSubSub1SourceDirectory = subSubSourceDirectory.list.toList.filter(f => f.name.equals("sub 1")).head
    subSubSub1SourceDirectory.isDirectory shouldEqual true
    subSubSub1SourceDirectory.isEmpty shouldEqual true
  }

  it should "return a move just .txt extension files from source-sub 3 to destination-sub 3 with exclusion jpg " in {

    val sub = "sub 3"
    val exampleSettings: Settings = Settings(
      sourcePath,
      destinationPath,
      Array("*"),
      Array("jpg"),
      Seq(SettingSyncItem(
        "sub directory 3",
        sub,
        Array())
      )
    )

    createSettingJson(settingsPath, exampleSettings)
    runSyncService()
    val subSourceDirectory = File(s"$sourcePath$getSeparator$sub")
    val subDestinationDirectory = File(s"$destinationPath$getSeparator$sub")
    subSourceDirectory.exists shouldEqual true
    subSourceDirectory.isEmpty shouldEqual false
    subDestinationDirectory.exists shouldEqual true
    val subDestinationFiles = subDestinationDirectory.list.toList
    subDestinationFiles.exists(f => f.name.equals("sub")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("sub")) shouldEqual 1
    subDestinationFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("first.txt")) shouldEqual 1
    subDestinationFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    subDestinationFiles.count(f => f.name.equals("second.TXT")) shouldEqual 1
    val subSourceFiles = subSourceDirectory.list.toList
    subSourceFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
    subSourceFiles.count(f => f.name.equals("test.jpg")) shouldEqual 1
    val subSubDestinationFiles = subDestinationFiles.filter(f => f.name.equals("sub")).head.list.toList
    subSubDestinationFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    subSubDestinationFiles.count(f => f.name.equals("first.txt")) shouldEqual 1
    subSubDestinationFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    subSubDestinationFiles.count(f => f.name.equals("second.TXT")) shouldEqual 1
    val subSubSourceFiles = subSourceFiles.filter(f => f.name.equals("sub")).head.list.toList
    subSubSourceFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
    subSubSourceFiles.count(f => f.name.equals("test.jpg")) shouldEqual 1

  }

  private def createSettingJson(filename: String, settings: Settings) = {
    val json = convertSettingsToJson(settings)
    createFile(filename, json.toString)
  }

}
