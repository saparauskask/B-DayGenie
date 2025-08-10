package com.example.b_daygenie.model

import android.app.Application
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.b_daygenie.repository.AuthRepository
import com.example.b_daygenie.repository.BirthdaysRepository
import com.example.b_daygenie.utils.SortState
import com.google.firebase.auth.FirebaseUser

class BirthdaysViewModel(application: Application): AndroidViewModel(application) {
    private val authRepository: AuthRepository = AuthRepository(application)
    private val userLiveData: MutableLiveData<FirebaseUser?> = authRepository.getUserLiveData()
    private val loggedOutLiveData: MutableLiveData<Boolean> = authRepository.getLoggedOutLiveData()
    private val repository = BirthdaysRepository()
    val birthdays: State<List<Person>> = repository.birthdays
    val errorMessage: State<String> = repository.errorMessage
    val isLoadingBirthdays: State<Boolean> = repository.isLoadingBirthdays

    fun logOut() {
        authRepository.logout()
        repository.userUid = ""
    }

    private fun getUserLiveData(): MutableLiveData<FirebaseUser?> {
        return userLiveData
    }

    fun getLoggedOutLiveData(): MutableLiveData<Boolean> {
        return loggedOutLiveData
    }

    fun getBirthdays() {
        val uid = getUserLiveData().value?.uid
        if (!uid.isNullOrEmpty()) repository.getBirthdays(uid)
    }

    fun add(person: Person) {
        repository.add(person)
    }

    fun remove(person: Person) {
        repository.delete(person)
    }

    fun update(person: Person) {
        repository.update(person)
    }

    fun sortBirthdays(ageSortState: MutableState<SortState>, nameSortState: MutableState<SortState>, birthdaySortState: MutableState<SortState>) {
        repository.sortBirthdays(ageSortState, nameSortState, birthdaySortState)
    }

    fun filterBirthdays(filterByName: MutableState<String>, filterByAgeStart: MutableFloatState, filterByAgeEnd: MutableFloatState) {
        repository.filterBirthdays(filterByName, filterByAgeStart, filterByAgeEnd)
    }

    fun clearErrorMessage() {
        repository.clearErrorMessage()
    }

    fun setUserUid() {
        val uid = getUserLiveData().value?.uid
        if (!uid.isNullOrEmpty()) repository.userUid = uid
    }

    fun removeUserUid() {
        repository.userUid = ""
    }
}