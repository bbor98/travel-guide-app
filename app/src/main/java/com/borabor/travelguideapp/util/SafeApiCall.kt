package com.borabor.travelguideapp.util

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SafeApiCall @Inject constructor(@ApplicationContext val context: Context) {
    suspend inline fun <T> execute(crossinline body: suspend () -> T) = try {
        Resource.Success(
            withContext(Dispatchers.IO) {
                body()
            }
        )
    } catch (e: Exception) {
        Resource.Error.also {
            Log.e("ResourceException", e.stackTraceToString())
        }
    }
}