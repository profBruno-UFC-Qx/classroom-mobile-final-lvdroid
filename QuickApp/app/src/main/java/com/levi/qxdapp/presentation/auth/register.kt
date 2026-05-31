package com.levi.qxdapp.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import com.levi.qxdapp.data.local.UserProfileManager
import com.levi.qxdapp.R

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onRegisterSuccess: (String) -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icons container
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(Color(0xFFF0F4FF))
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.agua),
                contentDescription = "Water Jug",
                modifier = Modifier.size(32.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.gas),
                contentDescription = "Gas Cylinder",
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Title
        Text(
            text = "QuickQXD",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Subtitle
        Text(
            text = "Água e gás na sua porta, rápido.",
            fontSize = 14.sp,
            color = Color(0xFF666666)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Name Field
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Nome completo",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("Digite seu nome", color = Color(0xFFAAAAAA)) },
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


        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Senha",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Digite sua senha", color = Color(0xFFAAAAAA)) },
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

        Button(
            onClick = {
                if (name.isBlank() || email.isBlank() || password.isBlank()) {
                    Toast.makeText(context, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
                } else if (!email.contains("@")) {
                    Toast.makeText(context, "E-mail inválido", Toast.LENGTH_SHORT).show()
                } else if (password.length < 4) {
                    Toast.makeText(context, "A senha deve ter pelo menos 4 caracteres", Toast.LENGTH_SHORT).show()
                } else {
                    UserProfileManager.registerUser(context, name, email, password)
                    Toast.makeText(context, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                    onRegisterSuccess(email)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
        ) {
            Text(
                text = "Cadastrar",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Already have account
        Text(
            text = "Já tem uma conta?",
            fontSize = 14.sp,
            color = Color(0xFF666666)
        )
        
        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Fazer login",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1565C0),
            modifier = Modifier.clickable { onLoginClick() }
        )
    }
}
