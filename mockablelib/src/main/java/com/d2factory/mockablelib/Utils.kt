package com.d2factory.mockablelib

import android.content.Context
import okhttp3.Request
import java.io.File

internal object Utils {

    fun getMockCacheDir(context: Context): File {
        val baseFolderPath = context.cacheDir.path +
                File.separator +
                context.getString(R.string.app_name) +
                File.separator +
                "mocks"

        val baseFolder = File(baseFolderPath)

        if (!baseFolder.exists()) {
            baseFolder.mkdirs()
        }
        return baseFolder
    }

    fun getMockFileName(request: Request): String {
        val pathSegments = request.url().pathSegments()
        val httpMethod = request.method()

        var fileName = httpMethod.toUpperCase()
        pathSegments.forEach {
            fileName = "$fileName-$it"
        }

        return "$fileName.json"
    }
}