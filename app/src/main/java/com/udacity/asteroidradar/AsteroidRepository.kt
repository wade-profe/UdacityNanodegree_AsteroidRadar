package com.udacity.asteroidradar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.udacity.asteroidradar.api.ApodService
import com.udacity.asteroidradar.api.NeoWService
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.DatabasePictureOfDay
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
    private var currentDate: String = getTodaysDate()
    val imageOfTheDay: LiveData<DatabasePictureOfDay> =
        database.pictureOfDayDao.getPictureOfDay()

    suspend fun retrieveAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroids = NeoWService.feedService.getFeed()
            database.asteroidDao.insertAll(*parseAsteroidsJsonResult(JSONObject(asteroids)).asDatabaseModel())
        }
    }

    suspend fun retrieveDailyImage() {
        withContext(Dispatchers.IO) {
            if (database.pictureOfDayDao.getPictureOfDay().value?.date != currentDate) {
                Log.d("WADE", "Retrieving picture of the day")
                val response = ApodService.apodService.getImageOfTheDay()
                if (response.isSuccessful) {
                    response.body().apply {
                        if (this?.mediaType == "image") {
                            this.asDatabaseModel().apply {
                                database.pictureOfDayDao.insertImage(this)
                            }
                        }
                    }
                }
            }

        }
    }

    private fun getTodaysDate(): String {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(currentTime)
    }
}