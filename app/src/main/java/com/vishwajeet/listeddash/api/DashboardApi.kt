package com.vishwajeet.listeddash.api

import com.vishwajeet.listeddash.models.DashboardResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface DashboardApi {
    @GET("api/v1/dashboardNew")
    suspend fun getDashboard(@Header("Authorization") jwt : String) : Response<DashboardResponse>
}