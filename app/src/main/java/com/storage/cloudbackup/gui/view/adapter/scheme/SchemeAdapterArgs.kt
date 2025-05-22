package com.storage.cloudbackup.gui.view.adapter.scheme

import android.content.Context
import com.storage.cloudbackup.logic.cloud.provider.model.CloudProvider
import com.storage.cloudbackup.logic.model.item.scheme.Scheme
import com.storage.cloudbackup.logic.permit.edit.EditPermit
import com.storage.cloudbackup.logic.permit.language.LanguagePermit
import com.storage.cloudbackup.logic.permit.update.UpdatePermit
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.ArgsEvent

data class SchemeAdapterArgs(
    val schemes: MutableList<Scheme>,
    val updateEvent: ArgsEvent<List<Scheme>>,
    val cloudProviders: List<CloudProvider>,
    val updatePermit: UpdatePermit,
    val editPermit: EditPermit,
    val languagePermit: LanguagePermit,
    val context: Context)
