package com.d2factory.echo

import android.content.Context
import okhttp3.*
import java.io.IOException

/**
 * This interceptor try to find associated mock file of network call in assets (app/src/main/assets) folder or internal app files folder.
 *
 * Example :
 * network call GET https://mydomain.com/_ah/api/user/v1.0/getUser
 * try to find file named =
 * GET-_ah-api-user-v1.0-getUser.json
 *
 * If mock file not found, it will execute the original network call.
 */
open class MockNetworkInterceptor(private val context: Context) : Interceptor {

  private val mediaTypeJson = MediaType.parse("application/json")

  @Throws(IOException::class)
  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()

    var json = getMockFromAssetsFolder(request)

    if (EchoConfig.overrideWithAssetsEnable && json == null) {
      json = getMockFromInternalAppFolder(request)
    }

    if (json == null) return chain.proceed(request)

    return Response.Builder()
        .body(ResponseBody.create(mediaTypeJson, json))
        .request(chain.request())
        .message("")
        .protocol(Protocol.HTTP_2)
        .code(200)
        .build()
  }

  private fun getMockFromInternalAppFolder(request: Request) =
      try {
        Utils.getInternalAppMockFile(context, request).readBytes().toString(Charsets.UTF_8)
      } catch (ex: Exception) {
        null
      }

  private fun getMockFromAssetsFolder(request: Request) =
      try {
        context.assets.open(Utils.getAssetMockFilePath(request)).readBytes().toString(Charsets.UTF_8)
      } catch (ex: Exception) {
        null
      }

}