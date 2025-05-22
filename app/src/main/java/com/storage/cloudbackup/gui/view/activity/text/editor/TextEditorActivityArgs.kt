package com.storage.cloudbackup.gui.view.activity.text.editor

import com.storage.cloudbackup.logic.permit.edit.EditPermit
import com.storage.cloudbackup.logic.permit.language.LanguagePermit
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.ArgsEvent

data class TextEditorActivityArgs(
    val text: String,
    val title: String,
    val editPermit: EditPermit,
    val languagePermit: LanguagePermit,
    val saveEvent: ArgsEvent<String>)
