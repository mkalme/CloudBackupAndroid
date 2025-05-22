package com.storage.cloudbackup.logic.updater.event.args

import com.storage.cloudbackup.logic.cloud.provider.model.upload.LocalFile
import com.storage.cloudbackup.logic.model.item.scheme.Scheme

data class FileCanceledArgs(val file: LocalFile, val index: Int, val max: Int, val scheme: Scheme, val messages: List<String> = listOf())
