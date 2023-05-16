package com.hdd.globalmovie.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    var _id:String="",

    @ColumnInfo(name = "user_fullname")
    var fullname:String? = null,

    @ColumnInfo(name = "user_username")
    var username:String? = null,

    @ColumnInfo(name = "user_email")
    var email:String? = null,

    @ColumnInfo(name = "user_address")
    var address:String? = null,

    @ColumnInfo(name = "user_phone")
    var phone:String? = null,

    @ColumnInfo(name = "user_password")
    var password:String? = null,

    @ColumnInfo(name = "user_confirm_password")
    var confirmPassword:String? = null,

    @ColumnInfo(name = "user_role")
    var role:String? = null,

    @ColumnInfo(name = "user_profile_pic")
    var profilePicUrl:String? = null
)