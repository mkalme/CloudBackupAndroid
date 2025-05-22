package com.storage.cloudbackup.logic.updater.standard.updater.uploader

import com.storage.cloudbackup.logic.cloud.provider.model.upload.LocalFile
import com.storage.cloudbackup.logic.model.item.scheme.Scheme

data class SchemeFileSourceOutput(val scheme: Scheme, val files: List<LocalFile>)
