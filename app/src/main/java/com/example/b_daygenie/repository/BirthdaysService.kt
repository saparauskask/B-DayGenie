package com.example.b_daygenie.repository

import com.example.b_daygenie.model.Person
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BirthdaysService {
    @GET("Persons")
    fun getAllPersons(): Call<List<Person>>

    @POST("Persons")
    fun createPerson(@Body person: Person): Call<Person>

    @DELETE("Persons/{id}")
    fun deletePerson(@Path("id") id: Int): Call<Person>

    @PUT("Persons/{id}")
    fun updatePerson(@Path("id") id: Int, @Body person: Person): Call<Person>
}