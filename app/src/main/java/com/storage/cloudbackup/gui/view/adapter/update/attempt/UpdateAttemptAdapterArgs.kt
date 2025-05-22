package com.storage.cloudbackup.gui.view.adapter.update.attempt

import android.content.Context
import com.storage.cloudbackup.logic.permit.language.LanguagePermit
import com.storage.cloudbackup.logic.cloud.provider.model.CloudProvider
import com.storage.cloudbackup.logic.model.item.history.attempt.UpdateAttempt
import com.storage.cloudbackup.logic.model.item.scheme.Scheme
import com.storage.cloudbackup.logic.shared.utilities.observable.collections.ObservableMutableList
import com.storage.cloudbackup.logic.shared.utilities.utilities.provider.GenericArgsProvider
import java.util.UUID

data class UpdateAttemptAdapterArgs(
    val updateAttempts: ObservableMutableList<UpdateAttempt>,
    val cloudProviders: List<CloudProvider>,
    val schemeProvider: GenericArgsProvider<UUID, Scheme?>,
    val languagePermit: LanguagePermit,
    val context: Context)
