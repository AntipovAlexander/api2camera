package com.antipov.camera2apiidp.utils

import java.io.File

class PreviewPicker(private val filesDir: File?) {
    fun get(): String? = with(File(filesDir, "")) {
        return if (exists())
            listFiles { _, name -> name.endsWith(".jpg") }?.maxBy { it.lastModified() }?.absolutePath
        else null
    }
}