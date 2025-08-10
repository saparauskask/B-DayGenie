package com.example.b_daygenie.model

data class Person(
    val id: Int,
    var userId: String?,
    var name: String?,
    var birthYear: Int,
    var birthMonth: Int,
    var birthDayOfMonth: Int,
    var remarks: String?,
    val pictureUrl: String?,
    val age: Int
)
