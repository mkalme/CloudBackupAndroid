package com.storage.cloudbackup.logic.model.item.data.amount

data class DataAmount(val unit: DataUnit, val amount: ULong){
    fun getLongSize() : ULong {
        return unit.getLongSize() * amount
    }

    fun clone() : DataAmount {
        return DataAmount(unit, amount)
    }
}