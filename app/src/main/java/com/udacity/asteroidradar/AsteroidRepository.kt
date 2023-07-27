package com.udacity.asteroidradar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.udacity.asteroidradar.api.ApodService
import com.udacity.asteroidradar.api.NeoWService
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AsteroidRepository(private val database: AsteroidDatabase) {

    val asteroids: LiveData<List<Asteroid>> =
        database.asteroidDao.getAsteroidList().map {
            it.asDomainModel()
        }
    private var imageDownloadDate: String? = null
    val imageOfTheDay = MutableLiveData<PictureOfDay>()

    suspend fun retrieveAsteroids(){
        withContext(Dispatchers.IO){
            val asteroids = NeoWService.feedService.getFeed()
            database.asteroidDao.insertAll(*parseAsteroidsJsonResult(JSONObject(asteroids)).asDatabaseModel())
        }
    }

    suspend fun retrieveDailyImage(){
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val currentDate = dateFormat.format(currentTime)
        if(imageDownloadDate == null || imageDownloadDate != currentDate){
            withContext(Dispatchers.IO){
                val response = ApodService.apodService.getImageOfTheDay()
                if(response.isSuccessful){
                    imageDownloadDate = currentDate
                    if(response.body()?.mediaType == "image"){
                        imageOfTheDay.postValue(response.body())
                    }
                }
            }
        }

        //todo place image of day into room db for caching

    }
}