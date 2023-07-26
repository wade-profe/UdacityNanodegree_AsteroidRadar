package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.Constants
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val retrofit = Retrofit.Builder().addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(Constants.BASE_URL).build()

interface NeoWs {
    @GET("neo/rest/v1/feed")
    suspend fun getFeed(@Query("api_key") key: String = Constants.API_KEY): String
}

object NeoWService {
    val feedService: NeoWs by lazy {retrofit.create(NeoWs::class.java)}

}