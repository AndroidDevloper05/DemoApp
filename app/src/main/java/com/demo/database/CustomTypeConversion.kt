package com.demo.database

import androidx.room.TypeConverter
import com.demo.model.MediaModel
import com.demo.model.UserModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

object CustomTypeConversion {
    @TypeConverter
    @JvmStatic
    fun userListToString(list: List<UserModel>?): String? {
        return  Gson().toJson(list)
    }
    @TypeConverter
    @JvmStatic
    fun stringUsersList(data: String?): List<UserModel>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type =object : TypeToken<List<UserModel?>?>() {}.type
        return Gson().fromJson(data, listType)
    }
    @TypeConverter
    @JvmStatic
    fun mediaListToString(list: List<MediaModel>?): String? {
        return  Gson().toJson(list)
    }
    @TypeConverter
    @JvmStatic
    fun stringMediaList(data: String?): List<MediaModel>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type =object : TypeToken<List<MediaModel?>?>() {}.type
        return Gson().fromJson(data, listType)
    }
}