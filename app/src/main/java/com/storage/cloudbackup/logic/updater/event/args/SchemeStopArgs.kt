package com.storage.cloudbackup.logic.updater.event.args

import com.storage.cloudbackup.logic.model.item.scheme.Scheme

data class SchemeStopArgs(val scheme: Scheme, val successful: Boolean, val messages: List<String> = listOf())