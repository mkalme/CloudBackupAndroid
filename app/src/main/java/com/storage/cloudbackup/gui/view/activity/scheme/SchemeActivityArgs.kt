package com.storage.cloudbackup.gui.view.activity.scheme

import com.storage.cloudbackup.logic.permit.edit.EditPermit
import com.storage.cloudbackup.logic.permit.language.LanguagePermit
import com.storage.cloudbackup.logic.cloud.provider.model.CloudProvider
import com.storage.cloudbackup.logic.model.item.scheme.Scheme
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.ArgsEvent

data class SchemeActivityArgs(
    val scheme: Scheme,
    val cloudProviders: List<CloudProvider>,
    val editPermit: EditPermit,
    val languagePermit: LanguagePermit,
    val removeEvent: ArgsEvent<Scheme>)
