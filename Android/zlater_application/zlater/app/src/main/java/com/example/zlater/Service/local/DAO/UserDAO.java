package com.example.zlater.Service.local.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.zlater.Model.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Query("SELECT * FROM  zlater_users")
    List<User> getAllUsers();

    @Insert
    void registerUser(User user);
}
