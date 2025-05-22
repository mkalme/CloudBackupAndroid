package com.storage.cloudbackup.logic.model.item.settings

import com.storage.cloudbackup.logic.model.item.data.amount.DataAmount
import com.storage.cloudbackup.logic.model.item.data.amount.DataUnit
import com.storage.cloudbackup.logic.model.item.settings.language.LanguagePack
import com.storage.cloudbackup.logic.model.item.settings.language.container.LanguagePackContainer
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent
import com.storage.cloudbackup.logic.shared.utilities.observable.resource.ObservableObject

class Settings(val languagePackContainer: LanguagePackContainer) : ObservableObject() {
    var editModeEnabled: Boolean = true
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    var automaticUpdatingEnabled: Boolean = true
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    var saveHistoryAttempts: Int = 3
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    var monthlyMobileDataLimit: DataAmount = DataAmount(DataUnit.MB, 100u)
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    var languagePack: String = "en"
        set(value){
            val equals = field == value
            field = value

            if(!equals) {
                invokeListener()
                invokeLanguagePackChangedListener()
            }
        }

    val languagePackChangedListener: MutableList<EmptyEvent> = mutableListOf()

    private fun invokeLanguagePackChangedListener(){
        languagePackChangedListener.forEach {
            run {
                it.onInvoke()
            }
        }
    }

    fun getLanguagePack() : LanguagePack {
        return languagePackContainer.packs[languagePack]!!
    }
}