package com.storage.cloudbackup.logic.permit.edit

import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent

interface EditPermit {
    val editListener: MutableList<EmptyEvent>
    val canEdit: Boolean
}