package com.example.expansetracker.viewModel

import com.example.expansetracker.R
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.expansetracker.data.ExpanseDataBase
import com.example.expansetracker.data.ExpanseRepository
import com.example.expansetracker.data.module.ExpanseEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

class HomeViewModel(private val repository: ExpanseRepository): ViewModel() {
    val expanses : StateFlow<List<ExpanseEntity>> = repository.getAllExpanses().stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(),emptyList())

   //delete func for all transaction
   fun deleteAllTransaction(){
       viewModelScope.launch {
           repository.deleteAllExpense()
       }
   }

    // delete func single transaction
    fun deleteTransaction(expanseEntity: ExpanseEntity){
        viewModelScope.launch {
            repository.deleteExpense(expanseEntity)
        }
    }
    fun getBalance(list: List<ExpanseEntity>): String{
         var total = 0.0
        list.forEach{
            if (it.type == "Income"){
                total += it.amount
            }else{
                total -= it.amount
            }
        }
        return "₹%.2f".format(total)
    }
    fun getTotalExpanse(list: List<ExpanseEntity>): String{
         var total = 0.0
        list.forEach{
            if (it.type == "Expense"){
                total += it.amount
            }
        }
        return "₹%.2f".format(total)
    }
    fun getTotalIncome(list: List<ExpanseEntity>): String{
          var total = 0.0
          list.forEach{
            if (it.type == "Income"){
                total += it.amount
            }
        }
        return "₹%.2f".format(total)
    }
        fun getItemIcon(item: ExpanseEntity): Int{
            if (item.category == "Salary"){
                return R.drawable.salary
            }else if(item.category == "Paypal"){
                return R.drawable.paypal
            }else if(item.category == "Youtube"){
                return R.drawable.youtube
            }else if (item.category == "Grocery"){
                return R.drawable.shopping_bag
            }else if(item.category == "Shopping"){
                return R.drawable.shop_bag
            }else if(item.category == "Tea"){
                return R.drawable.black_tea
            }else if (item.category == "Fuel"){
                return R.drawable.fuel_icon
            }else if (item.category == "Money"){
                return R.drawable.rupee
            }else if (item.category == "Coffee"){
                return R.drawable.coffee
            }else if (item.category == "Rent"){
                return R.drawable.rent
            }else if (item.category == "Bills"){
                return R.drawable.bills
            }else if (item.category == "Other"){
                return R.drawable.other
            } else if (item.category == "Transfer"){
                return R.drawable.money_transfer
                }
                return R.drawable.other
        }
}

@RequiresApi(Build.VERSION_CODES.O)
fun calculateOverallTotals(total: List<ExpanseEntity>): Map<YearMonth,Pair<Double, Double>> {
    return total.groupBy { YearMonth.from(LocalDate.parse(it.date)) }
        .mapValues { (_,entries) ->
            val income = entries.filter { it.type == "Income" }.sumOf { it.amount }
            val expense = entries.filter { it.type == "Expense" }.sumOf { it.amount }
            income to expense
        }

}

class HomeViewModeFactory(private val context : Context): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        val  dataBase = ExpanseDataBase.getDatabase(context)
        val repository = ExpanseRepository(dataBase.expanseDao())
        return HomeViewModel(repository) as T
    }
}


