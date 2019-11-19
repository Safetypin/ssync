package com.ssync.controllers

import com.ssync.models.{Settings, SyncItem}
import com.ssync.controllers.FileToolUtils._

trait SyncController {
  def convertSettingSyncItemsToSyncItems(settings: Settings):Seq[SyncItem]  = {

    val settingSyncItems = settings.SyncItems
    settingSyncItems.map(item => SyncItem(
      item.Name,
      mergeSourcePathWithSyncItemPath(settings, item.Path),
      mergeDestinationPathWithSyncItemPath(settings,item.Path),
      settings.Extensions.toList,
      List(""),
      List("")))
  }

  private def mergeParentPathWithSyncItemPath(parentPath: String, syncItemPath: String) = {
    parentPath + getSeparator + syncItemPath
  }

  private def mergeSourcePathWithSyncItemPath(settings: Settings, syncItemPath: String) = {
    mergeParentPathWithSyncItemPath(settings.Source, syncItemPath)
  }

  private def mergeDestinationPathWithSyncItemPath(settings: Settings, syncItemPath: String) = {
    mergeParentPathWithSyncItemPath(settings.Destination, syncItemPath)
  }
}
