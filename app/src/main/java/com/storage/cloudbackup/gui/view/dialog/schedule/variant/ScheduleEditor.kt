package com.storage.cloudbackup.gui.view.dialog.schedule.variant

import android.view.View
import com.storage.cloudbackup.logic.model.item.schedule.Schedule
import com.storage.cloudbackup.logic.model.item.settings.language.LanguagePack

interface ScheduleEditor {
    fun setInput()
    fun setLanguage(languagePack: LanguagePack, editable: Boolean)
    fun setEditable(editable: Boolean)
    fun save()

    fun getView(): View?
    fun getSchedule(): Schedule
}
