package com.borabor.travelguideapp.data.local.converter

import androidx.room.TypeConverter
import com.borabor.travelguideapp.domain.model.Image
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ImageListConverter {
    @TypeConverter
    fun fromImageList(value: List<Image>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Image>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toImageList(value: String): List<Image> {
        val gson = Gson()
        val type = object : TypeToken<List<Image>>() {}.type
        return gson.fromJson(value, type)
    }
}