package com.storage.cloudbackup.gui.utilities


object Globals {
    private val Data = HashMap<String, Any>()
    fun setData(key: String, data: Any) {
        Data[key] = data
    }

    fun getData(key: String): Any? {
        val data = Data[key]
        Data.remove(key)
        return data
    }
}
