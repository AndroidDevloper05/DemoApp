package com.demo.model

import com.google.gson.annotations.SerializedName

data class UserModel (
    @SerializedName("id") val id : String,
    @SerializedName("blogId") val blogId : String,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("name") val name : String,
    @SerializedName("avatar") val avatar : String,
    @SerializedName("lastname") val lastName : String,
    @SerializedName("designation") val designation : String,
    @SerializedName("about") val about : String,
    @SerializedName("content") val content : String)




