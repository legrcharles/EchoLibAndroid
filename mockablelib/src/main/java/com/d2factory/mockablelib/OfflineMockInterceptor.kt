package com.d2factory.mockablelib

import android.content.Context
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody

class OfflineMockInterceptor(private val mContext: Context) : Interceptor {

    private val MEDIA_JSON = MediaType.parse("application/json")

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()

        val pathSegments = request.url().pathSegments()
        val path = pathSegments[pathSegments.size - 1]

        val json = mContext.assets.open("mocks/$path.json").readBytes().toString(Charsets.UTF_8)

        return Response.Builder()
                .body(ResponseBody.create(MEDIA_JSON, json))
                .request(chain.request())
                .message("")
                .protocol(Protocol.HTTP_2)
                .code(200)
                .build()
    }
}