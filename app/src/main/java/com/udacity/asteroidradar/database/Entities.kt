package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.Asteroid

@Entity
data class DatabaseAsteroid constructor(
    @PrimaryKey
    val id: Int,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: String,
    val estimatedDiameter: String,
    val relativeVelocity: String,
    val distanceFromEarth: String,
    val isPotentiallyHazardous: Int
)

@Entity
data class DatabasePictureOfDay constructor(
    @PrimaryKey
    val url: String,
    val title: String
)

fun List<DatabaseAsteroid>.asDomainModel(): List<Asteroid>{
    return this.map {
        Asteroid(it.id.toLong(),
        it.codename,
        it.closeApproachDate,
        it.absoluteMagnitude.toDouble(),
        it.estimatedDiameter.toDouble(),
        it.relativeVelocity.toDouble(),
        it.distanceFromEarth.toDouble(),
        when(it.isPotentiallyHazardous){
            1 -> true
            0 -> false
            else -> false
        })
    }
}