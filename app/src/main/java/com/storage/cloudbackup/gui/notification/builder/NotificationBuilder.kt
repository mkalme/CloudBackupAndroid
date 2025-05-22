package com.storage.cloudbackup.gui.notification.builder

interface NotificationBuilder {
    fun showWithoutProgressBar(title: String, message: String?, icon: Int)
    fun show(progress: Float, title: String, message: String?, secondMessage: String?, icon: Int, secondIcon: Int)
    fun showSecondMessage(secondMessage: String?, progress: Float)

    fun close()
}