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

    logger.info(s"$processedItems items processed.")
    logger.info(s"$successfulProcessedItems items successfully processed.")
    logger.info(s"$unsuccessfulProcessedItems items unsuccessfully processed.")
    val cleanedItems = syncItems
      .map(item => {
        cleanSyncItemSource(item)
      })
    val processedCleanedItems = processedSyncItems.length
    val successfulCleanedProcessedItems = cleanedItems.count(_.isSuccess)
    val unsuccessfulCleanedProcessedItems = cleanedItems.count(_.isFailure)
    logger.info(s"$processedCleanedItems items cleaned.")
    logger.info(s"$successfulCleanedProcessedItems items successfully cleaned.")
    logger.info(s"$unsuccessfulCleanedProcessedItems items unsuccessfully cleaned.")
  }
}
