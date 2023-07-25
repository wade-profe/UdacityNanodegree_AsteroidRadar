package com.udacity.asteroidradar.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid

class MainViewModel : ViewModel() {

    var asteroids = MutableLiveData<ArrayList<Asteroid>>()

    init {
        fetchAsteroids()
    }

    private fun fetchAsteroids(){
        val newAsteroids: ArrayList<Asteroid> = ArrayList()
        newAsteroids.addAll(listOf(
            Asteroid(1L, "codeName1", "2023-07-15",
                2.53, 3.45, 1.23, 3.45, true),
            Asteroid(1L, "codeName2", "2023-07-16",
                2.53, 3.45, 1.23, 3.45, false),
                    Asteroid(1L, "codeName3", "2023-07-17",
            2.53, 3.45, 1.23, 3.45, true),
            Asteroid(1L, "codeName4", "2023-07-18",
                2.53, 3.45, 1.23, 3.45, false)))
        asteroids.value = newAsteroids
    }

}