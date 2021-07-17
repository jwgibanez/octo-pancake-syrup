package io.github.jwgibanez.contacts.service.model

data class Response<T>(
    var page: Int,
    var per_page: Int,
    var total: Int,
    var total_pages: Int,
    var data: List<T>,
)