package com.storage.cloudbackup.gui.view.dialog.filter

import com.storage.cloudbackup.logic.permit.edit.EditPermit
import com.storage.cloudbackup.logic.permit.language.LanguagePermit
import com.storage.cloudbackup.logic.model.item.scheme.FileSearcher
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent

data class FilterDialogArgs(
    val filter: FileSearcher,
    val editPermit: EditPermit,
    val languagePermit: LanguagePermit,
    val closeEvent: EmptyEvent)
