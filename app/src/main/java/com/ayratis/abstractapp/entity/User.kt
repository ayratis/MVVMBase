package com.ayratis.abstractapp.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "users")
data class User (

    @PrimaryKey
    @SerializedName("id")
    val id : Long,

    @SerializedName("login")
    val login : String,

    @SerializedName("avatar_url")
    val avatarUrl : String
)