package com.storage.cloudbackup.logic.model.item.schedule

enum class WeekDay {
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday,
    Saturday,
    Sunday;

    fun toInt(): Int {
        return when(this){
            Monday -> 0
            Tuesday -> 1
            Wednesday -> 2
            Thursday -> 3
            Friday -> 4
            Saturday -> 5
            Sunday -> 6
        }
    }
}