package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.http.GET
import retrofit2.http.Query

interface NeoWs {

    @GET("neo/rest/v1/feed")
    suspend fun getFeed(@Query("api_key") key: String = Constants.API_KEY): Result<JSONObject>
}

object NeoWService{

}