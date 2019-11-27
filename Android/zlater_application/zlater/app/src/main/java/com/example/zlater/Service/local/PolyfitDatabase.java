package com.example.zlater.Service.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.zlater.Model.Converters;
import com.example.zlater.Model.Reminder;
import com.example.zlater.Model.Routine;
import com.example.zlater.Model.StepCount;
import com.example.zlater.Model.User;
import com.example.zlater.Service.local.DAO.ReminderDAO;
import com.example.zlater.Service.local.DAO.RoutineDAO;
import com.example.zlater.Service.local.DAO.StepDAO;
import com.example.zlater.Service.local.DAO.UserDAO;

@Database(entities = {User.class, Reminder.class, StepCount.class, Routine.class}, version = 13)
@TypeConverters({Converters.class})
public abstract class PolyfitDatabase extends RoomDatabase {
    private static final String DB_NAME = "polyfit_db";
    private static PolyfitDatabase instance;

    public static synchronized PolyfitDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), PolyfitDatabase.class, DB_NAME)
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build();
        }

        return instance;
    }

    public abstract UserDAO userDAO();
    public abstract ReminderDAO reminderDAO();
    public abstract RoutineDAO routineDAO();
    public abstract StepDAO stepDAO();
}
