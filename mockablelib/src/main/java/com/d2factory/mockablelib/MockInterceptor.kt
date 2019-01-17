package com.d2factory.mockablelib

import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.File


open class MockInterceptor(private val context: Context): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val bodyString = response.body()?.string()

        val pathSegments = request.url().pathSegments()
        val lastPathSegment = pathSegments[pathSegments.size - 1]

        val baseFolderPath = context.cacheDir.path +
                File.separator +
                context.getString(R.string.app_name) +
                File.separator +
                "mocks"

        val baseFolder = File(baseFolderPath)

        if (!baseFolder.exists()) {
            baseFolder.mkdirs()
        }

        if (baseFolder.exists()) {
            val mockFile = File(baseFolder, "$lastPathSegment.json")

            mockFile.writeText(bodyString ?: "")
            Log.i("MockInterceptor", "create json mock at ${mockFile.path}")
        } else {
            Log.e("MockInterceptor", "cannot create file at path: ${baseFolder.path}")
        }

        return response.newBuilder()
                .body(ResponseBody.create(response.body()?.contentType(), bodyString))
                .build()
    }
}