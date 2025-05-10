package com.example.dessertrelease

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.dessertrelease.data.UserPreferencesRepository

/** Here we manually initialize the dependencies defined in the DessertReleaseApplication class
before launching the MainActivity.

Context.dataStore extension property creates a DataStore instance using preferencesDataStore.

A preference file is a file where your Android application stores simple key-value data,
like user settings or application flags.

To understand how many preference files you have, you need to look for all instances where
preferencesDataStore is called with a distinct name argument across your entire codebase.
Each unique name corresponds to a separate preference file.
 */

private const val LAYOUT_PREFERENCE_NAME = "layout_preferences"


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = LAYOUT_PREFERENCE_NAME
)

class DessertReleaseApplication : Application() {

    // class property of DessertReleaseApplication
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        userPreferencesRepository = UserPreferencesRepository(dataStore)
    }
}