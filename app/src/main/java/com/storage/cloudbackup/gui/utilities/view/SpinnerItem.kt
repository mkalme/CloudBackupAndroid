package com.storage.cloudbackup.gui.utilities.view

data class SpinnerItem(val id: String, val displayText: String){
    override fun toString(): String {
        return displayText
    }
}
