package com.zeus.app.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import androidx.core.content.FileProvider
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import io.reactivex.Observable
import java.io.File
import java.io.FileOutputStream
import java.util.*

@Throws(WriterException::class)
fun createCode(
    content: String,
    codeWidth: Int,
    codeHeight: Int,
    logoBitmap: Bitmap? = null,
    logoPercent: Float = 20f
): Bitmap? {
    //生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
    val writer = MultiFormatWriter()
    val hst: Hashtable<EncodeHintType, Any> = Hashtable()
    // 设置字符编码
    hst[EncodeHintType.CHARACTER_SET] = "UTF-8"
    // 设置二维码容错率
    hst[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
//    hst[EncodeHintType.MARGIN] = 4
    // 生成二维码矩阵信息
    val matrix =
        writer.encode(content, BarcodeFormat.QR_CODE, codeWidth, codeHeight, hst)
    val width = matrix.width
    val height = matrix.height
    //二维矩阵转为一维像素数组,也就是一直横着排了
    val pixels = IntArray(width * height)
    for (y in 0 until height) {
        for (x in 0 until width) {
            if (matrix[x, y]) {
                pixels[y * width + x] = Color.BLACK
            } else {
                pixels[y * width + x] = Color.WHITE
            }
        }
    }
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    //通过像素数组生成bitmap,具体参考api
    bitmap.setPixels(pixels, 0, width, 0, 0, width, height)

    // 5.为二维码添加logo图标
    return if (logoBitmap != null) {
        return addLogo(bitmap, logoBitmap, logoPercent)
    } else bitmap
}

private fun addLogo(srcBitmap: Bitmap?, logoBitmap: Bitmap?, logoPercent: Float): Bitmap? {
    var logoPercent = logoPercent
    if (srcBitmap == null) {
        return null
    }
    if (logoBitmap == null) {
        return srcBitmap
    }
    //传值不合法时使用0.2F
    if (logoPercent < 0f || logoPercent > 1f) {
        logoPercent = 0.2f
    }
    // 1. 获取原图片和Logo图片各自的宽、高值
    val srcWidth = srcBitmap.width
    val srcHeight = srcBitmap.height
    val logoWidth = logoBitmap.width
    val logoHeight = logoBitmap.height

    // 2. 计算画布缩放的宽高比
    val scaleWidth = srcWidth * logoPercent / logoWidth
    val scaleHeight = srcHeight * logoPercent / logoHeight

    // 3. 使用Canvas绘制,合成图片
    val bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    canvas.drawBitmap(srcBitmap, 0f, 0f, null)
    canvas.scale(scaleWidth, scaleHeight, srcWidth / 2.toFloat(), srcHeight / 2.toFloat())
    canvas.drawBitmap(logoBitmap,
        srcWidth / 2 - logoWidth / 2.toFloat(),
        srcHeight / 2 - logoHeight / 2.toFloat(),
        null)
    return bitmap
}


@Throws(Exception::class)
fun saveBitmap(context: Context, bitmap: Bitmap, filename: String): String {
    val path = context.cacheDir.path
    val file = File(path, "${filename}.png")
    if (file.exists()) {
        file.delete()
    }
    val outStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.PNG, 90, outStream)
    outStream.flush()
    outStream.close()
    MediaStore.Images.Media.insertImage(
        context.contentResolver,
        bitmap, file.toString(), null
    )
    val uri = Uri.fromFile(file)
    val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri)
    context.sendBroadcast(intent)
    return file.toString()
}

fun getUri(context: Context, path: String): Uri {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        FileProvider.getUriForFile(context, context.packageName + ".fileProvider", File(path))
    } else {
        Uri.parse(path)
    }
}

fun view2Path(context: Context, view: View): Observable<String> {
    return Observable.create<String> {
        val shareBitmap =
            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(shareBitmap)
        canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        val path = saveBitmap(context, shareBitmap, view.id.toString())
        it.onNext(path)
        it.onComplete()
    }
}

private fun updateFileFromDatabase(context: Context, file: File) {
    val paths = arrayOf(context.cacheDir.toString())
    MediaScannerConnection.scanFile(context, paths, null, null);
    MediaScannerConnection.scanFile(
        context, arrayOf(
            file.absolutePath
        ),
        null
    ) { path, uri -> }
}