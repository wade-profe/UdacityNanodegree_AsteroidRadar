package com.udacity.asteroidradar.utils

import android.content.Context
import android.net.ConnectivityManager

class InternetCheck {

    companion object{
        fun isConnected(context: Context): Boolean{
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            return capabilities != null
        }
    }

}