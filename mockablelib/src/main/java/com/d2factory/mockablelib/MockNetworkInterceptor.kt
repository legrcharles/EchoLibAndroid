package com.d2factory.mockablelib

import android.content.Context
import android.util.Log
import java.io.IOException
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody

open class MockNetworkInterceptor(private val mContext: Context) : Interceptor {

    private val tag = "MockNetworkInterceptor"
    private val mediaTypeJson = MediaType.parse("application/json")

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val fileName = Utils.getMockFileName(request)

        try {
            val json = mContext.assets.open("mocks/$fileName").readBytes().toString(Charsets.UTF_8)

            Log.i(tag, "use mock file $fileName")

            return Response.Builder()
                    .body(ResponseBody.create(mediaTypeJson, json))
                    .request(chain.request())
                    .message("")
                    .protocol(Protocol.HTTP_2)
                    .code(200)
                    .build()
        } catch (ex: Exception) {
            Log.i(tag, "can't use mock file $fileName")

            return chain.proceed(request)
        }
    }
}