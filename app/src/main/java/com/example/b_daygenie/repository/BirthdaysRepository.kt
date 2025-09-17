package com.example.b_daygenie.repository

import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.b_daygenie.model.Person
import com.example.b_daygenie.utils.DateUtils
import com.example.b_daygenie.utils.SortState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory

class BirthdaysRepository {
    private val baseUrl = "http://10.0.2.2:8000/api/"

    private val birthdaysService: BirthdaysService
    val birthdays: MutableState<List<Person>> = mutableStateOf(listOf())
    val isLoadingBirthdays = mutableStateOf(false)
    val errorMessage = mutableStateOf("")
    var userUid: String = ""

    init {
        val build: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        birthdaysService = build.create(BirthdaysService::class.java)
        //getBirthdays(userUid)
    }

    fun getBirthdays(userUid: String) {
        isLoadingBirthdays.value = true
        birthdaysService.getAllPersons().enqueue(object : Callback<List<Person>> {
            override fun onResponse(call: Call<List<Person>>, response: Response<List<Person>>) {
                isLoadingBirthdays.value = false
                if (response.isSuccessful) {
                    //Log.d("APPLE", response.body().toString())
                    val personsList: List<Person>? = response.body()
                    birthdays.value = personsList ?: emptyList()
                    birthdays.value = personsList?.filter { it.userId == userUid } ?: emptyList()
                    errorMessage.value = ""
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessage.value = message
                }
            }
            override fun onFailure(call: Call<List<Person>>, t: Throwable) {
                isLoadingBirthdays.value = false
                val message = t.message ?: "No connection to back-end"
                errorMessage.value = message
            }
        })
    }

    fun add(person: Person) {
        birthdaysService.createPerson(person).enqueue(object: Callback<Person> {
            override fun onResponse(call: Call<Person>, response: Response<Person>) {
                if (response.isSuccessful) {
                    getBirthdays(userUid)
                    errorMessage.value = ""
                } else {
                    errorMessage.value = response.code().toString() + " " + response.message()
                }
            }

            override fun onFailure(call: Call<Person>, t: Throwable) {
                val message = t.message ?: "No connection to back-end"
                errorMessage.value = message
            }
        })
    }

    fun delete(person: Person) {
        val id = person.id

        birthdaysService.deletePerson(id).enqueue(object : Callback<Person> {
            override fun onResponse(call: Call<Person>, response: Response<Person>) {
                if(response.isSuccessful) {
                    errorMessage.value = ""
                    getBirthdays(userUid)
                } else {
                    errorMessage.value = response.code().toString() + " " + response.message()
                }
            }

            override fun onFailure(call: Call<Person>, t: Throwable) {
                val message = t.message ?: "No connection to back-end"
                errorMessage.value = message
            }
        })
    }

    fun update(person: Person) {
        val id = person.id

        birthdaysService.updatePerson(id, person).enqueue(object : Callback<Person> {
            override fun onResponse(call: Call<Person>, response: Response<Person>) {
                if(response.isSuccessful) {
                    errorMessage.value = ""
                    getBirthdays(userUid)
                } else {
                    errorMessage.value = response.code().toString() + " " + response.message()
                }
            }

            override fun onFailure(call: Call<Person>, t: Throwable) {
                val message = t.message ?: "No connection to back-end"
                errorMessage.value = message
            }
        })
    }

    fun clearBirthdays() {
        birthdays.value = listOf()
    }

    private fun sortBirthdaysByAge(list: List<Person>, ascending: Boolean): List<Person> {
        return if(ascending) list.sortedBy { it.age } else list.sortedByDescending { it.age }
    }

    private fun sortBirthdaysByName(list: List<Person>, ascending: Boolean): List<Person> {
        return if(ascending) list.sortedBy { it.name } else list.sortedByDescending { it.name }
    }

    private fun sortBirthdaysByBirthday(list: List<Person>, ascending: Boolean): List<Person> {
        return if(ascending) list.sortedBy { DateUtils.calculateNextBirthday(it) } else list.sortedByDescending { DateUtils.calculateNextBirthday(it) }
    }

    fun sortBirthdays(ageSortState: MutableState<SortState>, nameSortState: MutableState<SortState>, birthdaySortState: MutableState<SortState>) {
        var sortedList: List<Person> = birthdays.value
        if(ageSortState.value != SortState.NONE) {
            if(ageSortState.value == SortState.ASCENDING) sortedList = sortBirthdaysByAge(sortedList, true) else sortBirthdaysByAge(sortedList, false)
        }
        if(nameSortState.value != SortState.NONE) {
            sortedList =
                if(nameSortState.value == SortState.ASCENDING) sortBirthdaysByName(sortedList, true) else sortBirthdaysByName(sortedList, false)
        }
        if(birthdaySortState.value != SortState.NONE) {
            sortedList = if(birthdaySortState.value == SortState.ASCENDING) sortBirthdaysByBirthday(sortedList, true) else sortBirthdaysByBirthday(sortedList, false)
        }
        birthdays.value = sortedList
    }

    private fun filterBirthdaysByName(list: List<Person>, filterByName: String): List<Person> {
        return if(filterByName.isNotEmpty()) {
            list.filter { it.name?.lowercase()?.contains(filterByName.lowercase()) ?: false }
        } else {
            list
        }
    }

    private fun filterBirthdaysByAge(list: List<Person>, filterByAgeStart: MutableFloatState, filterByAgeEnd: MutableFloatState): List<Person> {
        if(filterByAgeStart.floatValue != 0f || filterByAgeEnd.floatValue != 100f) {
            val range = filterByAgeStart.floatValue.toInt()..filterByAgeEnd.floatValue.toInt()
            return list.filter { it.age in range }
        } else {
            return list
        }
    }

    fun filterBirthdays(filterByName: MutableState<String>, filterByAgeStart: MutableFloatState, filterByAgeEnd: MutableFloatState) {
        var filteredList = birthdays.value
        filteredList = filterBirthdaysByName(filteredList, filterByName.value)
        filteredList = filterBirthdaysByAge(filteredList, filterByAgeStart, filterByAgeEnd)
        birthdays.value = filteredList
    }

    fun clearErrorMessage() {
        errorMessage.value = ""
    }
}