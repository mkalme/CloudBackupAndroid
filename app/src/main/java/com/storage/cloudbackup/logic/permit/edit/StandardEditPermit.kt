package com.storage.cloudbackup.logic.permit.edit

import com.storage.cloudbackup.logic.model.item.settings.Settings
import com.storage.cloudbackup.logic.permit.update.UpdatePermit
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StandardEditPermit(private val updatePermit: UpdatePermit, val settings: Settings) :
    EditPermit {
    override val editListener: MutableList<EmptyEvent> = mutableListOf()
    override val canEdit: Boolean
        get() = updatePermit.canUpdate && settings.editModeEnabled

    private var prevCanEdit = canEdit

    private val uiScope = CoroutineScope(Dispatchers.Main)

    init {
        settings.propertyChangedListener.add(object: EmptyEvent {
            override fun onInvoke() {
                invokeListener()
            }
        })

        updatePermit.updateListener.add(object: EmptyEvent {
            override fun onInvoke() {
                invokeListener()
            }
        })
    }

    private fun invokeListener(){
        if(prevCanEdit == canEdit) return
        prevCanEdit = canEdit

        uiScope.launch {
            editListener.forEach {
                it.onInvoke()
            }
        }
    }
}