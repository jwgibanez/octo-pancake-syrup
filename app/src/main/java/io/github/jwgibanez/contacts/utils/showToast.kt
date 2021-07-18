package io.github.jwgibanez.contacts.utils

import android.content.Context
import android.widget.Toast

val toast: (Context, String) -> Unit = { context, message ->
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}