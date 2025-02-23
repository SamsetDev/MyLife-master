package com.sam.mylife.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sam.mylife.core.model.HomeResponse
import kotlinx.coroutines.launch

class DataViewModel :ViewModel(){
    private val repository = DataRepository()

    // LiveData to hold the list of HomeResponse objects
    private val _homeResponseLiveData = MutableLiveData<ArrayList<HomeResponse>>()
    val homeResponseLiveData: LiveData<ArrayList<HomeResponse>> = _homeResponseLiveData

    var selectedyear : MutableLiveData<String> = MutableLiveData()

    fun fetchYearData(year: String) {
        viewModelScope.launch {
            repository.getUserYearData(year).collect { data ->
                _homeResponseLiveData.postValue(data)
            }
        }
    }
}