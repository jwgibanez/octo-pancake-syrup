package io.github.jwgibanez.contacts.utils

import android.app.AlertDialog
import android.content.Context

fun showDialog(context: Context, title: String?, message: String, onOk: () -> Unit) {
    val builder = AlertDialog.Builder(context)
    with(builder) {
        title?.let { setTitle(title) }
        setMessage(message)
        setPositiveButton(android.R.string.ok) { _, _ -> onOk() }
        show()
    }
}

fun showDialog(
    context: Context,
    title: String?,
    message: String,
    onOk: () -> Unit,
    onCancel: () -> Unit
) {
    val builder = AlertDialog.Builder(context)
    with(builder) {
        title?.let { setTitle(title) }
        setMessage(message)
        setPositiveButton(android.R.string.ok) { _, _ -> onOk() }
        setNegativeButton(android.R.string.cancel) { _, _ -> onCancel() }
        show()
    }
}