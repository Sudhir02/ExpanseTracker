package com.example.expansetracker.viewModel

import android.content.Context
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.expansetracker.data.ExpanseDataBase
import com.example.expansetracker.data.ExpanseRepository
import com.example.expansetracker.data.module.ListEntity
import com.example.expansetracker.data.module.ListTitle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntityListViewModel @Inject constructor(private val repository: ExpanseRepository): ViewModel() {


    val entites: Flow<List<ListEntity>> = repository.getAllEntities()
    //Log.d("EntityListViewModel", "Entities Flow initialized from repository")

    //private val _entities = MutableStateFlow<List<ListEntity>>(emptyList())
    //val entites: StateFlow<List<ListEntity>> = _entities

    private val _listTitle = MutableStateFlow<List<String>>(emptyList())
    val listTitle: StateFlow<List<String>> = _listTitle

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllListTitles().collect { title ->
                _listTitle.value = title
            }
        }
    }

    // insert list title
    fun insertListTitle(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (title.isNotBlank() && !_listTitle.value.contains(title)) {
                val listTitle = ListTitle(title = title)
                repository.insertListTitle(listTitle)
                val updatedTitle = repository.getAllListTitles().first()
                _listTitle.value = updatedTitle as List<String>
            }
        }
    }

    fun deleteListTitle(title: String){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteListTitle(title)
            val updatedTitle = repository.getAllListTitles().first()
            _listTitle.value = updatedTitle as List<String>
        }
    }
    fun addEntity(entityList: ListEntity) {
        viewModelScope.launch {
            repository.insertEntity(entityList) //save to database
        }
    }

    fun deleteEntity(entity: ListEntity) {
        viewModelScope.launch {
            repository.deleteEntity(entity)
        }
    }
}




class EntityListViewModelFactory(private val context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>) : T{
        val dataBase = ExpanseDataBase.getDatabase(context)
        val repository = ExpanseRepository(dataBase.expanseDao())
        return EntityListViewModel(repository) as T
    }
}