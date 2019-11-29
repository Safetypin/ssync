package com.ssync.service

import com.ssync.controllers.{SettingsController, SyncController}
import com.typesafe.scalalogging.LazyLogging

trait SyncService extends
  SettingsController
  with SyncController
  with LazyLogging {

  def runSyncService() = {
    val settings = loadSettings
    val syncItems = convertSettingSyncItemsToSyncItems(settings)
    val processedSyncItems = syncItems.map(item => processSyncItem(item))
    val processedItems = processedSyncItems.length
    val successfulProcessedItems = processedSyncItems.count(_.isSuccess)
    val unsuccessfulProcessedItems = processedSyncItems.count(_.isFailure)

    logger.info(s"Number of directories processed: $processedItems.")
    logger.info(s"Number of directories successfully processed: $successfulProcessedItems.")
    logger.info(s"umber of directories unsuccessfully processed: $unsuccessfulProcessedItems.")
    val cleanedItems = syncItems
      .map(item => {
        cleanSyncItemSource(item)
      })
    val processedCleanedItems = processedSyncItems.length
    val successfulCleanedProcessedItems = cleanedItems.count(_.isSuccess)
    val unsuccessfulCleanedProcessedItems = cleanedItems.count(_.isFailure)
    logger.info(s"Number of directories cleaned: $processedCleanedItems.")
    logger.info(s"Number of directories successfully cleaned: $successfulCleanedProcessedItems.")
    logger.info(s"Number of directories unsuccessfully cleaned: $unsuccessfulCleanedProcessedItems.")
  }
}
