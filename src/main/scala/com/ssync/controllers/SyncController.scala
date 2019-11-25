package com.ssync.controllers

import java.io.FileNotFoundException

import better.files.File
import com.ssync.controllers.FileToolUtils._
import com.ssync.models.{Settings, SyncFileItem, SyncItem}
import com.typesafe.scalalogging.LazyLogging

import scala.util.Try

trait SyncController extends LazyLogging with FileToolsController {
  def convertSettingSyncItemsToSyncItems(settings: Settings): Seq[SyncItem] = {
    val settingSyncItems = settings.SyncItems
    settingSyncItems.map(item =>
      SyncItem(
        Name = item.Name,
        SourcePath = mergeSourcePathWithSyncItemPath(settings, item.Path),
        DestinationPath = mergeDestinationPathWithSyncItemPath(settings, item.Path),
        Extensions = settings.Extensions.toList,
        IgnoredExtensions = settings.IgnoredExtensions.toList,
        ProtectedDirectories = item.ProtectedDirectories.toList
      )
    )
  }

  private def mergeSourcePathWithSyncItemPath(settings: Settings, syncItemPath: String) = {
    mergeParentPathWithSyncItemPath(settings.Source, syncItemPath)
  }

  private def mergeParentPathWithSyncItemPath(parentPath: String, syncItemPath: String) = {
    parentPath + getSeparator + syncItemPath
  }

  private def mergeDestinationPathWithSyncItemPath(settings: Settings, syncItemPath: String) = {
    mergeParentPathWithSyncItemPath(settings.Destination, syncItemPath)
  }

  def processSyncItem(syncItem: SyncItem): Try[SyncItem] = {
    val source = syncItem.SourcePath
    Try {
      doesSourceExist(source)
      val files = collectFilesBasedOnExtensions(source, syncItem.Extensions)
      val syncFileItems = files.map {
        constructSyncItemFileWithDestination(syncItem, _)
      }
      val movedSyncFileItems = moveFiles(syncFileItems)
      SyncItem(
        syncItem.Name,
        syncItem.SourcePath,
        syncItem.DestinationPath,
        syncItem.Extensions,
        syncItem.IgnoredExtensions,
        syncItem.ProtectedDirectories,
        movedSyncFileItems)
    }
  }

  def constructSyncItemFileWithDestination(syncItem: SyncItem, file: File) = {
    val destination = file.parent.canonicalPath.replace(syncItem.SourcePath, syncItem.DestinationPath)
    val syncFileItem = SyncFileItem(file, File(destination))
    syncFileItem
  }

  private def doesSourceExist(source: String) = {
    if (doesDirectoryExist(source) equals false) {
      logger.error(s"Source destination: $source does not exist")
      throw new FileNotFoundException
    }
  }

  def cleanSyncItemSource(syncItem: SyncItem) = {
    val source = syncItem.SourcePath
    val protectedDirectories = syncItem.ProtectedDirectories
    Try {
      val directories = collectSourceDirectoriesInOrder(source, protectedDirectories)
    }
  }
}
