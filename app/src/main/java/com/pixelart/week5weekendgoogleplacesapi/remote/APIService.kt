package com.pixelart.week5weekendgoogleplacesapi.remote

import com.pixelart.week4weekendgoogleplacesapi.model.APIResponse
import com.pixelart.week5weekendgoogleplacesapi.common.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET(RELATIVE_URL)
    fun getPlaces(
        @Query(LOCATION) location: String,
        @Query(TYPE) type: String,
        @Query(RADIUS_QUERY) radius: Int,
        @Query(SENSOR) sensor: Boolean,
        @Query(KEY) key: String
    ): Single<APIResponse>
}