package com.storage.cloudbackup.gui.view.layout.cloud.provider.selector

import android.content.Context
import com.storage.cloudbackup.logic.permit.edit.EditPermit
import com.storage.cloudbackup.logic.cloud.provider.model.CloudProvider
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.ArgsEvent

data class SelectorLayoutArgs(
    val cloudProviders: List<CloudProvider>,
    val editPermit: EditPermit,
    val selectionEvent: ArgsEvent<CloudProvider?>,
    val context: Context)
