package com.storage.cloudbackup.logic.updater.event.args

import com.storage.cloudbackup.logic.model.item.scheme.Scheme

data class SchemeCanceledArgs(val scheme: Scheme, val messages: List<String> = listOf())