package com.storage.cloudbackup.logic.updater.standard.updater.searcher.validator

import android.content.Context
import com.storage.cloudbackup.logic.model.item.mobile.data.usage.MobileDataUsage
import com.storage.cloudbackup.logic.model.item.scheme.NetworkType
import com.storage.cloudbackup.logic.model.item.scheme.Scheme
import com.storage.cloudbackup.logic.model.item.settings.Settings
import com.storage.cloudbackup.logic.shared.utilities.utilities.provider.GenericProvider
import com.storage.cloudbackup.logic.traffic.state.NetworkState
import com.storage.cloudbackup.logic.traffic.state.NetworkStateProvider
import com.storage.cloudbackup.logic.updater.standard.updater.ErrorMessage
import com.storage.cloudbackup.logic.updater.standard.updater.Output

class FileSearchStandardValidator(private val mobileDataUsage: MobileDataUsage, private val settings: Settings, context: Context) : FileSearchValidator {
    private val networkStateProvider: GenericProvider<NetworkState> = NetworkStateProvider(context)

    override fun validate(scheme: Scheme): Output<Any?> {
        val networkState = networkStateProvider.provide()

        if(networkState == NetworkState.None){
            return Output(null, false, listOf(ErrorMessage.NoInternetConnection.value))
        }

        if(networkState == NetworkState.MobileData && scheme.networkType == NetworkType.Wifi){
            return Output(null, false, listOf(ErrorMessage.InadequateInternetConnection.value))
        }

        if(networkState == NetworkState.WiFi && scheme.networkType == NetworkType.MobileData){
            return Output(null, false, listOf(ErrorMessage.InadequateInternetConnection.value))
        }

        if(networkState == NetworkState.MobileData && mobileDataUsage.bytesUsage >= settings.monthlyMobileDataLimit.getLongSize().toLong()){
            return Output(null, false, listOf(ErrorMessage.MonthlyMobileDataExceeded.value))
        }

        return Output(null, true)
    }
}