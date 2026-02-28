package com.example.expansetracker.data.module

import androidx.room.PrimaryKey

data class ExpanseSummary(
    @PrimaryKey(autoGenerate = true)
    @Columninfo(name = "type") val type: String,
    @Columninfo(name = "date") val date: String,
    @Columninfo(name = "total") val total: Double,
    //@Columninfo(name = "amount") val amount: Double
) {
    annotation class Columninfo(val name: String)
}
