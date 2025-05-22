package com.storage.cloudbackup.logic.cloud.provider.model

interface GuiComponent {
    val settings: CloudProviderSettings?

    fun load()
}