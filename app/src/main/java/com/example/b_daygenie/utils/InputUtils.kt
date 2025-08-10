package com.example.b_daygenie.utils

object InputUtils {
    fun checkEditProfileForm(name: String, date: String): String {
        if(name.isEmpty()) return "Name field cannot be empty"
        if(date.isEmpty()) return "Date field cannot be empty"
        val regex = Regex("\\d{4}-\\d{2}-\\d{2}")
        if(!regex.matches(date)) return "Date format is not valid. It should be YYYY-MM-DD"
        val (year, month, day) = date.split("-")
        if(DateUtils.calculateAge(year.toInt(), month.toInt(), day.toInt()) == -1) return "Date provided is not valid"
        if(year.toInt() < 1900) return "Year is not valid"
        if(month.toInt() < 1 || month.toInt() > 12) return "Month is not valid"
        if(day.toInt() < 1 || day.toInt() > 31) return "Day is not valid"
        return ""
    }
}