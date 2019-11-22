package com.ssync.service

import com.ssync.controllers.{SettingsController, SyncController}

trait SyncService extends SettingsController with SyncController {

  val syncItems = convertSettingSyncItemsToSyncItems(settings)
  private val settings = loadSettings


}
