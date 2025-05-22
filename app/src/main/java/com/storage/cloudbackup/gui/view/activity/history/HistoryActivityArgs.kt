package com.storage.cloudbackup.gui.view.activity.history

import com.storage.cloudbackup.logic.permit.edit.EditPermit
import com.storage.cloudbackup.logic.permit.language.LanguagePermit
import com.storage.cloudbackup.logic.cloud.provider.model.CloudProvider
import com.storage.cloudbackup.logic.model.item.history.attempt.UpdateAttempt
import com.storage.cloudbackup.logic.model.item.scheme.Scheme
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent
import com.storage.cloudbackup.logic.shared.utilities.utilities.provider.GenericArgsProvider
import java.util.UUID

data class HistoryActivityArgs(
    val updateAttempts: List<UpdateAttempt>,
    val cloudProviders: List<CloudProvider>,
    val schemeProvider: GenericArgsProvider<UUID, Scheme?>,
    val editPermit: EditPermit,
    val languagePermit: LanguagePermit,
    val clearEvent: EmptyEvent)