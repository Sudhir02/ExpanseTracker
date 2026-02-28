package com.example.expansetracker.data.module


import androidx.room.Entity
import androidx.room.PrimaryKey

//List Tittle entity
@Entity(tableName = "list_titles")
data class ListTitle(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
)




@Entity (tableName = "list_entity")
data class ListEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val entity: String,
    val quantity: Double,
    val listTitle: String, // default value for listTitle
    val quantityType: String,
    val price: Double
)
