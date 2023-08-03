package com.nokhyun.permission

internal inline fun checkNull(vararg value: Any?, errorMessage: () -> Unit) {
    if (value.all { it == null }) errorMessage()
}

internal inline fun checkAllNotNull(vararg value: Any?, errorMessage: () -> Unit){
    if (value.all { it != null }) errorMessage()
}
