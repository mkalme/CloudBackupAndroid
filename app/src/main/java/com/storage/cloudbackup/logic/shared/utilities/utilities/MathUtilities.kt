package com.storage.cloudbackup.logic.shared.utilities.utilities

object MathUtilities {
    fun roundUp(n: Int, mod: Int): Int {
        if(n % mod == 0) return n
        return n + (mod - (n % mod))
    }
}