package com.example.zlater.Service.local.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.zlater.Model.StepCount;

import java.util.List;

/**
 * Created by Hoang Son on 31,October,2019
 **/
@Dao
public interface StepDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(StepCount... stepCounts);

    @Query("SELECT * FROM zlater_step_count")
    List<StepCount> getStepCount();
}
