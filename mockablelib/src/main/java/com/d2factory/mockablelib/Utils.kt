package com.d2factory.mockablelib

import android.content.Context
import okhttp3.Request
import java.io.File

internal object Utils {

    fun getMockFile(context: Context, request: Request): File {
        val baseFolder = Utils.getMockDir(context)
        return File(baseFolder, Utils.getMockFileNameFromRequest(request))
    }

    fun getAssetMockFilePath(context: Context, request: Request): String {
        val mockFileName = Utils.getMockFileNameFromRequest(request)
        return "${Config.bundleName}${Config.mockFolderName}/$mockFileName"
    }

    private fun getMockDir(context: Context): File {
        val baseFolderPath = context.filesDir.path +
                File.separator +
                context.getString(R.string.app_name) +
                File.separator +
                Config.bundleMockFolder

        val baseFolder = File(baseFolderPath)

        if (!baseFolder.exists()) {
            baseFolder.mkdirs()
        }
        return baseFolder
    }

    fun getMockFileNameFromRequest(request: Request): String {
        val pathSegments = request.url().pathSegments()
        val httpMethod = request.method()

        var fileName = httpMethod.toUpperCase()
        pathSegments.forEach {
            fileName = "$fileName-$it"
        }

        return "$fileName.json"
    }
}