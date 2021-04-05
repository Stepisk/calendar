package com.stepisk.calendar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event")
data class EventData(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    var title: String,
    var text: String,
    @ColumnInfo(name = "time_millis")
    var timeInMillis: Long
)