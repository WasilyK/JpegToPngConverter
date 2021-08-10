package com.wasilyk.app.jpegtopngconverter.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.net.toUri
import io.reactivex.rxjava3.core.Completable
import java.io.File
import java.io.FileOutputStream
import java.lang.RuntimeException

const val PNG_FILE_NAME = "new_png"

class IConverterImpl(private val context: Context): IConverter {

    override fun convert(stringUri: String): Completable {
        return Completable.create { emitter ->
            try {
                Thread.sleep(3000)
            } catch(e: InterruptedException) {
                return@create
            }
            val jpegUri = stringUri.toUri()
            val bitmap = createBitmap(jpegUri)
            val file = createFile()
            FileOutputStream(file).use {
                if(bitmap.compress(Bitmap.CompressFormat.PNG, 0, it)) {
                    emitter.onComplete()
                } else {
                    emitter.onError(RuntimeException("Error"))
                }
            }
        }
    }

    private fun createBitmap(uri: Uri): Bitmap {
        return if(Build.VERSION.SDK_INT >= 28) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
    }

    private fun createFile(): File {
        return File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "$PNG_FILE_NAME.png")
    }
}