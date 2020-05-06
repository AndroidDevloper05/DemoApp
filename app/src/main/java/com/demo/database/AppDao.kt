package com.demo.database

import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.demo.model.UsersResponse

// Get Data from Local
@Dao
interface AppDao {
    @Query("SELECT * FROM  ${DbConstant.USER_TABLE}")
    fun loadAllUsers(): MutableList<UsersResponse>

    @Insert
    fun insertAllUser(usersResponse:MutableList<UsersResponse>)

    @Query("DELETE FROM  ${DbConstant.USER_TABLE}")
    fun deleteAllUsers()
}