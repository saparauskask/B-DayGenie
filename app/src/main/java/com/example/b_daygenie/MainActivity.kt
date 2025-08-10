package com.example.b_daygenie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.b_daygenie.model.AuthViewModel
import com.example.b_daygenie.model.BirthdaysViewModel
import com.example.b_daygenie.screens.AuthScreen
import com.example.b_daygenie.screens.EditProfileScreen
import com.example.b_daygenie.screens.HomeScreen
import com.example.b_daygenie.screens.ViewProfileScreen
import com.example.b_daygenie.ui.theme.BDayGenieTheme
import com.example.b_daygenie.ui.theme.NavRoutes
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val authViewModel = AuthViewModel(application)
        val viewModel = BirthdaysViewModel(application) // created on onCreate, therefore they'll be recreated everytime the screen is rotated
        setContent {
            BDayGenieTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainContent(Modifier.padding(innerPadding), viewModel, authViewModel)
                }
            }
        }
    }
}

@Composable
fun MainContent(modifier: Modifier = Modifier, viewModel: BirthdaysViewModel, authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val birthdays = viewModel.birthdays.value
    val errorMessage = viewModel.errorMessage.value

    //val userState = authViewModel.getUserLiveData().observeAsState(initial = FirebaseAuth.getInstance().currentUser) // setting the initial value might be problematic because of several reasons
    val userState = authViewModel.getUserLiveData().observeAsState()
    val user = userState.value
    val uid = authViewModel.getUserLiveData().value?.uid

    LaunchedEffect(user) {
        println("userState value has changed")
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        if (user != null && currentRoute != NavRoutes.Home.route) {
            navController.navigate(NavRoutes.Home.route) {
                popUpTo(0)
            }
            println("UserId was set")
            viewModel.setUserUid()
            delay(2000)
            viewModel.getBirthdays()
        } else if (user == null && currentRoute != NavRoutes.Authorization.route) {
            navController.navigate(NavRoutes.Authorization.route) {
                popUpTo(0)
            }
            viewModel.removeUserUid()
            println("User is null — navigating to Authorization")
        }
    }

    NavHost(navController = navController, startDestination = NavRoutes.Authorization.route) {
        composable(route = NavRoutes.Authorization.route) {
            AuthScreen(
                navController,
                onLogin = { email, password -> authViewModel.login(email, password)},
                onRegister = {email, password, repeatPassword -> authViewModel.register(email, password, repeatPassword)}
            )
        }
        composable(route = NavRoutes.Home.route) {
            HomeScreen(
                navController = navController,
                birthdays = birthdays,
                onPersonSelected = { person -> navController.navigate(NavRoutes.ViewProfile.route + "/${person.id}")},
                onUpdateList = { viewModel.getBirthdays() },
                onSort = { ageSort, nameSort, birthdaySort -> viewModel.sortBirthdays(ageSort, nameSort, birthdaySort) },
                onFilter = { nameFilter, ageFilterStart, ageFilterEnd -> viewModel.filterBirthdays(nameFilter, ageFilterStart, ageFilterEnd) },
                errorMessage = errorMessage,
                onClearErrorMessage = { viewModel.clearErrorMessage() },
                onLogout = {
                    viewModel.logOut()
                    navController.navigate(NavRoutes.Authorization.route)
                }
            )
        }
        composable(NavRoutes.ViewProfile.route + "/{personId}", arguments = listOf(navArgument("personId") { type = NavType.IntType })) {
            backstackEntry ->
            val personId = backstackEntry.arguments?.getInt("personId")
            val person = birthdays.find { it.id == personId}
            ViewProfileScreen(
                navController = navController,
                person = person,
                onPersonSelected = { selectedPerson -> viewModel.remove(selectedPerson); viewModel.getBirthdays()},
                onPersonSelectedEdit = { selectedEdit -> navController.navigate(NavRoutes.EditProfile.route + "/${selectedEdit.id}")}
            )
        }
        composable(route = NavRoutes.EditProfile.route) {
            EditProfileScreen(
                uid = uid,
                navController,
                onAddPerson = { person ->
                    viewModel.add(person);
                    viewModel.getBirthdays()
                }
            )
        }
        composable(NavRoutes.EditProfile.route + "/{personId}", arguments = listOf(navArgument("personId") { type = NavType.IntType})) {
            backstackEntry ->
            val personId = backstackEntry.arguments?.getInt("personId")
            val person = birthdays.find { it.id == personId }
            EditProfileScreen(
                uid = uid,
                navController = navController,
                person = person,
                onUpdatePerson = { updatedPerson -> viewModel.update(updatedPerson); viewModel.getBirthdays()}
            )
        }
    }
}