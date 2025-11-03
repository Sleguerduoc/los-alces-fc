package com.example.losalcesfc.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory

fun decodeScaledBitmap(path: String, reqSizePx: Int = 512): Bitmap? {
    val opts = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeFile(path, opts)
    val w = opts.outWidth
    val h = opts.outHeight
    if (w <= 0 || h <= 0) return null
    var inSample = 1
    while (w / inSample > reqSizePx || h / inSample > reqSizePx) inSample *= 2
    val opts2 = BitmapFactory.Options().apply { inSampleSize = inSample }
    return BitmapFactory.decodeFile(path, opts2)
}
