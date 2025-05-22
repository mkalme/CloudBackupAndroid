package com.storage.cloudbackup.logic.permit.update

import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent

interface UpdatePermit {
    val updateListener: MutableList<EmptyEvent>
    val canUpdate: Boolean
}