package com.storage.cloudbackup.logic.traffic

import android.os.Handler
import android.os.Looper
import com.storage.cloudbackup.logic.model.item.mobile.data.usage.MobileDataUsage
import com.storage.cloudbackup.logic.shared.utilities.utilities.provider.GenericProvider
import com.storage.cloudbackup.logic.traffic.state.NetworkState
import java.time.LocalDateTime

class MobileDataMonitor(private val mobileDataUsage: MobileDataUsage, private val dataUsageProvider: GenericProvider<Long>, private val networkStateProvider: GenericProvider<NetworkState>) {
    private val handler = Handler(Looper.getMainLooper())
    private val runnableCode = Runnable { onCheck() }

    private var lastBytesUsage = 0L
    private var lastNetworkState = NetworkState.None

    init {
        lastBytesUsage = dataUsageProvider.provide()
        lastNetworkState = networkStateProvider.provide()

        handler.post(runnableCode)
    }

    private fun onCheck(){
        if(LocalDateTime.now().month != mobileDataUsage.lastUpdated.month){
            update(0)
        }

        val bytesUsage = dataUsageProvider.provide()
        if(bytesUsage == lastBytesUsage) return

        val networkState = networkStateProvider.provide()

        if(networkState == NetworkState.MobileData || lastNetworkState == NetworkState.MobileData) {
            update(mobileDataUsage.bytesUsage + (bytesUsage - lastBytesUsage))
        }

        lastBytesUsage = bytesUsage
        lastNetworkState = networkState

        handler.postDelayed(runnableCode, 1000)
    }

    private fun update(bytesUsage: Long){
        mobileDataUsage.invokeWithSingleEventTrigger {
            mobileDataUsage.bytesUsage = bytesUsage
            mobileDataUsage.lastUpdated = LocalDateTime.now()
        }
    }
}