package com.borabor.travelguideapp.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import com.borabor.travelguideapp.data.remote.api.TravelApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://mockapi.io/projects/6339f07e471b8c3955686977"

    private const val CACHE_SIZE = 1024 * 1024 * 5L // 5 MB
    private const val CACHE_MAX_AGE = 60 * 60  // 1 hour
    private const val CACHE_MAX_STALE = 60 * 60 * 24 * 7 // 7 days

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

            when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnected
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient = OkHttpClient.Builder()
        .cache(Cache(context.cacheDir, CACHE_SIZE))
        .addInterceptor { chain ->
            chain.proceed(
                chain.request()
                    .newBuilder()
                    .header(
                        "Cache-Control",
                        if (isNetworkAvailable(context)) "public, max-age=$CACHE_MAX_AGE"
                        else "public, only-if-cached, max-stale=$CACHE_MAX_STALE"
                    )
                    .build()
            )
        }
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideTravelApi(retrofit: Retrofit): TravelApi = retrofit.create(TravelApi::class.java)
}