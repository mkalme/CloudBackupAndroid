package com.storage.cloudbackup.logic.updater.standard.updater

enum class ErrorMessage(val value: String) {
    NoInternetConnection("NoInternetConnection"),
    InadequateInternetConnection("InadequateInternetConnection"),
    MonthlyMobileDataExceeded("MonthlyMobileDataExceeded"),
    DirectoryDoesNotExist("DirectoryDoesNotExist"),
    AllSchemesUnsuccessful("AllSchemesUnsuccessful"),
    Other("Other")
}