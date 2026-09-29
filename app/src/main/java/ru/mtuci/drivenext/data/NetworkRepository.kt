package ru.mtuci.drivenext.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

interface NetworkRepository {
    fun hasInternet(): Boolean
}

class AndroidNetworkRepository(context: Context) : NetworkRepository {
    private val connectivityManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun hasInternet(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        val hasInternet = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        val isValidated = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        return hasInternet && isValidated
    }
}
