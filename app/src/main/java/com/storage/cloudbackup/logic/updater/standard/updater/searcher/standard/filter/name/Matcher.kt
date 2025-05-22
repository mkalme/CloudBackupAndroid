package com.storage.cloudbackup.logic.updater.standard.updater.searcher.standard.filter.name

import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.PathMatcher

class Matcher(pattern: String){
    private val matcher: PathMatcher
    private val exclude: Boolean

    init {
        exclude = pattern.startsWith(":")
        matcher = FileSystems.getDefault().getPathMatcher("glob:${pattern.substring(if(exclude) 1 else 0)}")
    }

    fun matches(path: Path): Boolean {
        val matches = matcher.matches(path)
        return matches xor exclude
    }
}
