package com.example.b_daygenie.utils

import android.util.Log
import com.example.b_daygenie.model.Person
import java.time.DateTimeException
import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit

object DateUtils {
    fun calculateNextBirthday(person: Person): Int{
        try {
            val today = LocalDate.now()
            val birthDate = LocalDate.of(person.birthYear, person.birthMonth, person.birthDayOfMonth)
            var nextBirthday = birthDate.withYear(today.year)
            if(nextBirthday.isBefore(today)) {
                nextBirthday = nextBirthday.plusYears(1)
            }
            return ChronoUnit.DAYS.between(today, nextBirthday).toInt()
        } catch (e: DateTimeException) {
            Log.e("Date error", "Invalid date: ${e.message}")
            return -1 //FIXME probably not the best approach
        }

    }

    fun calculateBirthDate(person: Person): String{
        return LocalDate.of(person.birthYear, person.birthMonth, person.birthDayOfMonth).toString()
    }

    fun calculateAge(year: Int, month: Int, day: Int): Int {
        try {
            val currentDate = LocalDate.now()
            val birthDay = LocalDate.of(year, month, day)
            val period = Period.between(birthDay, currentDate)
            return period.years
        } catch (e:DateTimeException) {
            Log.e("Date error", "Invalid date: ${e.message}")
            return -1
        }
    }
}