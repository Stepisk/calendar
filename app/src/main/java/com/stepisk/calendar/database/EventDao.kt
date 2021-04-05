package com.stepisk.calendar.database

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface EventDao {
    @Query("SELECT * FROM event ORDER BY time_millis DESC")
    fun getAllRecords(): Single<List<EventData>>

    @Query("SELECT * FROM event WHERE id = :id")
    fun getEventById(id: Int): Maybe<EventData>

    @Query("SELECT * FROM event WHERE time_millis >= :timeStart AND time_millis <= :timeEnd")
    fun getBetweenTimes(timeStart: Long, timeEnd: Long): Single<List<EventData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEvent(event: EventData): Completable

    @Update
    fun updateEvent(event: EventData): Completable

    @Delete
    fun deleteEvent(event: EventData): Completable
}