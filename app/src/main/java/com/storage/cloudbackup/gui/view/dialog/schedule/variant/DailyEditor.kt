package com.storage.cloudbackup.gui.view.dialog.schedule.variant

import android.view.View
import com.storage.cloudbackup.logic.model.item.settings.language.LanguagePack
import com.storage.cloudbackup.logic.model.item.schedule.DailySchedule
import com.storage.cloudbackup.logic.model.item.schedule.Schedule

class DailyEditor(val schedule: DailySchedule) : ScheduleEditor {
    override fun setLanguage(languagePack: LanguagePack, editable: Boolean) {}
    override fun setInput() {}
    override fun setEditable(editable: Boolean) {}
    override fun save() {}

    override fun getView(): View? {
        return null
    }

    override fun getSchedule(): Schedule {
        return schedule
    }
}
