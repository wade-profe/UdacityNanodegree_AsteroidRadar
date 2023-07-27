package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.AsteroidRepository
import com.udacity.asteroidradar.database.getDatabase
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = AsteroidRepository(database)

    init {
        viewModelScope.launch {
            repository.retrieveAsteroids()
            repository.retrieveDailyImage()
        }
    }

    val asteroids = repository.asteroids
    val imageOfTheDay = repository.imageOfTheDay





}