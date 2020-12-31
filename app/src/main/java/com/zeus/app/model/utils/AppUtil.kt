package com.zeus.app.model.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import com.zeus.app.BuildConfig
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*


val deviceType: String
    get() = Build.MODEL

val systemVersion: String
    get() = Build.VERSION.RELEASE

val appVersion: String
    get() = BuildConfig.VERSION_NAME

val lang: String
    get() = "zh_cn"

val timestamp: Long
    get() = System.currentTimeMillis()

val systemType: String
    get() = "android"

@SuppressLint("HardwareIds")
fun getDeviceUUID(context: Context): String {
    return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
}


fun getIPAddress(context: Context): String{
    val info = (context
        .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
    if (info != null && info.isConnected) {
        //当前使用2G/3G/4G网络
        if (info.type == ConnectivityManager.TYPE_MOBILE) {
            try {
                val en: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
                while (en.hasMoreElements()) {
                    val intf: NetworkInterface = en.nextElement()
                    val enumIpAddr: Enumeration<InetAddress> = intf.inetAddresses
                    while (enumIpAddr.hasMoreElements()) {
                        val inetAddress: InetAddress = enumIpAddr.nextElement()
                        if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                            return inetAddress.getHostAddress()
                        }
                    }
                }
            } catch (e: SocketException) {
                e.printStackTrace()
            }
            //当前使用无线网络
        } else if (info.type == ConnectivityManager.TYPE_WIFI) {
            val wifiManager = (context.applicationContext
                .getSystemService(Context.WIFI_SERVICE) as WifiManager)
            val wifiInfo = wifiManager.connectionInfo
            //得到IPV4地址
            return intIP2StringIP(wifiInfo.ipAddress)
        }
    }
    return ""
}


private fun intIP2StringIP(ip: Int): String {
    return (ip and 0xFF).toString() + "." +
            (ip shr 8 and 0xFF) + "." +
            (ip shr 16 and 0xFF) + "." +
            (ip shr 24 and 0xFF)
}
