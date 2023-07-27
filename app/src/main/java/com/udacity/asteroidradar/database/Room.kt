package com.udacity.asteroidradar.database

import android.content.Context
import android.provider.ContactsContract.Data
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update

@Dao
interface AsteroidDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroid)

    @Query("select * FROM databaseasteroid ORDER BY closeApproachDate")
    fun getAsteroidList(): LiveData<List<DatabaseAsteroid>>
    //todo filter for date >= today
}

interface PictureOfDayDao{
    @Query("select * FROM databasepictureofday")
    fun getPictureOfDay(): LiveData<List<DatabasePictureOfDay>>

    @Query("INSERT OR REPLACE INTO databasepictureofday (url, title) VALUEs (:url, :title)")
    fun insertNewImage(url: String, title: String)

}

@Database(entities = [DatabaseAsteroid::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                AsteroidDatabase::class.java,
                "asteroids").build()
        }
    }
    return INSTANCE
}