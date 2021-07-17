package io.github.jwgibanez.contacts.database

import androidx.lifecycle.LiveData
import androidx.room.*
import io.github.jwgibanez.contacts.service.model.User

@Dao
interface UserDao {
//    @Query("SELECT * FROM user ORDER BY id ASC")
//    val all: LiveData<List<User>?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg facts: User?)

    @Delete
    fun delete(fact: User?)
}