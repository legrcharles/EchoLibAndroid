package com.d2factory.mockablelib

import android.content.Context
import android.util.Log
import java.io.IOException
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.File

/**
 * MockNetworkInterceptor
 *
 * Add interceptor to your OkHttpClient.Builder when you build your retrofit API.
 *
 * Then when you use the application with build Variant mock and you use for example the WS declared like that in the ApiService of retrofit :
 * <pre>{@code
 * @GET("_ah/api/user/v1.0/getUser") Single<UserLoginDto> login(@Query("email") String email);
 * }
 * </pre>
 *
 * The MockNetworkInterceptor will try to find the file **GET-_ah-api-user-v1.0-getUser.json** in your app assets folder (app/src/main/assets).
 * If not found, it execute the orginal network call.
 */
open class MockNetworkInterceptor(private val context: Context) : Interceptor {

    private val mediaTypeJson = MediaType.parse("application/json")

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        var json: String? = null

        if (Config.useMockEnable) {
            kotlin.runCatching {
                if (Config.autoFetchEnable) {
                    json = Utils.getMockFile(context, request).readBytes().toString(Charsets.UTF_8)
                }

                if (json == null) {
                    json = context.assets.open(Utils.getAssetMockFilePath(context, request)).readBytes().toString(Charsets.UTF_8)
                }

                return Response.Builder()
                        .body(ResponseBody.create(mediaTypeJson, json))
                        .request(chain.request())
                        .message("")
                        .protocol(Protocol.HTTP_2)
                        .code(200)
                        .build()
            }
        }

        return chain.proceed(request)
    }
}