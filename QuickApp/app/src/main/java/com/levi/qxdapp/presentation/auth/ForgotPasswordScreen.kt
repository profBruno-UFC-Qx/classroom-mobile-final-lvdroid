package com.levi.qxdapp.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top bar area with back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Voltar",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBackClick() },
                tint = Color(0xFF333333)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Recuperar Senha",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )
        }

        Text(
            text = "Redefina sua senha",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A),
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Insira o seu e-mail cadastrado e escolha uma nova senha para acessar sua conta.",
            fontSize = 14.sp,
            color = Color(0xFF666666),
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Email Field
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "E-mail",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Digite seu e-mail", color = Color(0xFFAAAAAA)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF1A1A1A),
                    unfocusedTextColor = Color(0xFF1A1A1A),
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = Color(0xFF1565C0)
                ),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // New Password Field
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Nova Senha",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                placeholder = { Text("Digite sua nova senha", color = Color(0xFFAAAAAA)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF1A1A1A),
                    unfocusedTextColor = Color(0xFF1A1A1A),
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = Color(0xFF1565C0)
                ),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password Field
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Confirmar Nova Senha",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = { Text("Confirme sua nova senha", color = Color(0xFFAAAAAA)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF1A1A1A),
                    unfocusedTextColor = Color(0xFF1A1A1A),
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = Color(0xFF1565C0)
                ),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Redefinir Senha Button
        Button(
            onClick = { /* Handle reset password */
                // Navigate back usually or show success
                onBackClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
            enabled = email.isNotBlank() && newPassword.isNotBlank() && confirmPassword.isNotBlank() && newPassword == confirmPassword
        ) {
            Text(
                text = "Salvar Nova Senha",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
