package com.storage.cloudbackup.gui.notification.queue

import java.util.concurrent.locks.ReentrantLock

class StandardRunnableQueue : RunnableQueue {
    private val refreshRate = 100L
    @Volatile private var isOpen = false

    @Volatile private var next: (() -> Unit)? = null

    private val lock = ReentrantLock()

    override fun runNext(func: () -> Unit) {
        if(!isOpen) return

        lock.lock()

        try {
            next = func
        }finally {
            lock.unlock()
        }
    }

    override fun open() {
        isOpen = true

        Thread {
            while (isOpen){
                lock.lock()

                var thisNext: (() -> Unit)?

                try {
                    thisNext = next
                    next = null
                }finally {
                    lock.unlock()
                }

                thisNext?.invoke()

                Thread.sleep(refreshRate)
            }
        }.start()
    }

    override fun close() {
        isOpen = false
    }
}