package me.fidelep.ravennews.data.api.interceptors

import okhttp3.Interceptor

interface INetworkInterceptor : Interceptor {
    fun isNetworkAvailable(): Boolean
}
