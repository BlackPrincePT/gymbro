package com.pegio.gymbro.presentation.core

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtil {
    fun todayDate(): String {
        val formatter = SimpleDateFormat("d MMMM h:mma", Locale.getDefault())
        return formatter.format(Date())
    }
}