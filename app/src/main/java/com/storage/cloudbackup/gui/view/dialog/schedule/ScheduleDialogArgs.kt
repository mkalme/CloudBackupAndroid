package com.storage.cloudbackup.gui.view.dialog.schedule

import com.storage.cloudbackup.logic.permit.edit.EditPermit
import com.storage.cloudbackup.logic.permit.language.LanguagePermit
import com.storage.cloudbackup.logic.model.item.schedule.Schedule
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent
import com.storage.cloudbackup.logic.shared.utilities.utilities.Reference

data class ScheduleDialogArgs(
    val schedule: Reference<Schedule>,
    val editPermit: EditPermit,
    val languagePermit: LanguagePermit,
    val closeEvent: EmptyEvent)
