package io.github.jwgibanez.contacts.database

import androidx.lifecycle.LiveData
import androidx.room.*
import io.github.jwgibanez.contacts.service.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user ORDER BY first_name ASC")
    fun all(): LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(users: List<User>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg user: User)

    @Query("DELETE FROM user WHERE id = :id")
    fun delete(id: Int)
}