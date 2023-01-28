package com.uchi.sling

import android.app.Application

open class SlingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
    companion object {
        /** Singleton instance of this class.
         * Note: this may not be initialized if AnkiDroid is run via BackupManager
         */
        lateinit var instance: SlingApp
    }
}