package com.storage.cloudbackup.logic.model.item.schedule

data class TimeOfDay(val hour: Byte, val minute: Byte) {
    fun clone() : TimeOfDay {
        return TimeOfDay(hour, minute)
    }
}
