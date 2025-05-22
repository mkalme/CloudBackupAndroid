package com.storage.cloudbackup.logic.model.item.history.attempt

enum class AttemptResult(val value: String) {
    Successful("Successful"),
    Unsuccessful("Unsuccessful"),
    PartiallySuccessful("PartiallySuccessful"),
    Unfinished("Unfinished")
}