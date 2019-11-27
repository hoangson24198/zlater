package com.example.zlater.Model;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * Created by Hades on 08,November,2019
 **/
public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
