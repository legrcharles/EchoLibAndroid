package com.d2factory.mockablelib

import android.content.Context
import okhttp3.Request
import java.io.File

internal object Utils {

    fun getInternalAppMockFile(context: Context, request: Request): File {
        val baseFolder = Utils.getInternalAppMockDir(context)
        return File(baseFolder, Utils.getMockFileNameFromRequest(request))
    }

    private fun getInternalAppMockDir(context: Context): File {
        val baseFolderPath = context.filesDir.path +
                File.separator +
                context.getString(R.string.app_name) +
                File.separator +
                MockableConfig.bundleMockFolder

        val baseFolder = File(baseFolderPath)

        if (!baseFolder.exists()) {
            baseFolder.mkdirs()
        }
        return baseFolder
    }

    fun getAssetMockFilePath(request: Request): String {
        val mockFileName = Utils.getMockFileNameFromRequest(request)
        return "${MockableConfig.bundleName}${MockableConfig.mockFolderName}/$mockFileName"
    }

    private fun getMockFileNameFromRequest(request: Request): String {
        val pathSegments = request.url().pathSegments()
        val httpMethod = request.method()

        var fileName = httpMethod.toUpperCase()
        pathSegments.forEach {
            fileName = "$fileName-$it"
        }

        return "$fileName.json"
    }
}