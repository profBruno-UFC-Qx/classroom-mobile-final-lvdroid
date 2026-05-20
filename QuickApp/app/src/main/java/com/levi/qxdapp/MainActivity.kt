package com.levi.qxdapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.levi.qxdapp.presentation.auth.ForgotPasswordScreen
import com.levi.qxdapp.presentation.auth.LoginScreen
import com.levi.qxdapp.presentation.auth.RegisterScreen
import com.levi.qxdapp.presentation.navigation.MainScreen
import com.levi.qxdapp.ui.theme.QxdAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QxdAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("login") {
                            LoginScreen(
                                onLoginClick = {
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onForgotPasswordClick = {
                                    navController.navigate("forgot_password")
                                },
                                onRegisterClick = {
                                    navController.navigate("register")
                                }
                            )
                        }
                        composable("forgot_password") {
                            ForgotPasswordScreen(
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable("register") {
                            RegisterScreen(
                                onLoginClick = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable("home") {
                            MainScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QxdAppTheme {
        Greeting("Android")
    }
}
