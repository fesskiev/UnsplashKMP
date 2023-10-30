package com.unsplash.android.utils

import com.unsplash.android.R
import com.unsplash.shared.data.remote.network.client.exceptions.NoNetworkException
import java.net.ConnectException

fun parseHttpError(e: Throwable): Int =
    when (e) {
        is NoNetworkException -> R.string.error_no_network
        //is HttpRequestTimeoutException -> R.string.error_timeout
        is ConnectException -> R.string.error_connect_server
        else ->  R.string.error_unknown
    }