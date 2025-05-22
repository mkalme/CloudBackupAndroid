package com.storage.cloudbackup.logic.updater.event.args

data class UpdateStopArgs(val successful: Boolean, val messages: List<String> = listOf())
