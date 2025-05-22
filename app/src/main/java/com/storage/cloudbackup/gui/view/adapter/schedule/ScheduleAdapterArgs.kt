package com.storage.cloudbackup.gui.view.adapter.schedule

import android.content.Context
import com.storage.cloudbackup.logic.permit.edit.EditPermit
import com.storage.cloudbackup.logic.permit.language.LanguagePermit
import com.storage.cloudbackup.logic.model.item.schedule.Schedule
import com.storage.cloudbackup.logic.shared.utilities.utilities.Reference

data class ScheduleAdapterArgs(
    val scheduleReferences: MutableList<Reference<Schedule>>,
    val editPermit: EditPermit,
    val languagePermit: LanguagePermit,
    val context: Context)
