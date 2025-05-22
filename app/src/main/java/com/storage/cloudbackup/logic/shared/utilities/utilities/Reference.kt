package com.storage.cloudbackup.logic.shared.utilities.utilities

import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent

class Reference<T>(private var reference: T) {
    fun getReference(): T {
        return reference
    }

    fun setReference(reference: T) {
        if (this.reference === reference) return
        this.reference = reference
        if (referenceChangedListener != null) referenceChangedListener?.onInvoke()
    }

    private var referenceChangedListener: EmptyEvent? = null
    fun setContentsChangedListener(referenceChangedListener: EmptyEvent?) {
        this.referenceChangedListener = referenceChangedListener
    }
}
