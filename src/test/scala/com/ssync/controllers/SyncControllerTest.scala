package com.ssync.controllers

import com.ssync.controllers.DataUtils._
import com.ssync.controllers.FileToolUtils.getSeparator
import com.ssync.models.{SettingSyncItem, Settings}
import org.scalatest.Matchers._
import org.scalatest.{BeforeAndAfter, BeforeAndAfterEach, FlatSpec}

class SyncControllerTest extends FlatSpec
  with BeforeAndAfter
  with BeforeAndAfterEach
  with SyncController {
  "convertSettingSyncItemsToSyncItems" should "an empty list when SettingSyncItems is empty" in {
    val settings =
      Settings(source,
        destination,
        Array("jpg"),
        Seq())
    val returnedSyncItems = convertSettingSyncItemsToSyncItems(settings)
    returnedSyncItems.isEmpty shouldEqual true
  }
  it should "return a list of SyncItems with populated SettingSyncItems" in {
    val sub1path = "sub1"
    val sub2path = "sub2"
    val settings =
      Settings(source,
        destination,
        Array("jpg"),
        Seq(SettingSyncItem("sub 1", sub1path), SettingSyncItem("sub 2", sub2path)))
    val returnedSyncItems = convertSettingSyncItemsToSyncItems(settings)
    returnedSyncItems.isEmpty shouldEqual false
    returnedSyncItems.length shouldEqual 2

    val firstSyncItem = returnedSyncItems.head
    val secondSyncItem = returnedSyncItems.last
    firstSyncItem.Name shouldEqual "sub 1"
    firstSyncItem.SourcePath shouldEqual s"$source$getSeparator$sub1path"
    firstSyncItem.DestinationPath shouldEqual s"$destination$getSeparator$sub1path"
    firstSyncItem.Extensions shouldEqual List("jpg")

    secondSyncItem.Name shouldEqual "sub 2"
    secondSyncItem.SourcePath shouldEqual s"$source$getSeparator$sub2path"
    secondSyncItem.DestinationPath shouldEqual s"$destination$getSeparator$sub2path"
    secondSyncItem.Extensions shouldEqual List("jpg")
  }
}
