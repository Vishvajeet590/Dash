package com.vishwajeet.listeddash.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vishwajeet.listeddash.api.DashboardApi
import com.vishwajeet.listeddash.models.DashboardResponse
import com.vishwajeet.listeddash.utils.NetworkResult
import javax.inject.Inject
import kotlin.math.log

class DashboardRepository @Inject constructor(private val dashboardApi: DashboardApi) {
    private val _dashboardResponseLiveData = MutableLiveData<NetworkResult<DashboardResponse>>()
    val dashboardResponseLiveData: LiveData<NetworkResult<DashboardResponse>> get() = _dashboardResponseLiveData

    suspend fun getDashboardResponse(jwt : String){
        _dashboardResponseLiveData.postValue(NetworkResult.Loading())
        val response = dashboardApi.getDashboard(jwt)
        if(response.isSuccessful && response.body() != null){
            _dashboardResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
            Log.d("DashResp", response.body().toString())
        }else{
            _dashboardResponseLiveData.postValue(NetworkResult.Error(response.errorBody().toString()))
        }

    }

}