/*
 * Created by Sujoy Datta. Copyright (c) 2020. All rights reserved.
 *
 * To the person who is reading this..
 * When you finally understand how this works, please do explain it to me too at sujoydatta26@gmail.com
 * P.S.: In case you are planning to use this without mentioning me, you will be met with mean judgemental looks and sarcastic comments.
 */

package com.morningstar.planetary.network.interceptors

import android.util.Log
import com.morningstar.planetary.network.ResponseException
import okhttp3.Interceptor
import okhttp3.Response

class ResponseInterceptorImpl : ResponseInterceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val response = chain.proceed(chain.request())

        Log.d(javaClass.simpleName, "error code ${response.code()}")
        when {
            response.code() == 400 -> throw ResponseException(response.code(), "Bad Request")
            response.code() == 401 -> throw ResponseException(
                response.code(),
                "Invalid username or password"
            )
            response.code() == 403 -> throw ResponseException(response.code(), "Forbidden")
            response.code() == 404 -> throw ResponseException(response.code(), "Not Found")
            response.code() == 405 -> throw ResponseException(response.code(), "Method Not Allowed")
            response.code() == 406 -> throw ResponseException(response.code(), "Not Acceptable")
            response.code() == 408 -> throw ResponseException(response.code(), "Request timeout")
            response.code() == 500 -> throw ResponseException(response.code(), "Server Error")
            response.code() == 204 -> throw ResponseException(
                response.code(),
                "No Content exception"
            )
            response.code() != 200 -> throw ResponseException(
                response.code(),
                "Something went wrong"
            )
            else -> return response
        }
    }
}