package com.storage.cloudbackup.logic.shared.utilities.resource

import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent
import com.storage.cloudbackup.logic.shared.utilities.resource.stream.provider.StreamProvider
import java.io.InputStream
import java.io.OutputStream

abstract class ResourceObject {
    abstract val streamProvider: StreamProvider
    abstract val propertyChangeListener: MutableList<EmptyEvent>

    abstract fun createDefault()
    abstract fun load(inputStream: InputStream)
    abstract fun save(outputStream: OutputStream)

    protected fun initialize(){
        loadResource()

        propertyChangeListener.add(object : EmptyEvent {
            override fun onInvoke() {
                onPropertyChange()
            }
        })
    }

    private fun onPropertyChange(){
        saveResource()
    }

    private fun saveResource(){
        save(streamProvider.getOutputStream())
        streamProvider.notifyOutputStreamFinished()
    }
    private fun loadResource(){
        if(streamProvider.isInputStreamAvailable()){
            load(streamProvider.getInputStream())
            streamProvider.notifyOutputStreamFinished()
        }else{
            createDefault()
            saveResource()
        }
    }
}