package com.example.market.utils

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment


fun Fragment.isGranted(permission: AppPermission) = run {
    context?.let {
        (PermissionChecker.checkSelfPermission(it, permission.permissionName
        ) == PermissionChecker.PERMISSION_GRANTED)
    } ?: false
}

fun Fragment.shouldShowRationale(permission: AppPermission) = run {
    shouldShowRequestPermissionRationale(permission.permissionName)
}

fun Fragment.requestPermission(permission: AppPermission) {
    requestPermissions(arrayOf(permission.permissionName), permission.requestCode
    )
}

sealed class AppPermission(
    val permissionName: String, val requestCode: Int
) {

    object ACCESS_CAMERA : AppPermission(
        Manifest.permission.CAMERA, 42
    )


}