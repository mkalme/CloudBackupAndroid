package com.storage.cloudbackup.gui.notification.builder

import android.graphics.Color
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.storage.cloudbackup.R
import com.storage.cloudbackup.gui.service.notification.StandardNotificationBuilderArgs

class StandardNotificationBuilder(private val args: StandardNotificationBuilderArgs) : NotificationBuilder {
    private var builder: NotificationCompat.Builder? = null
    private var lastTitle: String = ""
    private var lastMessage: String? = null
    private var lastProgress: Float = 0f
    private val color = Color.BLACK

    private lateinit var largeView: RemoteViews
    private lateinit var smallView: RemoteViews

    override fun showWithoutProgressBar(title: String, message: String?, icon: Int) {
        lastTitle = title
        lastMessage = message

        builder = NotificationCompat.Builder(args.context, args.channelId)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(message)
            .setOngoing(true)
            .setColor(color)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)

        args.notificationManager.notify(args.notificationId, builder?.build())
    }

    override fun show(progress: Float, title: String, message: String?, secondMessage: String?, icon: Int, secondIcon: Int) {
        lastTitle = title
        lastMessage = message
        lastProgress = progress

        largeView = RemoteViews(args.context.packageName, R.layout.layout_notification_progress)
        smallView = RemoteViews(args.context.packageName, R.layout.layout_notification)

        largeView.setTextViewText(R.id.layout_notification_progress_title, title)
        largeView.setTextViewText(R.id.layout_notification_progress_message, message)
        largeView.setTextViewText(R.id.layout_notification_progress_second_message, secondMessage)
        largeView.setProgressBar(R.id.layout_notification_progressBar, 1000000, (progress * 1000000).toInt(), false)
        if(secondIcon != 0) largeView.setImageViewResource(R.id.layout_notification_progress_second_icon, secondIcon)

        smallView.setTextViewText(R.id.layout_notification_title, title)
        smallView.setTextViewText(R.id.layout_notification_message, message)

        builder = NotificationCompat.Builder(args.context, args.channelId)
            .setCustomBigContentView(largeView)
            .setCustomContentView(smallView)
            .setSmallIcon(icon)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setColor(color)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)

        args.notificationManager.notify(args.notificationId, builder?.build())
    }

    override fun showSecondMessage(secondMessage: String?, progress: Float) {
        lastProgress = progress

        largeView.setTextViewText(R.id.layout_notification_progress_title, lastTitle)
        largeView.setTextViewText(R.id.layout_notification_progress_message, lastMessage)
        largeView.setTextViewText(R.id.layout_notification_progress_second_message, secondMessage)
        largeView.setProgressBar(R.id.layout_notification_progressBar, 1000000, (progress * 1000000).toInt(), false)

        args.notificationManager.notify(args.notificationId, builder?.build())
    }

    override fun close() {
        args.notificationManager.cancel(args.notificationId)
    }
}