package com.example.expansetracker.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expansetracker.Utils
import com.example.expansetracker.data.ExpanseDataBase
import com.example.expansetracker.data.dao.ExpanseDao
import com.example.expansetracker.data.module.ExpanseSummary
import com.github.mikephil.charting.data.Entry


class StatsViewModel(val dao: ExpanseDao): ViewModel(){
    val entries = dao.getAllExpansesByDate()

    fun getEntriesForChart(entries: List<ExpanseSummary>): List<Entry>{
        val list = mutableListOf<Entry>()
        for (entry in entries){
            val formattedDate = Utils.getMillisFromDate(entry.date)
            list.add(Entry(formattedDate.toFloat(),entry.total.toFloat()))
        }
        return list
    }
}

class StatsViewModelFactory(private val context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>):T{

            val dataBase = ExpanseDataBase.getDatabase(context)
            val dao = dataBase.expanseDao()
            return StatsViewModel(dao) as T
        }
    }
