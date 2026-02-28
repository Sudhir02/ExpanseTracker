package com.example.expansetracker.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expansetracker.data.ExpanseDataBase
import com.example.expansetracker.data.ExpanseRepository
import com.example.expansetracker.data.module.ExpanseEntity

class AddExpanseViewModal(private val repository: ExpanseRepository): ViewModel() {
    suspend fun  addExpanse(expanseEntity: ExpanseEntity){
     repository.insertExpanse(expanseEntity) //save to database
}


class AddExpanseViewModalFactory(private val context: Context): ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T{
       val dataBase = ExpanseDataBase.getDatabase(context)
        val repository = ExpanseRepository(dataBase.expanseDao())
        return AddExpanseViewModal(repository) as T
        }

    }
}