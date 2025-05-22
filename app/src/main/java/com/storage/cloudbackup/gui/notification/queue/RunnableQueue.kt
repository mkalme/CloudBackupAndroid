package com.storage.cloudbackup.gui.notification.queue

interface RunnableQueue {
    fun runNext(func: () -> Unit)

    fun open()
    fun close()
}