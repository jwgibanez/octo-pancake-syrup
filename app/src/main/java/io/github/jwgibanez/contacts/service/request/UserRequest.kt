package io.github.jwgibanez.contacts.service.request

data class UserRequest(
    var email: String? = null,
    var first_name: String? = null,
    var last_name: String? = null
)