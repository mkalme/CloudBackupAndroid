package com.storage.cloudbackup.logic.cloud.provider.providers.s3.gui.view

import android.content.Context
import com.storage.cloudbackup.logic.permit.edit.EditPermit
import com.storage.cloudbackup.logic.permit.language.LanguagePermit
import com.storage.cloudbackup.logic.cloud.provider.providers.s3.logic.settings.Settings

data class SettingsLayoutArgs (
    val settings: Settings,
    val editPermit: EditPermit,
    val languagePermit: LanguagePermit,
    val context: Context
)
