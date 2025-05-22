package com.storage.cloudbackup.logic.permit.edit

import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent

class EmptyEditPermit(private val isEditable: Boolean) : EditPermit {
    override val editListener: MutableList<EmptyEvent>
        get() = mutableListOf()
    override val canEdit: Boolean
        get() = isEditable
}