package com.d2factory.mockablelib

import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody

/**
 * ScrapingNetworkInterceptor
 * Add **ScrapingNetworkInterceptor** once to your OkHttpClient.Builder when you build your retrofit API.
 *
 * And then just go everywhere in app to call every WS, this will save every call of WS in Json file to your device app files folder.
 *
 * You can find and export all mocks files by exporting device folders
 * Then to use mock BuildVariant copy mocks folder to app assets folder
 */
open class ScrapingNetworkInterceptor(private val context: Context): Interceptor {

    private val tag = "NetworkCallScraping"

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val bodyString = response.body()?.string()

        if (Config.recordEnable) {
            val mockFile = Utils.getMockFile(context, request)
            mockFile.writeText(bodyString ?: "")
            Log.i(tag, "create json mock at ${mockFile.path}")
        }

        return response.newBuilder()
                .body(ResponseBody.create(response.body()?.contentType(), bodyString))
                .build()
    }
}