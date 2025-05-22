package com.storage.cloudbackup.logic.shared.utilities.observable.resource

import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent

abstract class ObservableObject {
    val propertyChangedListener: MutableList<EmptyEvent> = mutableListOf()

    private var inUpdateMode = false
    private var updateNeedsToBeTriggered = false

    protected fun invokeListener(){
        if(inUpdateMode) {
            updateNeedsToBeTriggered = true
            return
        }

        propertyChangedListener.forEach{
            it.onInvoke()
        }
    }

    fun invokeWithSingleEventTrigger(ignoredFunc: () -> Unit) {
        inUpdateMode = true
        ignoredFunc()
        inUpdateMode = false

        if(updateNeedsToBeTriggered) {
            updateNeedsToBeTriggered = false
            invokeListener()
        }
    }
}