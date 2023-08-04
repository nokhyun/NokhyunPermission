package com.nokhyun.samples

import android.Manifest
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.nokhyun.permission.Permission
import com.nokhyun.permission.PermissionListener
import com.nokhyun.samples.databinding.ActivityMainBinding

class MainActivity : FragmentActivity() {
    private val onPositive = DialogInterface.OnClickListener { dialog, _ -> logger { "onPositive" } }
    private val onNegative = DialogInterface.OnClickListener { dialog, _ -> logger { "onNegative" } }
    private val permissionListener = object : PermissionListener {
        override fun onGranted() {
            logger { "onGranted" }
        }

        override fun onRejected() {
            logger { "onRejected" }
        }
    }

    private val single: Permission = Permission.Builder(this@MainActivity)
        .onPositive(onPositive)
        .onNegative(onNegative)
        .singlePermission(Manifest.permission.CALL_PHONE)
        .permissionListener(permissionListener)
        .build()

    private val multiple = Permission.Builder(this@MainActivity)
        .onPositive(onPositive)
        .onNegative(onNegative)
        .multiplePermissions(arrayOf(Manifest.permission.CALL_PHONE, Manifest.permission.ACTIVITY_RECOGNITION))
        .permissionListener(permissionListener)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        binding.btnSinglePermission.setOnClickListener { single.launch() }
        binding.btnMultiplePermissions.setOnClickListener { multiple.launch() }
    }
}

fun logger(log: () -> Any) {
    Log.e("logger", log().toString())
}