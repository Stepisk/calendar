package com.stepisk.calendar

import java.text.SimpleDateFormat
import java.util.*

abstract class MyDateFormat {
    companion object {
        val sdf: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.US)
        val stf: SimpleDateFormat = SimpleDateFormat("HH:mm", Locale.US)
    }
}