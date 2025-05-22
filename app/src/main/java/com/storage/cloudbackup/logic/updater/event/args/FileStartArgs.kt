package com.storage.cloudbackup.logic.updater.event.args

import com.storage.cloudbackup.logic.cloud.provider.model.upload.LocalFile
import com.storage.cloudbackup.logic.model.item.scheme.Scheme

data class FileStartArgs(val file: LocalFile, val index: Int, val max: Int, val scheme: Scheme)
