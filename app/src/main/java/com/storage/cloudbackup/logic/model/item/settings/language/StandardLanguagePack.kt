package com.storage.cloudbackup.logic.model.item.settings.language

class StandardLanguagePack(private val map: Map<String, String>, override val name: String, override val code: String) :
    LanguagePack {
    override fun getTranslation(key: String): String {
        return map[key] ?: ""
    }
}