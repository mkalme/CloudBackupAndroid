package com.storage.cloudbackup.gui.notification

import com.storage.cloudbackup.R
import com.storage.cloudbackup.gui.notification.builder.NotificationBuilder
import com.storage.cloudbackup.gui.notification.queue.RunnableQueue
import com.storage.cloudbackup.gui.notification.queue.StandardRunnableQueue
import com.storage.cloudbackup.logic.model.item.scheme.Scheme
import com.storage.cloudbackup.logic.permit.language.LanguagePermit
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.ArgsEvent
import com.storage.cloudbackup.logic.updater.Updater
import com.storage.cloudbackup.logic.updater.event.args.FileCanceledArgs
import com.storage.cloudbackup.logic.updater.event.args.FileStartArgs
import com.storage.cloudbackup.logic.updater.event.args.FileStopArgs
import com.storage.cloudbackup.logic.updater.event.args.UpdateStartArgs
import com.storage.cloudbackup.logic.updater.event.args.UpdateStopArgs
import java.util.UUID

class StandardNotification(private val updater: Updater, private val builder: NotificationBuilder, private val languagePermit: LanguagePermit) {
    @Volatile private var updating = false
    private var filesUploaded = 0
    private var lastPercentage = ""
    private var currentSecondIcon = 0
    private var index = 0
    private var total = 0
    private var fileUpdateUnderway = false

    private val queue: RunnableQueue = StandardRunnableQueue()
    private var currentUuid: UUID = UUID.randomUUID()

    init {
        updater.updateStartListener.add(object: ArgsEvent<UpdateStartArgs> {
            override fun onInvoke(args: UpdateStartArgs) {
                updateStart()
            }
        })
        updater.updateStopListener.add(object: ArgsEvent<UpdateStopArgs> {
            override fun onInvoke(args: UpdateStopArgs) {
                updateStop(args)
            }
        })

        updater.schemeUpdateStartListener.add(object: ArgsEvent<Scheme> {
            override fun onInvoke(args: Scheme) {
                schemeUpdateStart(args)
            }
        })

        updater.fileUpdateStartListener.add(object: ArgsEvent<FileStartArgs> {
            override fun onInvoke(args: FileStartArgs) {
                fileUpdateStart(args)
            }
        })
        updater.fileUpdateCanceledListener.add(object: ArgsEvent<FileCanceledArgs> {
            override fun onInvoke(args: FileCanceledArgs) {
                fileUpdateCanceled(args)
            }
        })
        updater.fileUpdateStopListener.add(object: ArgsEvent<FileStopArgs> {
            override fun onInvoke(args: FileStopArgs) {
                fileUpdateStop(args)
            }
        })
    }

    private fun updateStart(){
        filesUploaded = 0
        index = 0
        total = 0
        updating = true
        fileUpdateUnderway = false
        currentUuid = UUID.randomUUID()

        queue.open()

        val title = languagePermit.currentLanguage.getTranslation("notification.updateBegin.title")
        val message = languagePermit.currentLanguage.getTranslation("notification.updateBegin.message")
        queue.runNext {
            builder.showWithoutProgressBar(title, message, R.mipmap.app_icon)
        }

        Thread {
            while(updating){
                if(!fileUpdateUnderway) continue

                val percentage = getPercentageText()
                if(lastPercentage == percentage) continue

                var totalPercentage = index.toDouble() / total.toDouble()
                if(total > 0) totalPercentage += getFilePercentage() / total.toDouble()

                queue.runNext {
                    builder.showSecondMessage(percentage, totalPercentage.toFloat())
                }
                lastPercentage = percentage

                Thread.sleep(250)
            }
        }.start()
    }
    private fun updateStop(args: UpdateStopArgs){
        updating = false

        val func: () -> Unit = if(args.successful) {
            val message = if(filesUploaded > 0){
                "${languagePermit.currentLanguage.getTranslation("notification.updateStop.successful.moreThanZero.message")}: $filesUploaded"
            }else{
                languagePermit.currentLanguage.getTranslation("notification.updateStop.successful.zero")
            }

            val title = languagePermit.currentLanguage.getTranslation("notification.updateStop.successful.title")
            ({ builder.showWithoutProgressBar(title, message, R.mipmap.app_icon) })
        }else{
            val title = languagePermit.currentLanguage.getTranslation("notification.updateStop.unsuccessful.title")
            ({ builder.showWithoutProgressBar(title, args.messages.first(), R.mipmap.app_icon) })
        }

        val id = currentUuid
        queue.runNext {
            func()
            Thread.sleep(3000)
            if(!updating && id == currentUuid) {
                builder.close()
                queue.close()
            }
        }
    }

    private fun schemeUpdateStart(args: Scheme){
        currentSecondIcon = args.owner?.cloudProvider?.logicComponent?.info?.icon!!
    }

    private fun fileUpdateStart(args: FileStartArgs){
        queue.runNext {
            index = args.index
            total = args.max
            fileUpdateUnderway = true

            val progressText = "(${args.index + 1}/${args.max})"

            builder.show(
                args.index / args.max.toFloat(), "${languagePermit.currentLanguage.getTranslation("notification.fileBeginUpdate.title")} $progressText",
                args.file.name,
                getPercentageText(),
                android.R.drawable.stat_sys_upload,
                currentSecondIcon
            )
        }
    }
    private fun fileUpdateCanceled(args: FileCanceledArgs){
        val title = languagePermit.currentLanguage.getTranslation("notification.fileUpdateCanceled.title")
        val secondMessage = "100% - ${languagePermit.currentLanguage.getTranslation("notification.fileUpdateCanceled.secondMessage")}"

        queue.runNext {
            builder.show(
                args.index / args.max.toFloat(), title,
                args.file.name,
                secondMessage,
                android.R.drawable.stat_sys_upload,
                currentSecondIcon
            )
        }
    }
    private fun fileUpdateStop(args: FileStopArgs){
        if(args.successful) filesUploaded++

        fileUpdateUnderway = false
        val progressText = "(${args.index + 1}/${args.max})"

        if(args.successful){
            val title = languagePermit.currentLanguage.getTranslation("notification.fileUpdateStop.successful.title")
            val secondMessage = "100% - ${languagePermit.currentLanguage.getTranslation("notification.fileUpdateStop.successful.secondMessage")}"

            queue.runNext {
                builder.show(
                    (args.index + 1) / args.max.toFloat(), "$title $progressText",
                    args.file.name,
                    secondMessage,
                    android.R.drawable.stat_sys_upload,
                    currentSecondIcon
                )
            }
        }else{
            val title = languagePermit.currentLanguage.getTranslation("notification.fileUpdateStop.unsuccessful.title")

            queue.runNext {
                builder.show(
                    args.index / args.max.toFloat(), "$title $progressText",
                    args.file.name,
                    args.messages.first(),
                    android.R.drawable.stat_sys_upload,
                    currentSecondIcon
                )
            }
        }
    }

    private fun getFilePercentage(): Float {
        val max = updater.currentFileUploadedBytesProvider.provide().total
        val min = updater.currentFileUploadedBytesProvider.provide().sent
        return min.toFloat() / max.toFloat()
    }

    private fun getPercentageText(): String {
        return "${(getFilePercentage() * 100).toInt()}%"
    }
}