package com.d2factory.mockablelib

import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.File


open class ScrapingNetworkInterceptor(private val context: Context): Interceptor {

    private val tag = "NetworkCallScraping"

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val bodyString = response.body()?.string()

        val baseFolder = Utils.getMockCacheDir(context)

        if (baseFolder.exists()) {
            val mockFile = File(baseFolder, Utils.getMockFileName(request))

            mockFile.writeText(bodyString ?: "")
            Log.i(tag, "create json mock at ${mockFile.path}")
        } else {
            Log.e(tag, "cannot create file at path: ${baseFolder.path}")
        }

        return response.newBuilder()
                .body(ResponseBody.create(response.body()?.contentType(), bodyString))
                .build()
    }
}