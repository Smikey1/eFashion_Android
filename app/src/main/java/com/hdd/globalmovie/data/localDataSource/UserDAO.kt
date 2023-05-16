package com.hdd.globalmovie.data.localDataSource
import androidx.room.*
import com.hdd.globalmovie.data.models.User

@Dao
interface UserDAO {
    @Insert
    suspend fun registerUser(user: User)

    @Query("Select * from User where user_email=(:email) and user_password=(:password)")
    suspend fun loginUser(email:String,password:String): User

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)
}