package com.storage.cloudbackup.logic.permit.update

import com.storage.cloudbackup.logic.cloud.provider.model.CloudProvider
import com.storage.cloudbackup.logic.model.item.updateplan.UpdatePlan
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.ArgsEvent
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StandardUpdatePermit(private val shallowPermit: UpdatePermit, private val updatePlan: UpdatePlan) : UpdatePermit, ArgsEvent<Boolean> {
    override val updateListener: MutableList<EmptyEvent> = mutableListOf()
    override val canUpdate: Boolean
        get() {
            return shallowPermit.canUpdate && updatePlan.cloudProvider != null && updatePlan.cloudProvider!!.logicComponent.isOpen && updatePlan.folder.isNotEmpty() && updatePlan.schemes.size > 0
        }

    private var prevCanUpdate: Boolean? = null
    private var prevCloudProvider: CloudProvider? = null

    private val uiScope = CoroutineScope(Dispatchers.Main)

    init {
        shallowPermit.updateListener.add(object: EmptyEvent {
            override fun onInvoke() {
                checkUpdate()
            }
        })

        updatePlan.propertyChangedListener.add(object: EmptyEvent {
            override fun onInvoke() {
                if(prevCloudProvider != updatePlan.cloudProvider){
                    removeIsOpenListener(prevCloudProvider)
                    setIsOpenListener(updatePlan.cloudProvider)

                    prevCloudProvider = updatePlan.cloudProvider
                }

                checkUpdate()
            }
        })

        prevCloudProvider = updatePlan.cloudProvider
        setIsOpenListener(updatePlan.cloudProvider)
    }

    private fun setIsOpenListener(cloudProvider: CloudProvider?){
        if(cloudProvider == null) return
        cloudProvider.logicComponent.isOpenListener.add(this)
    }

    private fun removeIsOpenListener(cloudProvider: CloudProvider?){
        if(cloudProvider == null) return
        cloudProvider.logicComponent.isOpenListener.remove(this)
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