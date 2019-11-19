package com.ssync.service

import com.ssync.controllers.SettingsController

trait SyncService extends SettingsController {

  private val settings = loadSettings


}
