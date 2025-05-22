package com.storage.cloudbackup.logic.cloud.provider.providers.demo.gui.view

import android.view.View
import com.storage.cloudbackup.logic.cloud.provider.model.CloudProviderSettings
import com.storage.cloudbackup.logic.model.item.settings.language.LanguagePack

class ViewSettings(private val layoutArgs: SettingsLayoutArgs) : CloudProviderSettings {
    private var view: SettingsLayout? = null

    override fun getSettingsView(): View? {
        view = SettingsLayout(layoutArgs)
        return view
    }

    override fun setSave() {
        view?.setSave()
    }

    override fun setLanguage(languagePack: LanguagePack, editable: Boolean) {
        view?.setLanguage(languagePack, editable)
    }

    override fun setEditable(editable: Boolean) {
        view?.setEditable(editable)
    }
}