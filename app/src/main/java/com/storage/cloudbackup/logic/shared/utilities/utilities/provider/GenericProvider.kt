package com.storage.cloudbackup.logic.shared.utilities.utilities.provider

interface GenericProvider<T> {
    fun provide(): T
}