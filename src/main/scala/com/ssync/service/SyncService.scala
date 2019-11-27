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
    val successfulProcessedItems = processedSyncItems.filter(_.isSuccess).length
    val unsuccessfulProcessedItems = processedSyncItems.filter(_.isFailure).length

    logger.info(s"$processedItems items processed.")
    logger.info(s"$successfulProcessedItems items successfully processed.")
    logger.info(s"$unsuccessfulProcessedItems items unsuccessfully processed.")
//    syncItems
//      .map(item => {
//        cleanSyncItemSource(item)
//      })
  }
}
