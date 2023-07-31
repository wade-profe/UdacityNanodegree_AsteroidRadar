package com.udacity.asteroidradar.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.repositories.AsteroidRepository
import com.udacity.asteroidradar.database.getDatabase
import retrofit2.HttpException

class AsteroidRefreshWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

        companion object{
            const val WORK_NAME = "AsteroidRefreshWorker"
        }

    override suspend fun doWork(): Result {
        val db = getDatabase(applicationContext)
        val repository = AsteroidRepository(db)

        return try{
            repository.retrieveAsteroids()
            repository.retrieveDailyImage()
            Result.success()
        } catch(e: HttpException){
            Result.retry()
        }
    }
}