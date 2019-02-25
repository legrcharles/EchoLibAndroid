package com.d2factory.echo

import android.content.Context
import okhttp3.OkHttpClient
import java.io.File

object EchoConfig {

    /**
     * Setup the echo lib with :
     * bundleName => record/read in separate mock folder (for example record json files for user1 in bundle user1 and then user2 in bundle user2)
     * type RECORDING => when you want to save json files
     * type MOCK_RESPONSE => when you want to use json files
     */
    fun OkHttpClient.Builder.setupMock(context: Context, bundleName: String = "", type: EchoConfigType = EchoConfigType.NONE) {
        EchoConfig.bundleName = bundleName

        when (type) {
            EchoConfigType.RECORDING -> this.addInterceptor(ScrapingNetworkInterceptor(context))
            EchoConfigType.MOCK_RESPONSE -> this.addInterceptor(MockNetworkInterceptor(context))
            EchoConfigType.NONE -> {}
        }
    }

    var mockFolderName = "mock"

    var bundleName = ""

    var bundleMockFolder = "mock"
        get() {
            return if (bundleName.isBlank()) {
                mockFolderName
            }else {
                "$mockFolderName${File.separator}$bundleName"
            }

        }

    // Try to get mock file in assets folder before to internal app files folder
    var overrideWithAssetsEnable = true
}

enum class EchoConfigType {
    RECORDING,
    MOCK_RESPONSE,
    NONE
}
