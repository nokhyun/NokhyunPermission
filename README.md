# NokhyunPermission

[![](https://jitpack.io/v/nokhyun/NokhyunPermission.svg)](https://jitpack.io/#nokhyun/NokhyunPermission)

-------
build.gradle(project)
```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
or

settings.gradle
```
...
  maven { url 'https://jitpack.io' }
...
```

```
dependencies{
  implementation 'com.github.nokhyun:NokhyunPermission:{version}'
}
```
-----------
* Usage
  
SinglePermission
```
private val single: Permission = Permission.Builder(this@MainActivity)
        .onPositive(onPositive)
        .onNegative(onNegative)
        .singlePermission(Manifest.permission.CALL_PHONE)
        .permissionListener(permissionListener)
        .build()
```
MultiplePermission
```
private val multiple = Permission.Builder(this@MainActivity)
        .onPositive(onPositive)
        .onNegative(onNegative)
        .multiplePermissions(arrayOf(Manifest.permission.CALL_PHONE, Manifest.permission.ACTIVITY_RECOGNITION))
        .permissionListener(permissionListener)
        .build()
```
Alertdialog Custom
```
- Please pass alert builder as the argument

private val multiple = Permission.Builder(this@MainActivity)
        .multiplePermissions(arrayOf(Manifest.permission.CALL_PHONE, Manifest.permission.ACTIVITY_RECOGNITION))
        .permissionListener(permissionListener)
        .alertDialog()
        .build()
```
permissionListener
```
private val permissionListener = object : PermissionListener {
        override fun onGranted() {
            logger { "onGranted" }
        }

        override fun onRejected() {
            logger { "onRejected" }
        }
    }
```
launch api call
```
binding.btnSinglePermission.setOnClickListener { single.launch() }
binding.btnMultiplePermissions.setOnClickListener { multiple.launch() }
```
