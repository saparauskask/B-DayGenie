package com.example.b_daygenie.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.b_daygenie.R
import com.example.b_daygenie.model.Person
import com.example.b_daygenie.utils.DateUtils
import com.example.b_daygenie.utils.InputUtils


private var mainModifier = Modifier
        .fillMaxSize()
        .padding(10.dp)

    private var textFieldModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 15.dp)
        .padding(bottom = 10.dp)



    @Composable
    fun EditProfileScreen(
        uid: String?,
        navController: NavController,
        modifier: Modifier = Modifier, person: Person? = null,
        onAddPerson: (Person) -> Unit = { },
        onUpdatePerson: (Person) -> Unit = { }
    ) {
        val userIdTextField = remember { mutableStateOf(uid.toString()) }
        val nameTextField = remember { mutableStateOf("") }
        val birthdayTextField = remember { mutableStateOf("") }
        val remarksTextField = remember { mutableStateOf("") }
        val showInvalidInputDialog = remember { mutableStateOf(false) }
        val invalidInputErrorMessage = remember { mutableStateOf("") }

        Scaffold(
            topBar = { ScreenToolbarEditProfile(navController, modifier) }
        ){ paddingValues ->

            Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                if (showInvalidInputDialog.value) {
                    InvalidInputDialog(invalidInputErrorMessage, showInvalidInputDialog)
                }
                Column(Modifier.verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) {
                    if(person != null) {
                        UpdatePerson(
                            onUpdatePerson,
                            person,
                            navController,
                            userIdTextField,
                            nameTextField,
                            birthdayTextField,
                            remarksTextField,
                            showInvalidInputDialog,
                            invalidInputErrorMessage
                        )
                    } else {
                        AddNewPerson(
                            onAddPerson,
                            navController,
                            userIdTextField,
                            nameTextField,
                            birthdayTextField,
                            remarksTextField,
                            showInvalidInputDialog,
                            invalidInputErrorMessage
                        )
                    }
                }
            }
        }
    }

@Composable
private fun InvalidInputDialog(errorMessage: MutableState<String>, showInvalidInputDialog: MutableState<Boolean>) {
    Dialog(onDismissRequest = { showInvalidInputDialog.value = false }) {
        Card(
            Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(16.dp)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = errorMessage.value
                )
                TextButton(onClick = { showInvalidInputDialog.value = false }) {
                    Text("Dismiss")
                }
            }
        }
    }
}

@Composable
private fun AddNewPerson(
    onAddPerson: (Person) -> Unit,
    navController: NavController,
    userIdTextField: MutableState<String>,
    nameTextField: MutableState<String>,
    birthdayTextField: MutableState<String>,
    remarksTextField: MutableState<String>,
    showInvalidInputDialog: MutableState<Boolean>,
    invalidInputErrorMessage: MutableState<String>
) {
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
        SaveButton(onAddPerson, navController, userIdTextField, nameTextField, birthdayTextField, remarksTextField, showInvalidInputDialog, invalidInputErrorMessage)
    }
    Spacer(Modifier.size(25.dp))
    Box(
        Modifier
            .fillMaxWidth()
            .size(150.dp),
        contentAlignment = Alignment.Center
    ) {
        ProfilePictureEdit()
    }
    Text("Add Picture", textDecoration = TextDecoration.Underline)
    Spacer(Modifier.size(50.dp))
    TitleText(nameTextField)
    Spacer(Modifier.size(50.dp))
    UserIdTextField(userIdTextField, textFieldModifier)
    NameTextField(nameTextField, textFieldModifier)
    BirthdayTextField(birthdayTextField, textFieldModifier)
    RemarksTextField(remarksTextField, textFieldModifier)
}

@Composable
fun UpdatePerson(
    onUpdatePerson: (Person) -> Unit,
    person: Person,
    navController: NavController,
    userIdTextField: MutableState<String>,
    nameTextField: MutableState<String>,
    birthdayTextField: MutableState<String>,
    remarksTextField: MutableState<String>,
    showInvalidInputDialog: MutableState<Boolean>,
    invalidInputErrorMessage: MutableState<String>
) {
    userIdTextField.value = person.userId.toString()
    nameTextField.value = person.name.toString()
    birthdayTextField.value = DateUtils.calculateBirthDate(person)
    remarksTextField.value = person.remarks.toString()

    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
        UpdateButton(
            onUpdatePerson,
            navController,
            userIdTextField,
            nameTextField,
            birthdayTextField,
            remarksTextField,
            person,
            showInvalidInputDialog,
            invalidInputErrorMessage
        )
    }
    Spacer(Modifier.size(25.dp))
    Box(
        Modifier
            .fillMaxWidth()
            .size(150.dp),
        contentAlignment = Alignment.Center
    ) {
        ProfilePictureEdit()
    }
    Text("Add Picture", textDecoration = TextDecoration.Underline)
    Spacer(Modifier.size(50.dp))
    TitleText(nameTextField)
    Spacer(Modifier.size(50.dp))
    UserIdTextField(userIdTextField, textFieldModifier)
    NameTextField(nameTextField, textFieldModifier)
    BirthdayTextField(birthdayTextField, textFieldModifier)
    RemarksTextField(remarksTextField, textFieldModifier)
}

@OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ScreenToolbarEditProfile(navController: NavController, modifier: Modifier = Modifier) {
        TopAppBar(
            title = {
                    Text(
                        "Edit Profile",
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
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color(0xFF2A9D8F)
            )
        )
    }

    @Composable
    fun SaveButton(onAddPerson: (Person) -> Unit,
                   navController: NavController,
                   userIdTextField: MutableState<String>,
                   nameTextField: MutableState<String>,
                   birthdayTextField: MutableState<String>,
                   remarksTextField: MutableState<String>,
                   showInvalidInputDialog: MutableState<Boolean>,
                   invalidInputErrorMessage: MutableState<String>
    ) {
        Button(
            onClick = {
                val errorMessage = InputUtils.checkEditProfileForm(nameTextField.value, birthdayTextField.value)
                if(errorMessage.isEmpty()) {
                    val (year, month, day) = birthdayTextField.value.split("-")
                    val person: Person = Person(
                        0,
                        userIdTextField.value,
                        nameTextField.value,
                        year.toInt(),
                        month.toInt(),
                        day.toInt(),
                        remarksTextField.value,
                        null,
                        age = DateUtils.calculateAge(year.toInt(), month.toInt(), day.toInt())
                    )
                    onAddPerson(person)
                    navController.popBackStack()
                } else {
                    invalidInputErrorMessage.value = errorMessage
                    showInvalidInputDialog.value = true
                }
            },
            modifier = Modifier.padding(end = 5.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonColors(
                containerColor = Color(0xFF264653),
                contentColor = Color.White,
                disabledContainerColor = Color(0xFF264653),
                disabledContentColor = Color.White
            )
        ) {
            Text("Save")
        }
    }

@Composable
fun UpdateButton(onUpdatePerson: (Person) -> Unit,
                 navController: NavController,
                 userIdTextField: MutableState<String>,
                 nameTextField: MutableState<String>,
                 birthdayTextField: MutableState<String>,
                 remarksTextField: MutableState<String>,
                 person: Person,
                 showInvalidInputDialog: MutableState<Boolean>,
                 invalidInputErrorMessage: MutableState<String>
) {
    Button(
        onClick = {
            val errorMessage = InputUtils.checkEditProfileForm(nameTextField.value, birthdayTextField.value)
            if (errorMessage.isEmpty()) {
                person.userId = userIdTextField.value
                person.name = nameTextField.value
                val (year, month, day) = birthdayTextField.value.split("-")
                person.birthYear = year.toInt()
                person.birthMonth = month.toInt()
                person.birthDayOfMonth = day.toInt()
                person.remarks = remarksTextField.value
                onUpdatePerson(person)
                navController.popBackStack()
            } else {
                invalidInputErrorMessage.value = errorMessage
                showInvalidInputDialog.value = true
            }
        },
        modifier = Modifier.padding(end = 5.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonColors(
            containerColor = Color(0xFF264653),
            contentColor = Color.White,
            disabledContainerColor = Color(0xFF264653),
            disabledContentColor = Color.White
        )
    ) {
        Text("Save")
    }
}

    @Composable
    fun ProfilePictureEdit() {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Profile picture",
            modifier = Modifier
                .clip(CircleShape)
                .border(width = 3.dp, color = Color.DarkGray, CircleShape)
        )
    }

    @Composable
    fun TitleText(name: MutableState<String>) {
        Text(
            text = name.value,
            style = MaterialTheme.typography.headlineLarge
        )
    }

    @Composable
    fun UserIdTextField(userIdTextField: MutableState<String>, modifier: Modifier) {
        OutlinedTextField(
            value = userIdTextField.value,
            onValueChange = { userIdTextField.value = it },
            modifier = modifier,
            enabled = false,
            readOnly = true,
            label = { Text("User Id") }
        )
    }

    @Composable
    fun NameTextField(nameTextField: MutableState<String>, modifier: Modifier) {
        OutlinedTextField(
            value = nameTextField.value,
            onValueChange = { nameTextField.value = it },
            modifier = modifier,
            label = { Text("Full Name") },
            shape = RoundedCornerShape(16.dp)
        )
    }

    @Composable
    fun BirthdayTextField(birthdayTextField: MutableState<String>, modifier: Modifier) {
        OutlinedTextField(
            value = birthdayTextField.value,
            onValueChange = { birthdayTextField.value = it },
            modifier = modifier,
            label = { Text("Date of Birth") },
            placeholder = { Text("YYYY-MM-DD") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(16.dp)
        )
    }

    @Composable
    fun RemarksTextField(remarksTextField: MutableState<String>, modifier: Modifier) {
        OutlinedTextField(
            value = remarksTextField.value,
            onValueChange = { remarksTextField.value = it },
            modifier = modifier,
            label = { Text("Remarks") },
            minLines = 3,
            shape = RoundedCornerShape(16.dp)
        )
    }

    @Preview
    @Composable
    fun ScreenPreview() {
        val message = remember { mutableStateOf("Date format is not valid. It should be YYYY-MM-DD") }
        val show = remember { mutableStateOf(true) }
        InvalidInputDialog(message, show)
    }