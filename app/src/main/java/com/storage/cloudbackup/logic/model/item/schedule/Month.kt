package com.storage.cloudbackup.logic.model.item.schedule

enum class Month {
    January,
    February,
    March,
    April,
    May,
    June,
    July,
    August,
    September,
    October,
    November,
    December;

    fun getAmountOfDays(year: Int): Int {
        return when (this){
            January -> 31
            February -> if((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) 29 else 28
            March -> 31
            April -> 30
            May -> 31
            June -> 30
            July -> 31
            August -> 31
            September -> 30
            October -> 31
            November -> 30
            December -> 31
        }
    }

    fun toInt(): Int{
        return when (this){
            January -> 0
            February -> 1
            March -> 2
            April -> 3
            May -> 4
            June -> 5
            July -> 6
            August -> 7
            September -> 8
            October -> 9
            November -> 10
            December -> 11
        }
    }
}