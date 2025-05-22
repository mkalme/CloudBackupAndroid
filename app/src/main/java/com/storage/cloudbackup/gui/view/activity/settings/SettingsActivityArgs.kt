package com.storage.cloudbackup.gui.view.activity.settings

import com.storage.cloudbackup.logic.model.item.settings.Settings

data class SettingsActivityArgs(
    val settings: Settings,
    val usedData: ULong)
