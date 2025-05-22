package com.storage.cloudbackup.logic.model.io.writer

import com.storage.cloudbackup.logic.model.item.data.amount.DataAmount
import com.storage.cloudbackup.logic.model.item.settings.Settings
import org.json.JSONObject

class SettingsWriter : Writer<Settings> {
    private var dataAmountWriter: Writer<DataAmount> = DataAmountWriter()

    override fun write(input: Settings): JSONObject {
        val output = JSONObject()
        output.put("EditModeEnabled", input.editModeEnabled)
        output.put("AutomaticUpdatingEnabled", input.automaticUpdatingEnabled)
        output.put("SaveHistoryAttempts", input.saveHistoryAttempts)
        output.put("MonthlyMobileDataLimit", dataAmountWriter.write(input.monthlyMobileDataLimit))
        output.put("LanguagePack", input.languagePack)

        return output
    }
}