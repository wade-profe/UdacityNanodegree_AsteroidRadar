package com.udacity.asteroidradar

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.udacity.asteroidradar.api.NeoWService
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidDatabase) {

    val asteroids: LiveData<List<Asteroid>> =
        database.asteroidDao.getAsteroidList().map {
            it.asDomainModel()
        }

    suspend fun retrieveAsteroids(){
        withContext(Dispatchers.IO){
            val asteroids = NeoWService.feedService.getFeed()
            database.asteroidDao.insertAll(*parseAsteroidsJsonResult(JSONObject(asteroids)).asDatabaseModel())
        }
    }
}