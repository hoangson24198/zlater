package com.example.zlater.Service.local.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.zlater.Model.Routine;

import java.util.List;

/**
 * Created by Hades on 31,October,2019
 **/
@Dao
public interface RoutineDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Routine... routine);

    @Query("SELECT * FROM polyfit_routine")
    List<Routine> getRoutine();

    @Query("DELETE FROM polyfit_routine")
    void deleteAll();

}
