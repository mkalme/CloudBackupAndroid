package com.storage.cloudbackup.logic.updater.history

import com.storage.cloudbackup.logic.model.item.history.SchemeHistory
import com.storage.cloudbackup.logic.model.item.history.attempt.AttemptResult
import com.storage.cloudbackup.logic.model.item.history.attempt.UpdateAttempt
import com.storage.cloudbackup.logic.model.item.scheme.Scheme
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.ArgsEvent
import com.storage.cloudbackup.logic.updater.Updater
import com.storage.cloudbackup.logic.updater.event.args.FileCanceledArgs
import com.storage.cloudbackup.logic.updater.event.args.FileStartArgs
import com.storage.cloudbackup.logic.updater.event.args.FileStopArgs
import com.storage.cloudbackup.logic.updater.event.args.SchemeCanceledArgs
import com.storage.cloudbackup.logic.updater.event.args.SchemeStopArgs
import com.storage.cloudbackup.logic.updater.event.args.UpdateStartArgs
import com.storage.cloudbackup.logic.updater.event.args.UpdateStopArgs
import java.time.LocalDateTime

class UpdateHistory(updater: Updater, maxHistoryUpdateAttempt: Int) {
    private var filesUploaded = mutableListOf<String>()
    private var unsuccessfulFilesUploaded = mutableListOf<String>()
    private var userInitiated: Boolean = false

    init {
        updater.updateStartListener.add(object: ArgsEvent<UpdateStartArgs> {
            override fun onInvoke(args: UpdateStartArgs) {
                userInitiated = args.userInitiated
            }
        })
        updater.updateStopListener.add(object: ArgsEvent<UpdateStopArgs> {
            override fun onInvoke(args: UpdateStopArgs) {

            }
        })

        updater.schemeFileSearchStartListener.add(object: ArgsEvent<Scheme> {
            override fun onInvoke(args: Scheme) {

            }
        })
        updater.schemeFileSearchCanceledListener.add(object: ArgsEvent<SchemeCanceledArgs> {
            override fun onInvoke(args: SchemeCanceledArgs) {
                val updateAttempt = createUpdateAttempt(args.scheme)
                updateAttempt.result = AttemptResult.Unsuccessful
                updateAttempt.schemeErrorMessages.addAll(args.messages)

                addAttempt(updateAttempt, maxHistoryUpdateAttempt, args.scheme.history)
            }
        })
        updater.schemeFileSearchStopListener.add(object: ArgsEvent<SchemeStopArgs> {
            override fun onInvoke(args: SchemeStopArgs) {
                if(!args.successful) {
                    val updateAttempt = createUpdateAttempt(args.scheme)
                    updateAttempt.result = AttemptResult.Unsuccessful
                    updateAttempt.schemeErrorMessages.addAll(args.messages)

                    addAttempt(updateAttempt, maxHistoryUpdateAttempt, args.scheme.history)
                }
            }
        })

        updater.schemeUpdateStartListener.add(object: ArgsEvent<Scheme> {
            override fun onInvoke(args: Scheme) {
                filesUploaded.clear()
                unsuccessfulFilesUploaded.clear()

                addAttempt(createUpdateAttempt(args), maxHistoryUpdateAttempt, args.history)
            }
        })

        updater.schemeUpdateStopListener.add(object: ArgsEvent<SchemeStopArgs> {
            override fun onInvoke(args: SchemeStopArgs) {
                val updateAttempt = args.scheme.history.updateAttempts.last()

                args.scheme.history.invokeWithSingleEventTrigger {
                    updateAttempt.finished = LocalDateTime.now()

                    updateAttempt.filesUploaded.addAll(filesUploaded)
                    updateAttempt.unsuccessfulFilesUploaded.addAll(unsuccessfulFilesUploaded)

                    if(args.successful) {
                        updateAttempt.result = AttemptResult.Successful
                        args.scheme.history.lastUpdated = LocalDateTime.now()
                    }
                    else {
                        updateAttempt.result = AttemptResult.Unsuccessful
                        updateAttempt.schemeErrorMessages.addAll(args.messages)
                    }
                }
            }
        })

        updater.fileUpdateStartListener.add(object: ArgsEvent<FileStartArgs> {
            override fun onInvoke(args: FileStartArgs) {

            }
        })
        updater.fileUpdateCanceledListener.add(object: ArgsEvent<FileCanceledArgs> {
            override fun onInvoke(args: FileCanceledArgs) {
                val relativePath = createRelativePath(args.scheme, args.file.fullPath)

                unsuccessfulFilesUploaded.add(relativePath)
            }
        })
        updater.fileUpdateStopListener.add(object: ArgsEvent<FileStopArgs> {
            override fun onInvoke(args: FileStopArgs) {
                val relativePath = createRelativePath(args.scheme, args.file.fullPath)

                if(args.successful){
                    filesUploaded.add(relativePath)
                }else {
                    unsuccessfulFilesUploaded.add(relativePath)
                }
            }
        })
    }

    private fun addAttempt(attempt: UpdateAttempt, max: Int, history: SchemeHistory){
        if(history.updateAttempts.size >= max) {
            for(i in 0 .. history.updateAttempts.size - max){
                history.updateAttempts.removeAt(0)
            }
        }

        history.updateAttempts.add(attempt)
    }

    private fun createUpdateAttempt(scheme: Scheme): UpdateAttempt {
        val updateAttempt = UpdateAttempt()
        updateAttempt.begin = LocalDateTime.now()
        updateAttempt.localDirectory = scheme.owner?.folder ?: ""
        updateAttempt.driveDirectory = scheme.driveFolder
        updateAttempt.cloudProviderId = scheme.owner?.cloudProvider?.logicComponent?.info?.id ?: ""
        updateAttempt.isAutomatic = !userInitiated

        return updateAttempt
    }

    private fun createRelativePath(scheme: Scheme, fullPath: String): String {
        val root = scheme.owner?.folder!!
        return fullPath.substring(root.length + 1)
    }
}