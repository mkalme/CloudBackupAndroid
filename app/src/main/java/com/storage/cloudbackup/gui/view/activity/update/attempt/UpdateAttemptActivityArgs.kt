package com.storage.cloudbackup.gui.view.activity.update.attempt

import com.storage.cloudbackup.logic.permit.language.LanguagePermit
import com.storage.cloudbackup.logic.model.item.history.attempt.UpdateAttempt

data class UpdateAttemptActivityArgs(
    val updateAttempt: UpdateAttempt,
    val languagePermit: LanguagePermit
)