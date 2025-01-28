package com.example.teste.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.teste.data.local.entity.User

@Dao
interface UserDAO {

    @Query("SELECT * FROM users WHERE userName = :userName LIMIT 1")
    suspend fun getUserbyName(userName: String): User?

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE userName = :userName)")
    suspend fun isUserNameTaken(userName: String): Boolean

    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>
}
