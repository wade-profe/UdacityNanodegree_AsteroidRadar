package com.udacity.asteroidradar.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NeoWService
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainViewModel : ViewModel() {

    var asteroids = MutableLiveData(listOf<Asteroid>())
    var listLoading = MutableLiveData(false)

    init {
        fetchAsteroids()
    }

    private fun fetchAsteroids(){
        viewModelScope.launch{
            listLoading.value = true
            val result = NeoWService.feedService.getFeed()
            if(result.isSuccessful){
                asteroids.value = parseAsteroidsJsonResult(JSONObject(result.body() ?: ""))
            }
            listLoading.value = false
        }
    }

}