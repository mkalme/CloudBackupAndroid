package com.storage.cloudbackup.logic.shared.utilities.observable.events

interface CollectionChangedEvent<T> {
    fun onInvoke(removed: Collection<T?>, added: Collection<T?>)
}