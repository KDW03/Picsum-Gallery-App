package com.example.picsum.core.database.util

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

// Room 데이터베이스는 Instant를 지원하지 않기에 Convert가 필요
class InstantConverter {
    @TypeConverter
    fun longToInstant(value: Long?): Instant? =
        value?.let(Instant::fromEpochMilliseconds)


    @TypeConverter
    fun instantToLong(instant: Instant?): Long? =
        instant?.toEpochMilliseconds()

}