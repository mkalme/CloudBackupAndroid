package com.storage.cloudbackup.gui.view.adapter.updateplan

import android.content.Context
import com.storage.cloudbackup.logic.cloud.provider.model.CloudProvider
import com.storage.cloudbackup.logic.model.item.scheme.Scheme
import com.storage.cloudbackup.logic.model.item.updateplan.UpdatePlan
import com.storage.cloudbackup.logic.permit.edit.EditPermit
import com.storage.cloudbackup.logic.permit.language.LanguagePermit
import com.storage.cloudbackup.logic.permit.update.UpdatePermit
import com.storage.cloudbackup.logic.shared.utilities.observable.collections.ObservableMutableList
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.ArgsEvent

data class UpdatePlanAdapterArgs(
    val updatePlans: ObservableMutableList<UpdatePlan>,
    val cloudProviders: List<CloudProvider>,
    val updateEvent: ArgsEvent<List<Scheme>>,
    val shallowUpdatePermit: UpdatePermit,
    val editPermit: EditPermit,
    val languagePermit: LanguagePermit,
    val context: Context)
