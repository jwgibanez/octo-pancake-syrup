package io.github.jwgibanez.contacts.service.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey var id: Long,
    var email: String?,
    var first_name: String?,
    var last_name: String?,
    var avatar: String?
)