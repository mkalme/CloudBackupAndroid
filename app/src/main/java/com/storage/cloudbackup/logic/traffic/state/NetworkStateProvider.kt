package com.storage.cloudbackup.logic.traffic.state

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.storage.cloudbackup.logic.shared.utilities.utilities.provider.GenericProvider

class NetworkStateProvider(val context: Context) : GenericProvider<NetworkState> {
    override fun provide(): NetworkState {
        if(isConnectedToWiFi()) return NetworkState.WiFi
        if(isConnectedToMobileData()) return NetworkState.MobileData
        return NetworkState.None
    }

    // isConnectedToWiFi() credit: Copilot
    private fun isConnectedToWiFi(): Boolean {
        val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }

    private fun isConnectedToMobileData(): Boolean {
        val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        return try {
            val cmClass = Class.forName(cm.javaClass.name)
            val method = cmClass.getDeclaredMethod("getMobileDataEnabled")
            method.isAccessible = true

            method.invoke(cm) as Boolean
        } catch (e: Exception) {
            false
        }
    }
}