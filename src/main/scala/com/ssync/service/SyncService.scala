package com.ssync.service

import com.ssync.controllers.SettingsController
import com.ssync.models.Settings

trait SyncService extends SettingsController {

  private val settings = loadSettings


}
