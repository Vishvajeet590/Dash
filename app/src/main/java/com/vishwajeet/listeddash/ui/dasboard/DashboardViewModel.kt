package com.vishwajeet.listeddash.ui.dasboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishwajeet.listeddash.BuildConfig
import com.vishwajeet.listeddash.repository.DashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val dashboardRepository: DashboardRepository) : ViewModel() {
    val dashboardLiveData get() = dashboardRepository.dashboardResponseLiveData

    private val _currentGreet = MutableLiveData<String>()
    val currentGreet : LiveData<String>  get() = _currentGreet

    val jwt_token = BuildConfig.JWT_TOKEN
    fun getDashboardResponse(){
        viewModelScope.launch {
            dashboardRepository.getDashboardResponse(jwt_token)
        }
    }


    fun greetMsg(){
        val rightNow = Calendar.getInstance()
        val hour: Int =rightNow.get(Calendar.HOUR_OF_DAY)
        Log.d("DATERESP",hour.toString())

        if ( hour < 12 )
            _currentGreet.value = "Good Morning"
        if (hour in 13..16)
            _currentGreet.value = "Good Afternoon"
        if ( hour > 17 )
            _currentGreet.value = "Good Evening"
    }
}