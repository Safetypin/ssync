package com.ssync.controllers

import java.io.FileNotFoundException

import better.files.File
import com.ssync.controllers.DataUtils._
import com.ssync.controllers.FileToolUtils.getSeparator
import com.ssync.models.FileState._
import com.ssync.models.{SettingSyncItem, Settings, SyncItem}
import org.scalatest.Matchers._
import org.scalatest.{BeforeAndAfter, BeforeAndAfterEach, FlatSpec}

class SyncControllerTest extends FlatSpec
  with BeforeAndAfter
  with BeforeAndAfterEach
  with SyncController {

  override def beforeEach {
    setupTestResources
  }

  override def afterEach {
    teardownTestResources
  }

  "convertSettingSyncItemsToSyncItems" should "an empty list when SettingSyncItems is empty" in {
    val settings =
      Settings(sourcePath,
        destinationPath,
        Array("jpg"),
        Array(""),
        Seq())
    val returnedSyncItems = convertSettingSyncItemsToSyncItems(settings)
    returnedSyncItems.isEmpty shouldEqual true
  }
  it should "return a list of SyncItems with populated SettingSyncItems" in {
    val sub1path = "sub1"
    val sub2path = "sub2"
    val settings =
      Settings(sourcePath,
        destinationPath,
        Array("jpg"),
        Array(""),
        Seq(SettingSyncItem("sub 1", sub1path, Array("")),
          SettingSyncItem("sub 2", sub2path, Array(""))))
    val returnedSyncItems = convertSettingSyncItemsToSyncItems(settings)
    returnedSyncItems.isEmpty shouldEqual false
    returnedSyncItems.length shouldEqual 2

    val firstSyncItem = returnedSyncItems.head
    val secondSyncItem = returnedSyncItems.last
    firstSyncItem.Name shouldEqual "sub 1"
    firstSyncItem.SourcePath shouldEqual s"$sourcePath$getSeparator$sub1path"
    firstSyncItem.DestinationPath shouldEqual s"$destinationPath$getSeparator$sub1path"
    firstSyncItem.Extensions shouldEqual List("jpg")

    secondSyncItem.Name shouldEqual "sub 2"
    secondSyncItem.SourcePath shouldEqual s"$sourcePath$getSeparator$sub2path"
    secondSyncItem.DestinationPath shouldEqual s"$destinationPath$getSeparator$sub2path"
    secondSyncItem.Extensions shouldEqual List("jpg")
  }

  "constructSyncItemFileWithDestination" should "populate destination" in {
    val file = File(s"$sourcePath$getSeparator" + "testfile.txt")
    val syncItem = SyncItem("test", sourcePath, destinationPath, List("*"), List(""), List(""))
    val syncFileItem = constructSyncItemFileWithDestination(syncItem, file)
    syncFileItem.Destination shouldEqual File(destinationPath)
  }
  it should "populate destination of sub directory" in {
    val file = File(s"$sourcePath$getSeparator" + "sub 1" + getSeparator + "firstj.jpg")
    val subDirectory = File(s"$destinationPath$getSeparator" + "sub 1")
    val syncItem = SyncItem("test", sourcePath, destinationPath, List("*"), List(""), List(""))
    val syncFileItem = constructSyncItemFileWithDestination(syncItem, file)
    syncFileItem.Destination shouldEqual subDirectory
  }

  "processSyncItem" should "handle a non existent directory with a FileNotFoundException Failure" in {
    val subSourceDirectory = s"$sourcePath$getSeparator" + "sub1"
    val subDestinationDirectory = s"$destinationPath$getSeparator" + "sub1"
    val syncItem = SyncItem(
      "sub directory 1",
      subSourceDirectory,
      subDestinationDirectory,
      List("*"),
      List(""),
      List("")
    )
    val processedSyncItem = processSyncItem(syncItem)
    processedSyncItem.isFailure shouldEqual true
    processedSyncItem.failed.get.isInstanceOf[FileNotFoundException] shouldEqual true
  }
  it should "handle a a directory with no sub directories with a Success" in {
    val subSourceDirectoryPath = s"$sourcePath$getSeparator" + "sub 1"
    val subDestinationDirectoryPath = s"$destinationPath$getSeparator" + "sub 1"
    val syncItem = SyncItem(
      "sub directory 1",
      subSourceDirectoryPath,
      subDestinationDirectoryPath,
      List("*"),
      List(""),
      List("")
    )
    val processedSyncItem = processSyncItem(syncItem)
    processedSyncItem.isSuccess shouldEqual true
    val subSourceDirectory = File(subSourceDirectoryPath)
    subSourceDirectory.isEmpty shouldEqual true
    val processedSyncFileItems = processedSyncItem.get.SyncFileItems
    processedSyncFileItems.foreach(
      item => item.State shouldEqual MOVED)
  }
  it should "handle all in a directory with sub directory with a Success" in {
    val subSourceDirectoryPath = s"$sourcePath$getSeparator" + "sub 3"
    val subDestinationDirectoryPath = s"$destinationPath$getSeparator" + "sub 3"
    val syncItem = SyncItem(
      "sub directory 1",
      subSourceDirectoryPath,
      subDestinationDirectoryPath,
      List("*"),
      List(""),
      List("")
    )
    val processedSyncItem = processSyncItem(syncItem)
    processedSyncItem.isSuccess shouldEqual true
    val subSourceDirectory = File(subSourceDirectoryPath)
    subSourceDirectory
      .listRecursively
      .filter(!_.isDirectory)
      .toList shouldEqual List.empty
    val processedSyncFileItems = processedSyncItem.get.SyncFileItems
    processedSyncFileItems.length shouldEqual 6
    processedSyncFileItems.foreach(
      item => {
        item.State shouldEqual MOVED
        item.FileItem.exists shouldEqual true
      }
    )
  }
  it should "handle .txt in a directory with sub directory with a Success" in {
    val subSourceDirectoryPath = s"$sourcePath$getSeparator" + "sub 3"
    val subDestinationDirectoryPath = s"$destinationPath$getSeparator" + "sub 3"
    val syncItem = SyncItem(
      "sub directory 1",
      subSourceDirectoryPath,
      subDestinationDirectoryPath,
      List("txt"),
      List(""),
      List("")
    )
    val processedSyncItem = processSyncItem(syncItem)
    processedSyncItem.isSuccess shouldEqual true
    val subSourceDirectory = File(subSourceDirectoryPath)
    subSourceDirectory
      .listRecursively
      .filter(!_.isDirectory)
      .toList.length shouldEqual 2
    val processedSyncFileItems = processedSyncItem.get.SyncFileItems
    processedSyncFileItems.length shouldEqual 4
    processedSyncFileItems.foreach(
      item => {
        item.State shouldEqual MOVED
        item.FileItem.exists shouldEqual true
        item.FileItem.extension(false).get shouldEqual "txt"
      }
    )
  }
}
