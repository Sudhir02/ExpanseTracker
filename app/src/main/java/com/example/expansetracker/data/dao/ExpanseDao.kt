package com.example.expansetracker.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.expansetracker.data.module.ExpanseEntity
import com.example.expansetracker.data.module.ExpanseSummary
import com.example.expansetracker.data.module.ListEntity
import com.example.expansetracker.data.module.ListTitle
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpanseDao {

     @Query("SELECT * FROM expanse_table Order By date DESC")
     fun getAllExpanses(): Flow<List<ExpanseEntity>>

     @Query("SELECT type,date, SUM(amount) as total FROM expanse_table where type = :type GROUP BY type,date ORDER BY date")
        fun getAllExpansesByDate(type: String = "Expense"): Flow<List<ExpanseSummary>>

     @Query("DELETE FROM expanse_table")
     suspend fun deleteAll()

     @Insert
     suspend fun insertExpanse(expanseEntity: ExpanseEntity)

     @Delete
     suspend fun delete(expanseEntity: ExpanseEntity)

     @Update
     suspend fun updateExpanse(expanseEntity: ExpanseEntity)


     // List entity
     @Query("Select * FROM list_entity")
     fun  getALLEntities(): Flow<List<ListEntity>>

     @Insert
     suspend fun insertEntity(entityList:ListEntity)

     @Delete
     suspend fun deleteEntity(entity: ListEntity)

     //List Title
        @Insert
        suspend fun insertListTitle(listTitle: ListTitle)


        @Query("SELECT title FROM list_titles")
        fun getAllListTitles(): Flow<List<String>>

        @Query("DELETE FROM list_titles WHERE title = :title")
        suspend fun deleteListTitle(title: String)






}