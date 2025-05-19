package com.example.dessertrelease.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {

    /**Making IS_LINEAR_LAYOUT a part of a `companion object` means there's only one instance of
     this key associated with the UserPreferencesRepository class.

    While a private val in the class would also result in one instance per
    UserPreferencesRepository object, the companion object emphasizes that this key is a singleton
    for the class itself. The Preferences.Key returned by booleanPreferencesKey() should ideally be
    the exact same object instance whenever you refer to the "is_linear_layout" preference for
    this repository. The companion object ensures this by initializing it once when the class
    is loaded.

    Readability and Intent: It signals to other developers and to your future self that
    IS_LINEAR_LAYOUT is not part of the state of an individual UserPreferencesRepository instance,
    but rather a definition or constant associated with the UserPreferencesRepository type.

    Future-Proofing for True Static-Like Behavior: If IS_LINEAR_LAYOUT needed to be accessed from
    outside the class without an instance e.g., if it weren't private, the companion object would be
    the natural place for it. While it's private now, using the companion object pattern is a common
    convention for such constants.

    In summary, while a private val directly in the class would work for this specific private key,
    putting it in a private companion object is a more idiomatic Kotlin way to:
    - Clearly define it as a class-level constant/definition.
    - Ensure a single instance of the Preferences.Key object is associated with the class.
    - Group it with other class-level constants for better organization.
     */
    private companion object {
        val IS_LINEAR_LAYOUT = booleanPreferencesKey("is_linear_layout") // To define a preference
        const val TAG = "UserPreferencesRepository"
    }

    /**
     * You create and modify the values within a DataStore by passing a lambda to
     * the edit() method. The lambda is passed an instance of MutablePreferences,
     * which you can use to update values in the DataStore. All the updates inside
     * this lambda are executed as a single transaction. Put another way, the update
     * is atomic — it happens all at one time. This type of update prevents
     * a situation in which some values update but others do not.
     *
    The value does not exist in DataStore until this function is called and
    the value is set. By setting up the key-value pair in the edit() method,
    the value is defined and initialized until the app's cache or data is cleared.
     */
    suspend fun saveLayoutPreferences(isLinearLayout: Boolean) { // To save a preference

        dataStore.edit {
            it[IS_LINEAR_LAYOUT] = isLinearLayout // it: MutablePreferences
        }
    }

    /** The `data` property is a Flow of Preferences objects. The Preferences object
     contains all the key-value pairs in the DataStore.
     Each time the data in the DataStore is updated, a new Preferences object
    is emitted into the Flow.

    The `map` function accepts a lambda with the current Preferences object as a parameter.
    You can specify the key you previously defined to obtain the layout preference.
    The value might not exist if saveLayoutPreference hasn't been called yet, so you need
    also supply a default value.

    Remember that until the preference is defined and initialized, it does not exist
    in the DataStore.

    Any time you interact with the file system on a device, it's possible that something
    can fail.
    For example, a file might not exist, or the disk could be full or unmounted.
    As DataStore reads and writes data from files, IOExceptions can occur when accessing
    the DataStore. You use `catch{}` operator to catch exceptions and handle these failures.

    */
    val isLinearLayout: Flow<Boolean> = dataStore.data // To read a preference
        .catch { // this: FlowCollector<Preferences>
            if (it is IOException){ // it: Throwable
                Log.e(TAG, "Error reading preferences", it)
                emit(emptyPreferences())
            } else {
                throw it // it: Throwable
            }
        }
        .map {
        it[IS_LINEAR_LAYOUT] ?: true // it: Preferences; Default to true if not set
    }

}