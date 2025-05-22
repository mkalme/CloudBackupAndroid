package com.storage.cloudbackup.logic.updater.standard.updater

data class Output<T>(val output: T, val successful: Boolean, val messages: List<String> = listOf())
