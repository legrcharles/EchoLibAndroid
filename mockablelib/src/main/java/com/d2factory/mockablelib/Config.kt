package com.d2factory.mockablelib

import java.io.File

object Config {

    var recordEnable = false // save json file for each networkCall
    var useMockEnable = false // use json file over each networkCall

    var mockFolderName = "mock"

    // add File.separator to the end of bundleName to create a separate folder
    var bundleName = ""

    var bundleMockFolder = "mock"
        get() {
            return if (bundleName.isBlank()) {
                mockFolderName
            }else {
                "$mockFolderName${File.separator}$bundleName"
            }

        }

    // Try to get mock file in mock folder before to access assets mock files
    var autoFetchEnable = true

    fun setup(recordEnable: Boolean = false, useMockEnable: Boolean = false, bundleName: String = "") {
        this.recordEnable = recordEnable
        this.useMockEnable = useMockEnable
        this.bundleName = bundleName
    }
}