package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Dao
interface AsteroidDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroid)

    @Query("select * FROM databaseasteroid WHERE closeApproachDate >= :currentDate ORDER BY closeApproachDate ASC")
    fun getWeekList(currentDate: String): LiveData<List<DatabaseAsteroid>>

    @Query("select * FROM databaseasteroid WHERE closeApproachDate = :currentDate")
    fun getTodayList(currentDate: String): LiveData<List<DatabaseAsteroid>>

    @Query("select * FROM databaseasteroid ORDER BY closeApproachDate ASC")
    fun getSavedList(): LiveData<List<DatabaseAsteroid>>
}

@Dao
interface PictureOfDayDao{
    @Query("select * FROM databasepictureofday ORDER BY date DESC LIMIT 1")
    fun getPictureOfDay(): LiveData<DatabasePictureOfDay?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImage(picture: DatabasePictureOfDay)

}

@Database(entities = [DatabaseAsteroid::class, DatabasePictureOfDay::class], version = 4)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
    abstract val pictureOfDayDao: PictureOfDayDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                AsteroidDatabase::class.java,
                "asteroids").fallbackToDestructiveMigration().build()
        }
    }
    return INSTANCE
}