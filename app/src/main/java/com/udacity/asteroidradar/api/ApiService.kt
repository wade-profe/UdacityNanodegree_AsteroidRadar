package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val retrofit_NeoWs = Retrofit.Builder().addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(Constants.BASE_URL).build()

val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit_Apod = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL).build()


interface NeoWs {
    @GET("neo/rest/v1/feed")
    suspend fun getFeed(@Query("api_key") key: String = Constants.API_KEY): String
}

interface Apod {
    @GET("planetary/apod")
    suspend fun getImageOfTheDay(@Query("api_key") key: String = Constants.API_KEY): Response<PictureOfDay>
}

object NeoWService {
    val feedService: NeoWs by lazy {retrofit_NeoWs.create(NeoWs::class.java)}
}

object ApodService {
    val apodService: Apod by lazy {retrofit_Apod.create(Apod::class.java)}
}