package com.example.infosys.model

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("ID")
    val id: Int,

    @SerializedName("Message")
    val message: String,

    @SerializedName("Extra1")
    val extra1: String,

    @SerializedName("Extra2")
    val extra2: String,

    @SerializedName("Extra3")
    val extra3: String
)
