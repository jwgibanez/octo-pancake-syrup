package io.github.jwgibanez.contacts.database

import androidx.lifecycle.LiveData
import androidx.room.*
import io.github.jwgibanez.contacts.service.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user ORDER BY first_name ASC")
    fun all(): LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<User>)

    @Delete
    fun delete(fact: User)
}