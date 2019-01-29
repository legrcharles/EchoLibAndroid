package com.d2factory.mockablelib

import android.content.Context
import okhttp3.OkHttpClient
import java.io.File

object MockableConfig {

    /**
     * Setup the mockable lib with :
     * bundleName => record/read in separate mock folder (for example record json files for user1 in bundle user1 and then user2 in bundle user2)
     * recordEnable => when you want to save json files
     * useMockEnable => when you want to use json files
     *
     * /!\ you can't record and use mock at same times !!!
     */
    fun OkHttpClient.Builder.setupMock(context: Context, bundleName: String = "", recordEnable: Boolean = false, useMockEnable: Boolean = false) {
        MockableConfig.bundleName = bundleName

        if (useMockEnable && recordEnable) throw Exception("Mockable Lib can't have useMockEnable and recordEnable to true")

        if(useMockEnable) {
            this.addInterceptor(MockNetworkInterceptor(context))
        }
        if(recordEnable) {
            this.addInterceptor(ScrapingNetworkInterceptor(context))
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
