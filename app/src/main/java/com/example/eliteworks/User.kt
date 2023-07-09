package com.example.eliteworks

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(
    val id:String="",
    val email:String="",
    val name:String="",
    val password:String="",
    val address:String="",
    val gender:String="",
    val phoneNo:String="",
    val photo:String="",
    val profileCompleted:Boolean=false,
    val role:String=""
):Parcelable