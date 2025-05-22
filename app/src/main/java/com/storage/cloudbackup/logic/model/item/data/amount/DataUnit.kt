package com.storage.cloudbackup.logic.model.item.data.amount

enum class DataUnit {
    B,
    KB,
    MB,
    GB,
    TB;

    fun getLongSize(): ULong {
        return when(this){
            B -> 1u
            KB -> 1024u
            MB -> (1024u * 1024u).toULong()
            GB -> (1024u * 1024u * 1024u).toULong()
            TB -> (1024u * 1024u * 1024u * 1024u).toULong()
        }
    }
}