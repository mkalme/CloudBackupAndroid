package com.storage.cloudbackup.logic.permit.update

import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.ArgsEvent
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent
import com.storage.cloudbackup.logic.updater.Updater
import com.storage.cloudbackup.logic.updater.event.args.UpdateStartArgs
import com.storage.cloudbackup.logic.updater.event.args.UpdateStopArgs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShallowUpdatePermit(val updater: Updater) : UpdatePermit, ArgsEvent<Boolean> {
    override val updateListener: MutableList<EmptyEvent> = mutableListOf()
    override val canUpdate: Boolean
        get() {
            return !isUpdating
        }

    private var isUpdating = false
    private var prevCanUpdate: Boolean? = null
    private val uiScope = CoroutineScope(Dispatchers.Main)

    init {
        updater.updateStartListener.add(object: ArgsEvent<UpdateStartArgs> {
            override fun onInvoke(args: UpdateStartArgs) {
                isUpdating = true
                invokeUpdateListener()
            }
        })

        updater.updateStopListener.add(object: ArgsEvent<UpdateStopArgs> {
            override fun onInvoke(args: UpdateStopArgs) {
                isUpdating = false
                invokeUpdateListener()
            }
        })
    }

    override fun onInvoke(args: Boolean) {
        checkUpdate()
    }

    private fun checkUpdate(){
        val canUp = canUpdate
        if(prevCanUpdate != null && canUp == prevCanUpdate) return

        prevCanUpdate = canUpdate
        invokeUpdateListener()
    }

    private fun invokeUpdateListener(){
        uiScope.launch {
            updateListener.forEach {
                it.onInvoke()
            }
        }
    }
}