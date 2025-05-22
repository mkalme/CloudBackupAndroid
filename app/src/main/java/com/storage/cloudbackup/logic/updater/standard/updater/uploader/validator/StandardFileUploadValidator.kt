package com.storage.cloudbackup.logic.updater.standard.updater.uploader.validator

import android.content.Context
import com.storage.cloudbackup.logic.cloud.provider.model.upload.LocalFile
import com.storage.cloudbackup.logic.model.item.mobile.data.usage.MobileDataUsage
import com.storage.cloudbackup.logic.model.item.settings.Settings
import com.storage.cloudbackup.logic.shared.utilities.utilities.provider.GenericProvider
import com.storage.cloudbackup.logic.traffic.state.NetworkState
import com.storage.cloudbackup.logic.traffic.state.NetworkStateProvider
import com.storage.cloudbackup.logic.updater.standard.updater.ErrorMessage
import com.storage.cloudbackup.logic.updater.standard.updater.Output

class StandardFileUploadValidator(private val mobileDataUsage: MobileDataUsage, private val settings: Settings, context: Context) : FileUploadValidator {
    private val networkStateProvider: GenericProvider<NetworkState> = NetworkStateProvider(context)

    override fun validate(file: LocalFile): Output<Any?> {
        val networkState = networkStateProvider.provide()
        val endUsage = file.size.toLong() + mobileDataUsage.bytesUsage

        if(networkState == NetworkState.MobileData && endUsage >= settings.monthlyMobileDataLimit.getLongSize().toLong()){
            return Output(null, false, listOf(ErrorMessage.MonthlyMobileDataExceeded.value))
        }

        return Output(null, true)
    }
}