package com.storage.cloudbackup.gui.utilities.view

import com.storage.cloudbackup.logic.model.item.settings.language.LanguagePack

object LanguageUtilities {
    fun translateEditable(key: String, editable: Boolean, languagePack: LanguagePack): String {
        val newKey = "${key}.${if(editable) "enabled" else "disabled"}"
        return languagePack.getTranslation(newKey)
    }
}