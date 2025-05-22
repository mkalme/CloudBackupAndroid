package com.storage.cloudbackup.logic.shared.utilities.observable.watcher

import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent

class PropertyWatcher<T>(private val watcherArgs: PropertyWatcherArgs<T>) {
    private var lastValue = watcherArgs.valueProvider()

    init {
        watcherArgs.listener.add(object: EmptyEvent {
            override fun onInvoke() {
                update()
            }
        })
    }

    private fun update(){
        val currentValue = watcherArgs.valueProvider()
        if(currentValue == lastValue) return

        lastValue = currentValue
        watcherArgs.propertyChangeEvent()
    }
}