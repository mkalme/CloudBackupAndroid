package com.storage.cloudbackup.logic.traffic.data.usage

import android.net.TrafficStats
import com.storage.cloudbackup.logic.shared.utilities.utilities.provider.GenericProvider

class DataUsageProvider : GenericProvider<Long> {
    override fun provide(): Long {
        val uid = android.os.Process.myUid()

        val sent = TrafficStats.getUidTxBytes(uid)
        val received = TrafficStats.getUidRxBytes(uid)

        return sent + received
    }
}