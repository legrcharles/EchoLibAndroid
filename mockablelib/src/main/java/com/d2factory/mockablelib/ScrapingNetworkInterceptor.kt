package com.d2factory.mockablelib

import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody

/**
 * This interceptor record every network calls in Json file to your internal app files folder.
 *
 * Example :
 * network call GET https://mydomain.com/_ah/api/user/v1.0/getUser
 * try to find file named =
 * GET-_ah-api-user-v1.0-getUser.json
 */
open class ScrapingNetworkInterceptor(private val context: Context) : Interceptor {

    private val tag = "NetworkCallScraping"

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val bodyString = response.body()?.string()

        val mockFile = Utils.getInternalAppMockFile(context, request)
        mockFile.writeText(bodyString ?: "")
        Log.i(tag, "create json mock at ${mockFile.path}")

        return response.newBuilder()
                .body(ResponseBody.create(response.body()?.contentType(), bodyString))
                .build()
    }
}