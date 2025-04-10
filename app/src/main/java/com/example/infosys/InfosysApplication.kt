package com.example.infosys

import android.app.Application
import com.example.infosys.model.User

class InfosysApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }

    companion object {
        const val URL: String = "https://itnb.my.id/infosys/"
        var USER = User("", "", "","", 1)
    }
}