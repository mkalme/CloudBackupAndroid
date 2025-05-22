package com.storage.cloudbackup.logic.updater

import com.storage.cloudbackup.logic.cloud.provider.model.upload.BytesSent
import com.storage.cloudbackup.logic.model.item.scheme.Scheme
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.ArgsEvent
import com.storage.cloudbackup.logic.shared.utilities.utilities.provider.GenericProvider
import com.storage.cloudbackup.logic.updater.event.args.FileCanceledArgs
import com.storage.cloudbackup.logic.updater.event.args.FileStartArgs
import com.storage.cloudbackup.logic.updater.event.args.FileStopArgs
import com.storage.cloudbackup.logic.updater.event.args.SchemeCanceledArgs
import com.storage.cloudbackup.logic.updater.event.args.SchemeStopArgs
import com.storage.cloudbackup.logic.updater.event.args.UpdateStartArgs
import com.storage.cloudbackup.logic.updater.event.args.UpdateStopArgs

interface Updater {
    val updateStartListener: MutableList<ArgsEvent<UpdateStartArgs>>
    val updateStopListener: MutableList<ArgsEvent<UpdateStopArgs>>

    val schemeFileSearchStartListener: MutableList<ArgsEvent<Scheme>>
    val schemeFileSearchCanceledListener: MutableList<ArgsEvent<SchemeCanceledArgs>>
    val schemeFileSearchStopListener: MutableList<ArgsEvent<SchemeStopArgs>>

    val schemeUpdateStartListener: MutableList<ArgsEvent<Scheme>>
    val schemeUpdateStopListener: MutableList<ArgsEvent<SchemeStopArgs>>

    val fileUpdateStartListener: MutableList<ArgsEvent<FileStartArgs>>
    val fileUpdateCanceledListener: MutableList<ArgsEvent<FileCanceledArgs>>
    val fileUpdateStopListener: MutableList<ArgsEvent<FileStopArgs>>

    val currentFileUploadedBytesProvider: GenericProvider<BytesSent>

    fun updateSchemes(schemes: List<Scheme>, userInitiated: Boolean)
}