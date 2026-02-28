package com.example.expansetracker


import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {
    fun formatDateForChart(dateInMillis: Long): String{
       val dateFormatter = SimpleDateFormat("dd-MMM", Locale.getDefault())
        return dateFormatter.format(dateInMillis)
    }
    fun getMillisFromDate(date: String): Long{
        return getMilliFromDate(date)
    }

    fun getMilliFromDate(dateFormat: String?): Long{
        var date = Date()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        try {
            date = formatter.parse(dateFormat)
        }catch (e: ParseException){
            e.printStackTrace()
        }
        println("Today is $date")
        return date.time
    }
}