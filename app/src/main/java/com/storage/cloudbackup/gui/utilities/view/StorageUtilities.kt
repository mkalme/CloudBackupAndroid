package com.storage.cloudbackup.gui.utilities.view

import java.text.DecimalFormat
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.pow

object StorageUtilities {
    private val prefixesShort = arrayOf("B", "KB", "MB", "GB", "TB")

    fun convertToStorage(storage: Long, decimalFormat: DecimalFormat): String {
        var magnitude = floor(ln(storage.toDouble()) / ln(1024.0)).toInt()
        if (storage == 0L) magnitude = 0
        if (magnitude >= prefixesShort.size) magnitude = prefixesShort.size - 1

        val format = decimalFormat.format(storage / 1024.0.pow(magnitude.toDouble()))
        return "$format ${prefixesShort[magnitude]}"
    }
}
