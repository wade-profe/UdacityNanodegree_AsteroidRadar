package com.udacity.asteroidradar.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.api.ApodService
import com.udacity.asteroidradar.api.NeoWService
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.domain.asDatabaseModel
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.DatabasePictureOfDay
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AsteroidRepository(private val database: AsteroidDatabase) {

    private var currentDate: String = getTodaysDate()

    val weekAsteroids: LiveData<List<Asteroid>> =
        database.asteroidDao.getWeekList(currentDate).map {
            it.asDomainModel()
        }

    val todayAsteroids: LiveData<List<Asteroid>> by lazy{
        database.asteroidDao.getTodayList(currentDate).map {
            it.asDomainModel()
        }
    }


    val allAsteroids: LiveData<List<Asteroid>> by lazy{
        database.asteroidDao.getSavedList().map {
            it.asDomainModel()
        }
    }

    val imageOfTheDay: LiveData<DatabasePictureOfDay?> =
        database.pictureOfDayDao.getPictureOfDay()

    suspend fun retrieveAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroids = NeoWService.feedService.getFeed()
            database.asteroidDao.insertAll(*parseAsteroidsJsonResult(JSONObject(asteroids)).asDatabaseModel())
        }
    }

    suspend fun retrieveDailyImage() {
        val latestDate: String? = imageOfTheDay.value?.date
        if (latestDate != currentDate) {
            withContext(Dispatchers.IO) {
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