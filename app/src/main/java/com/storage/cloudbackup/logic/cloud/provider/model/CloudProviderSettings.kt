package com.storage.cloudbackup.logic.cloud.provider.model

import android.view.View
import com.storage.cloudbackup.logic.model.item.settings.language.LanguagePack

interface CloudProviderSettings {
    fun getSettingsView(): View?
    fun setSave()

    fun setLanguage(languagePack: LanguagePack, editable: Boolean)
    fun setEditable(editable: Boolean)
}