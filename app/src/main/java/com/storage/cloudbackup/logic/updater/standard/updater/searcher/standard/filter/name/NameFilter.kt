package com.storage.cloudbackup.logic.updater.standard.updater.searcher.standard.filter.name

interface NameFilter {
    fun filter(name: String): Boolean
}