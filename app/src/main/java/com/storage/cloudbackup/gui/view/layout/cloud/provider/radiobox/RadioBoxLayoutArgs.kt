package com.storage.cloudbackup.gui.view.layout.cloud.provider.radiobox

import android.content.Context
import com.storage.cloudbackup.logic.permit.edit.EditPermit
import com.storage.cloudbackup.logic.cloud.provider.model.CloudProvider
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.ArgsEvent

data class RadioBoxLayoutArgs(
    val cloudProvider: CloudProvider,
    val editPermit: EditPermit,
    val selectedListener: ArgsEvent<RadioBoxEventArgs>,
    val context: Context)
