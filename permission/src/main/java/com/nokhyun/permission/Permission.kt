package com.nokhyun.permission

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

/**
 * @author Nokhyun90 on 2023-08-03
 * */
class Permission private constructor(
    private val activity: AppCompatActivity,
    private val builder: Builder
) {
    private val permissionListener = builder.permissionListener!!
    private val isSinglePermission = !builder.singlePermission.isNullOrEmpty()
    private val singlePermission = activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            permissionListener.onGranted()
        } else {
            permissionListener.onRejected()
        }
    }

    private val multiplePermissions = activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
        if (map.values.all { it }) {
            permissionListener.onGranted()
        } else {
            permissionListener.onRejected()
        }
    }

    fun launch() {
        if (isSinglePermission) {
            singlePermission()
        } else {
            multiplePermissions()

        }
    }

    private fun singlePermission() {
        val permission = builder.singlePermission!!
        when {
            ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED -> permissionListener.onGranted()
            activity.shouldShowRequestPermissionRationale(permission) -> permissionAlertDialog()
            else -> singlePermission.launch(permission)
        }
    }

    private fun multiplePermissions() {
        val permission = builder.multiplePermissions!!
        when {
            permission.all { ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED } -> permissionListener.onGranted()
            permission.any { activity.shouldShowRequestPermissionRationale(it) } -> permissionAlertDialog()
            else -> multiplePermissions.launch(builder.multiplePermissions)
        }
    }

    private fun permissionAlertDialog() {
        if (builder.alertDialogBuilder == null) {
            checkNotNull(builder.onPositive) { throw NullPointerException("onPositive is Null") }
            checkNotNull(builder.onNegative) { throw NullPointerException("onNegative is Null") }

            AlertDialog.Builder(activity)
                .setTitle(activity.resources.getString(R.string.title))
                .setMessage(activity.resources.getString(R.string.message))
                .setPositiveButton(activity.resources.getString(R.string.confirm)) { dialog, _ ->
                    activity.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", activity.packageName, null)
                    })
                    dialog.dismiss()
                }
                .setNegativeButton(activity.resources.getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        } else {
            builder.alertDialogBuilder!!
                .create()
                .show()
        }
    }

    class Builder(
        private val activity: AppCompatActivity
    ) {
        internal var permissionListener: PermissionListener? = null
        internal var onPositive: DialogInterface.OnClickListener? = null
        internal var onNegative: DialogInterface.OnClickListener? = null
        internal var singlePermission: String? = null
        internal var multiplePermissions: Array<String>? = null
        internal var alertDialogBuilder: AlertDialog.Builder? = null

        fun permissionListener(permissionListener: PermissionListener): Builder {
            this.permissionListener = permissionListener
            return this@Builder
        }

        fun onPositive(onPositive: DialogInterface.OnClickListener): Builder {
            this.onPositive = onPositive
            return this@Builder
        }

        fun onNegative(onNegative: DialogInterface.OnClickListener): Builder {
            this.onNegative = onNegative
            return this@Builder
        }

        fun alertDialog(alertDialogBuilder: AlertDialog.Builder): Builder {
            this.alertDialogBuilder = alertDialogBuilder
            return this@Builder
        }

        fun singlePermission(singlePermission: String): Builder {
            this.singlePermission = singlePermission
            return this@Builder
        }

        fun multiplePermissions(multiplePermissions: Array<String>): Builder {
            this.multiplePermissions = multiplePermissions
            return this@Builder
        }

        fun build(): Permission {
            checkNull(singlePermission, multiplePermissions) { throw NullPointerException("Please enter permission") }
            checkNull(permissionListener) { throw NullPointerException("Please enter permissionListener") }
            checkAllNotNull(singlePermission, multiplePermissions) { throw NullPointerException("둘중하나만 입력 ㄱㄱㄱ") }

            return Permission(
                activity = activity,
                builder = this@Builder
            )
        }
    }
}