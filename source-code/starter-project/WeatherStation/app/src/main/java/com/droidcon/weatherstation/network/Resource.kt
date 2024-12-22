package com.droidcon.weatherstation.network

sealed class Resource<out T : Any> {
    data class Success<out T : Any>(val data: T? = null, val secondaryData: Any? = null) : Resource<T>()
    data class Error(val error: String, val data: Any? = null) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
    object Empty : Resource<Nothing>()

    fun toData(): T? = if (this is Success) this.data else null
}