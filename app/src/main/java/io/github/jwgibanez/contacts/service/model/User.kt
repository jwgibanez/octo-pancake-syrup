package io.github.jwgibanez.contacts.service.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey var id: Int = -1,
    var email: String? = null,
    var mobile: String? = null,
    var first_name: String? = null,
    var last_name: String? = null,
    var avatar: String? = null
)