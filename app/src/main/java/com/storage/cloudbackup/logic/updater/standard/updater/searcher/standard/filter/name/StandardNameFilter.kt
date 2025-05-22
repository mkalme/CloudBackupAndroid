package com.storage.cloudbackup.logic.updater.standard.updater.searcher.standard.filter.name

import java.nio.file.Paths

class StandardNameFilter(nameFilter: String) : NameFilter {
    private val matchers = mutableListOf<Matcher>()

    init {
        val patterns = nameFilter.split("|")

        patterns.forEach {
            matchers.add(Matcher(it))
        }
    }

    override fun filter(name: String): Boolean {
        val path = Paths.get(name)

        for(i in 0 until matchers.size){
            if(matchers[i].matches( path)) return true
        }

        return false
    }
}