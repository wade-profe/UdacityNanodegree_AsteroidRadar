package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface NeoWs {

    @GET("neo/rest/v1/feed")
    suspend fun getFeed(@Query("api_key") key: String = Constants.API_KEY): Result<List<Asteroid>>
}

object NeoWService{

}