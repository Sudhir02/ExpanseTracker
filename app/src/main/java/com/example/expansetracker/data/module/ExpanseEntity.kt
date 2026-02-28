package com.example.expansetracker.data.module


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expanse_table")
data class ExpanseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val title: String,
    val amount: Double,
    val date: String,
    val category: String,
    val type: String
)
