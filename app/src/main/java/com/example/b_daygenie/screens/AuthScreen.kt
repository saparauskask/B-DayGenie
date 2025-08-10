package com.example.b_daygenie.screens

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

private val formColumnModifier: Modifier = Modifier
        .padding(20.dp)
        .fillMaxSize()


    @Composable
    fun AuthScreen(
        navController: NavController,
        onLogin: (email: String, password: String) -> Unit,
        onRegister: (email: String, password: String, repeatPassword: String) -> Unit
    ) {
        TabScreen(navController, onLogin, onRegister)
    }

    @Composable
    fun TabScreen(
        navController: NavController,
        onLogin: (email: String, password: String) -> Unit,
        onRegister: (email: String, password: String, repeatPassword: String) -> Unit
    ) {
        val tabIndex = remember { mutableIntStateOf(0) }
        val tabs = listOf("Log In", "Register")
        val emailTextField = remember { mutableStateOf("") }
        val passwordTextField = remember { mutableStateOf("") }
        AnimatedGradientBackground()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            TabRow(
                selectedTabIndex = tabIndex.intValue,
                containerColor = Color.Transparent
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title)},
                        selected = tabIndex.intValue == index,
                        onClick = { tabIndex.intValue = index}
                    )
                }
            }
        }
        when (tabIndex.intValue) {
            0 -> LoginForm(navController, emailTextField, passwordTextField, onLogin)
            1 -> RegisterForm(emailTextField, passwordTextField, onRegister)
        }
    }

    @Composable
    fun LoginForm(
        navController: NavController,
        emailTextField: MutableState<String>,
        passwordTextField: MutableState<String>,
        onLogin: (email: String, password: String) -> Unit
    ) {
        Column (
            formColumnModifier,
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EmailTextField("Email", emailTextField)
            PasswordTextField("Password", passwordTextField)
            ForgotPasswordTextButton()
        }
        Column (
            Modifier
                .padding(bottom = 50.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoginButton(navController, emailTextField, passwordTextField, onLogin)
        }
    }

    @Composable
    fun RegisterForm(
        emailTextField: MutableState<String>,
        passwordTextField: MutableState<String>,
        onRegister: (email: String, password: String, repeatPassword: String) -> Unit
    ) {
        val repeatPasswordTextField = remember { mutableStateOf("") }
        Column(
            formColumnModifier,
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EmailTextField("Email", emailTextField)
            PasswordTextField("Password", passwordTextField)
            PasswordTextField("Repeat Password", repeatPasswordTextField)
        }
        Column (
            Modifier
                .padding(bottom = 50.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RegisterButton(emailTextField, passwordTextField, repeatPasswordTextField, onRegister)
        }
    }

    @Composable
    private fun PasswordTextField(label:String, password: MutableState<String>) {
        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text(label) },
            leadingIcon = {
                Icon(Icons.Filled.Lock, contentDescription = "Username icon")
            },
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
    }

    @Composable
    private fun EmailTextField(label:String, email: MutableState<String>) {
        TextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text(label) },
            leadingIcon = {
                Icon(Icons.Filled.Email, contentDescription = "Email icon")
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
    }

    @Composable
    private fun LoginButton(
        navController: NavController,
        email: MutableState<String>,
        password: MutableState<String>,
        onLogin: (email: String, password: String) -> Unit
    ) {
        Button(
            onClick = { if(email.value.isNotEmpty() && password.value.isNotEmpty()) onLogin(email.value, password.value)},
            modifier = Modifier
                .width(200.dp),
            colors = ButtonColors(
                containerColor = Color(0xFF264653),
                contentColor = Color.White,
                disabledContainerColor = Color.White,
                disabledContentColor = Color(0xFF264653)
            )
        ) {
            Text("Log In")
        }
    }

    @Composable
    private fun RegisterButton(
        email: MutableState<String>,
        password: MutableState<String>,
        repeatPassword: MutableState<String>,
        onRegister: (email: String, password: String, repeatPassword: String) -> Unit
    ) {
        Button(
            onClick = { if(email.value.isNotEmpty() && password.value.isNotEmpty() && repeatPassword.value.isNotEmpty()) onRegister(email.value, password.value, repeatPassword.value)},
            modifier = Modifier
                .width(250.dp),
            colors = ButtonColors(
                containerColor = Color(0xFF264653),
                contentColor = Color.White,
                disabledContainerColor = Color.White,
                disabledContentColor = Color(0xFF264653)
            )
        ) {
            Text("Create a New Account")
        }
    }

@Composable
private fun ForgotPasswordTextButton() {
    TextButton(onClick = { }) {
        Text("Forgot Password?")
    }
}

@Composable
fun AnimatedGradientBackground() {
    val infiniteTransition = rememberInfiniteTransition()

    val color1 by infiniteTransition.animateColor(
        initialValue = Color(0xFF2A9D8F),     // Teal (same as toolbar)
        targetValue = Color(0xFFFFD6A5),      // Soft peach
        animationSpec = infiniteRepeatable(
            animation = tween(4000),
            repeatMode = RepeatMode.Reverse
        )
    )

    val color2 by infiniteTransition.animateColor(
        initialValue = Color(0xFFFFA69E),     // Light coral
        targetValue = Color(0xFF2A9D8F),      // Teal again
        animationSpec = infiniteRepeatable(
            animation = tween(4000),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(color1, color2),
                    start = Offset.Zero,
                    end = Offset.Infinite
                )
            )
    )
}


    @Preview(showBackground = true)
    @Composable
    fun ComponentPreview() {
        //LoginForm()
        //Authorization()
    }