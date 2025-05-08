package com.example.dessertrelease.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val IS_LINEAR_LAYOUT = booleanPreferencesKey("is_linear_layout")
    }

    /**
     * You create and modify the values within a DataStore by passing a lambda to
     * the edit() method. The lambda is passed an instance of MutablePreferences,
     * which you can use to update values in the DataStore. All the updates inside
     * this lambda are executed as a single transaction. Put another way, the update
     * is atomic — it happens all at one time. This type of update prevents
     * a situation in which some values update but others do not.
     */
    suspend fun saveLayoutPreferences(isLinearLayout: Boolean) {
        /* The value does not exist in DataStore until this function is called and
        the value is set. By setting up the key-value pair in the edit() method,
        the value is defined and initialized until the app's cache or data is cleared.*/
        dataStore.edit {
            it[IS_LINEAR_LAYOUT] = isLinearLayout // it: MutablePreferences
        }
    }

}