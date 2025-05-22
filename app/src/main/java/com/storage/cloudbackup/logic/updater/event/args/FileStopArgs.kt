package com.storage.cloudbackup.logic.updater.event.args

import com.storage.cloudbackup.logic.cloud.provider.model.upload.LocalFile
import com.storage.cloudbackup.logic.model.item.scheme.Scheme

class FileStopArgs(val file: LocalFile, val index: Int, val max: Int, val scheme: Scheme, val successful: Boolean, val messages: List<String> = listOf())