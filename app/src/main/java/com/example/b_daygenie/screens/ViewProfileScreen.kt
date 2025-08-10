package com.example.b_daygenie.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.b_daygenie.R
import com.example.b_daygenie.model.Person
import com.example.b_daygenie.utils.DateUtils


private var mainModifier = Modifier
        .fillMaxSize()
        .padding(10.dp)

    @Composable
    fun ViewProfileScreen(navController: NavController,
                          person: Person?,
                          modifier: Modifier = Modifier,
                          onPersonSelected: (person: Person) -> Unit = {},
                          onPersonSelectedEdit: (person: Person) -> Unit
    ) {
        val openDeleteDialog = remember { mutableStateOf(false) }
        Scaffold (
            topBar = { ScreenToolbarViewProfile(navController, openDeleteDialog, onPersonSelectedEdit, person) }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                if(person != null) {
                    DeleteDialog(openDeleteDialog, onPersonSelected, person, navController)
                }
                Column(Modifier.verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) { //modifier, horizontalAlignment = Alignment.CenterHorizontally
                    Spacer(Modifier.size(50.dp))
                    Box(
                        Modifier.fillMaxWidth().size(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ProfilePicture()
                    }
                    if (person != null) {
                        TitleText(person.name.toString())
                        Spacer(Modifier.size(30.dp))
                        NextBirthdayText(person)
                        PersonDetails(person)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ScreenToolbarViewProfile(
        navController: NavController,
        showDeleteDialog: MutableState<Boolean>,
        onPersonSelectedEdit: (person: Person) -> Unit,
        person: Person?
    ) {
        TopAppBar(
            title = {
                Text(
                    "Profile",
                    color = Color(0xFFd9b9ac),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back button")
                }
            },
            actions = {
                IconButton(onClick = { showDeleteDialog.value = true }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete profile")
                }
                IconButton(
                    onClick = {
                        if(person != null) {
                            onPersonSelectedEdit(person)
                        }
                    }) {
                    Icon(Icons.Filled.Create, contentDescription = "Edit profile")
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color(0xFF2A9D8F)
            )
        )
    }

    @Composable
    fun ProfilePicture() {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Profile picture",
            modifier = Modifier
                .size(175.dp)
                .clip(CircleShape)
                .border(width = 3.dp, color = Color.DarkGray, CircleShape)
        )
    }

    @Composable
    fun TitleText(name: String) {
        Text(
            text = name,
            color = Color.DarkGray,
            fontWeight = FontWeight(500),
            style = MaterialTheme.typography.headlineLarge,
            fontSize = 48.sp
        )
    }

    @Composable
    fun NextBirthdayText(person: Person?) {
        var daysToBirthday = "X"
        if(person != null) {
            daysToBirthday = DateUtils.calculateNextBirthday(person).toString()
        }
        Text(
            "Next Birthday In:",
            color = Color(0xFF264653),
            style = MaterialTheme.typography.headlineMedium
        )
        Row {
            Text(
                text = daysToBirthday,
                color = Color(0xFF264653),
                fontWeight = FontWeight(500),
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 45.sp
            )
            Text(
                " Days",
                color = Color(0xFF264653),
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }

@Composable
fun DeleteDialog(showDialog: MutableState<Boolean>,
                 onPersonSelected: (person: Person) -> Unit,
                 person: Person,
                 navController: NavController
) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = {showDialog.value = false}, //onDismiss,
            title = { Text("Confirm Action") },
            text = { Text("Are you sure you want to delete this item?") },
            confirmButton = {
                TextButton(onClick = {onPersonSelected(person); navController.popBackStack()}) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = {showDialog.value = false} ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun PersonDetails(person: Person) {
    val birthDate = DateUtils.calculateBirthDate(person)
    Box(
        Modifier
            .fillMaxSize()
            .padding(10.dp)
            .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(16.dp))
            .background(Color.LightGray, shape = RoundedCornerShape(16.dp))
    ) {
        Column(
            Modifier
                .padding(5.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Details",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(Modifier.size(30.dp))
            PersonDetailsEntry("Full Name:", person.name.toString())
            PersonDetailsEntry("User ID:", person.userId.toString())
            PersonDetailsEntry("Birth Date:", birthDate)
            PersonDetailsEntry("Current Age:", person.age.toString())
            PersonDetailsEntry("Remarks:", person.remarks.toString())
        }
    }
}

@Composable
fun PersonDetailsEntry(description: String, value: String) {
    Row {
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
        Text("   ")
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

    @Preview
    @Composable
    fun PreviewScreen() {
        val person: Person = Person(0, "sample.longemail@securemail.com", "First Middle Last", 1995, 10, 25, remarks = null, pictureUrl = null, age = 0)
        val showDialog = remember { mutableStateOf(true) }
        val navController = rememberNavController()
        ViewProfileScreen(navController, person, onPersonSelectedEdit = {})
    }