package com.storage.cloudbackup.logic.permit.language

import com.storage.cloudbackup.logic.model.item.settings.Settings
import com.storage.cloudbackup.logic.model.item.settings.language.LanguagePack
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StandardLanguagePermit(val settings: Settings) : LanguagePermit {
    override val languageChangedListener: MutableList<EmptyEvent> = mutableListOf()
    override val currentLanguage: LanguagePack
        get() = settings.getLanguagePack()

    private val uiScope = CoroutineScope(Dispatchers.Main)

    init {
        settings.languagePackChangedListener.add(object: EmptyEvent {
            override fun onInvoke() {
                invokeListener()
            }
        })
    }

    private fun invokeListener(){
        uiScope.launch {
            languageChangedListener.forEach {
                it.onInvoke()
            }
        }
    }
}