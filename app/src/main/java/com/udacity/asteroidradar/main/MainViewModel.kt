package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.AsteroidRepository
import com.udacity.asteroidradar.InternetCheck
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.getDatabase
import kotlinx.coroutines.launch

enum class FilterValues{
    WEEK, DAY, SAVED
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = AsteroidRepository(database)
    private val currentFilter = MutableLiveData(FilterValues.WEEK)

    init {
        if (InternetCheck.isConnected(application)) {
            viewModelScope.launch {
                try{
                    repository.retrieveAsteroids()
                    repository.retrieveDailyImage()
                } catch (e: Exception){
                    Log.e(this.javaClass.simpleName, e.localizedMessage)
                }
            }
        } else {
            Toast.makeText(
                application,
                application.getText(R.string.no_connection),
                Toast.LENGTH_LONG
            ).show()
        }

    }

    val asteroids: LiveData<List<Asteroid>> = currentFilter.switchMap {filter ->
        when(filter){
            FilterValues.DAY -> repository.todayAsteroids
            FilterValues.SAVED -> repository.allAsteroids
            else -> repository.weekAsteroids
        }
    }
    val imageOfTheDay = repository.imageOfTheDay
    val imageOfTheDayTitle: LiveData<String?> =
        imageOfTheDay.map {
            it?.title
        }

    fun updateFilter(value: FilterValues){
        currentFilter.value = value
    }



}