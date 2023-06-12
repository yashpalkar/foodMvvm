package com.softdev.youtubeclone.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters

@TypeConverters
class MealTypeCoverter {

    @TypeConverter
    fun fromAnyToString(attribute:Any?):String{
        if(attribute==null)
            return ""
        return attribute as String
    }
    @TypeConverter
    fun StringToAny(attribute: String?):Any{
        if(attribute==null)
            return ""
        return attribute as Any
    }

}