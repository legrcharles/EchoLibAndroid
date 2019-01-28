package com.d2factory.mockablelib

import android.content.Context
import android.util.Log
import java.io.IOException
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody

/**
 * MockNetworkInterceptor
 * Create productFlavor named **mock**
 *
 * Add interceptor to your OkHttpClient.Builder when you build your retrofit API.
 *
 * <pre>{@code
 * if (BuildConfig.FLAVOR.equals("mock")) {
 *      okHttpBuilder.addInterceptor(new MockNetworkInterceptor(ApplicationContext));
 * }
 * }
 * </pre>
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