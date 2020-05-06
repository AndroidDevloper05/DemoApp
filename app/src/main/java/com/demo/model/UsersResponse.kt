package com.demo.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.demo.database.CustomTypeConversion
import com.demo.database.DbConstant
import com.google.gson.annotations.SerializedName
@TypeConverters(CustomTypeConversion::class)
@Entity(tableName = DbConstant.USER_TABLE)
data class UsersResponse (
    @PrimaryKey
    @field:SerializedName("id") val id:String,
    @field:SerializedName("createdAt") val createdAt:String,
    @field:SerializedName("content") val content:String,
    @field:SerializedName("comments") val comments:Int,
    @field:SerializedName("likes") val likes:Int,
    @field:SerializedName("media") val medias : MutableList<MediaModel>,
    @field:SerializedName("user") val users : MutableList<UserModel>)