package com.dicoding.naufal.mtoshokan.ui.utils

import org.joda.time.DateTime
import org.joda.time.Days
import java.util.*

fun getNextTenDays(optionalDay: Int = 10): Date {
    var todayJ = DateTime.now()
    todayJ = todayJ.plusDays(optionalDay)
    return todayJ.toDate()
}

fun Date.getRemaingDays(): Int {
    val returnDate = DateTime(this)
    val today = DateTime.now()

    return Days.daysBetween(today.toLocalDate(), returnDate.toLocalDate()).days

}

