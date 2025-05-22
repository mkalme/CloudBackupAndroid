package com.storage.cloudbackup.logic.model.io.reader

import com.storage.cloudbackup.logic.model.item.data.amount.DataAmount
import com.storage.cloudbackup.logic.model.item.settings.Settings
import com.storage.cloudbackup.logic.model.item.settings.language.container.LanguagePackContainer
import org.json.JSONObject

class SettingsReader(private val languagePackContainer: LanguagePackContainer) : Reader<Settings> {
    private var dataAmountReader: Reader<DataAmount> = DataAmountReader()

    override fun read(data: JSONObject): Settings {
        val output = Settings(languagePackContainer)
        output.editModeEnabled = data.getBoolean("EditModeEnabled")
        output.automaticUpdatingEnabled = data.getBoolean("AutomaticUpdatingEnabled")
        output.saveHistoryAttempts = data.getInt("SaveHistoryAttempts")
        output.monthlyMobileDataLimit = dataAmountReader.read(data.getJSONObject("MonthlyMobileDataLimit"))
        output.languagePack = data.getString("LanguagePack")

        return output
    }
}