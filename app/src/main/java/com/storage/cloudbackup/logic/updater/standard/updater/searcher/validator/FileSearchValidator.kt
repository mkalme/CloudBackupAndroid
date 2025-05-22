package com.storage.cloudbackup.logic.updater.standard.updater.searcher.validator

import com.storage.cloudbackup.logic.model.item.scheme.Scheme
import com.storage.cloudbackup.logic.updater.standard.updater.Output

interface FileSearchValidator {
    fun validate(scheme: Scheme) : Output<Any?>
}