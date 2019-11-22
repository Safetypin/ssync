package com.ssync.service

import com.ssync.controllers.{SettingsController, SyncController}

trait SyncService extends SettingsController with SyncController {

  private val settings = loadSettings
  val syncItems = convertSettingSyncItemsToSyncItems(settings)


}
