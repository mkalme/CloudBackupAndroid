package com.storage.cloudbackup.logic.shared.utilities.utilities.provider

interface GenericArgsProvider<TKey, TOutput> {
    fun provide(key: TKey): TOutput
}