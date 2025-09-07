package com.example.mechat.core

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.mechat.data.datasource.local.ChatDao
import com.example.mechat.data.datasource.local.MessageDao
import com.example.mechat.data.entity.ChatEntity
import com.example.mechat.data.entity.MessageEntity
import com.example.mechat.data.entity.ParticipantEntity
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

import java.util.Date

@Database(
    entities = [
        ChatEntity::class,
        MessageEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class ChatDatabase : RoomDatabase() {

    abstract fun chatDao(): ChatDao
    abstract fun messageDao(): MessageDao


}

class Converters {
    private val gson = Gson()


    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }



    @TypeConverter
    fun fromParticipants(participants: List<ParticipantEntity>?): String {
        return gson.toJson(participants)
    }

    @TypeConverter
    fun toParticipants(json: String): List<ParticipantEntity> {
        val type = object : TypeToken<List<ParticipantEntity>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }


    @TypeConverter
    fun fromStringList(list: List<String>?): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toStringList(json: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }


    @TypeConverter
    fun fromMutableStringList(list: MutableList<String>?): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toMutableStringList(json: String): MutableList<String> {
        val type = object : TypeToken<MutableList<String>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }
}