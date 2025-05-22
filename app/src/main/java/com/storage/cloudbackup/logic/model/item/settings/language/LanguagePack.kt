package com.storage.cloudbackup.logic.model.item.settings.language

interface LanguagePack {
    val name: String
    val code: String

    fun getTranslation(key: String): String
}