package com.storage.cloudbackup.gui.view.dialog.prompt

import android.view.Gravity
import com.storage.cloudbackup.logic.permit.edit.EditPermit
import com.storage.cloudbackup.logic.permit.language.LanguagePermit
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.ArgsEvent

data class PromptDialogArgs(
    val editPermit: EditPermit,
    val languagePermit: LanguagePermit,
    val message: String,
    val gravity: Int = Gravity.START,
    val clickEvent: ArgsEvent<Boolean>? = null)
