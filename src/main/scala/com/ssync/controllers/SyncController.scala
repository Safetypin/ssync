package com.ssync.controllers

import java.io.FileNotFoundException

import com.ssync.controllers.FileToolUtils._
import com.ssync.models.{Settings, SyncItem}
import com.typesafe.scalalogging.LazyLogging

import scala.util.Try

trait SyncController extends LazyLogging with FileToolsController {
  def convertSettingSyncItemsToSyncItems(settings: Settings): Seq[SyncItem] = {
    val settingSyncItems = settings.SyncItems
    settingSyncItems.map(item => SyncItem(
      item.Name,
      mergeSourcePathWithSyncItemPath(settings, item.Path),
      mergeDestinationPathWithSyncItemPath(settings, item.Path),
      settings.Extensions.toList,
      List(""),
      List("")))
  }

  private def mergeSourcePathWithSyncItemPath(settings: Settings, syncItemPath: String) = {
    mergeParentPathWithSyncItemPath(settings.Source, syncItemPath)
  }

  private def mergeDestinationPathWithSyncItemPath(settings: Settings, syncItemPath: String) = {
    mergeParentPathWithSyncItemPath(settings.Destination, syncItemPath)
  }

  private def mergeParentPathWithSyncItemPath(parentPath: String, syncItemPath: String) = {
    parentPath + getSeparator + syncItemPath
  }

  def processSyncItem(syncItem: SyncItem): Try[SyncItem] = {
    val source = syncItem.SourcePath
    Try {
      doesSourceExist(source)
      val syncItemFiles = collectFilesBasedOnExtensions(source, syncItem.Extensions)
      //TODO does destination exist
      //TODO if filtered files exist and destination doesn't exist create destination
      //TODO move files
      syncItem
    }
  }

  private def doesSourceExist(source: String) = {
    if (doesDirectoryExist(source) equals false) {
      logger.error(s"Source destination: $source does not exist")
      throw new FileNotFoundException
    }
  }
}
