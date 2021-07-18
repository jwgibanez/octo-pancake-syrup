package io.github.jwgibanez.contacts.utils

import io.github.jwgibanez.contacts.service.model.User

fun formFullName(
    user: User
) : String {
    return (user.first_name ?: "") + " " + user.last_name
}