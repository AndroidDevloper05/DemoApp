package com.demo

import android.app.Application
import com.demo.api.RestClient

class DemoApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        RestClient.getInstance()
        instance = this
    }

    companion object {
        lateinit var instance: DemoApplication
    }
}