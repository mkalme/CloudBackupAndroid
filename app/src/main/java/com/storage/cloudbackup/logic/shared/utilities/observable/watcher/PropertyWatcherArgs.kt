package com.storage.cloudbackup.logic.shared.utilities.observable.watcher

import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent

data class PropertyWatcherArgs<T>(val listener: MutableList<EmptyEvent>, val valueProvider: () -> T, val propertyChangeEvent: () -> Any)
