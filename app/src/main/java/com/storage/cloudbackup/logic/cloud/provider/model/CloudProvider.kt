package com.storage.cloudbackup.logic.cloud.provider.model

interface CloudProvider {
    val guiComponent: GuiComponent
    val logicComponent: LogicComponent
}