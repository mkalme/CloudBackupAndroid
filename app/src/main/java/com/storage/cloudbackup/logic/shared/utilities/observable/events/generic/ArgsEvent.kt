package com.storage.cloudbackup.logic.shared.utilities.observable.events.generic

interface ArgsEvent<T> {
    fun onInvoke(args: T)
}