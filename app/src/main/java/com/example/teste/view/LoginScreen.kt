package com.example.teste.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.PlProject.R

//Passar para a pasta View
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLogin: (String, String) -> Unit = { _, _ -> },
    onRegister: (String, String) -> Unit = { _, _ -> }
) {
    Box {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            var userName by remember {
                mutableStateOf("")
            }
            var password by remember {
                mutableStateOf("")
            }

            Icon(
                modifier = Modifier.padding(bottom = 4.dp),
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = null,
            )

            TextField(modifier = Modifier
                .background(Color.Transparent)
                .padding(4.dp),
                value = userName,
                shape = RoundedCornerShape(12.dp),
                onValueChange = { newValue -> userName = newValue },
                label = { Text(text = "User") })

            TextField(modifier = Modifier
                .background(Color.Transparent)
                .padding(4.dp),
                shape = RoundedCornerShape(12.dp),
                value = password,
                visualTransformation = PasswordVisualTransformation(),
                onValueChange = { newValue -> password = newValue },
                label = { Text(text = "Password") })

            Button(onClick = {
                print("Botao Entrar clicado")
                onLogin(userName, password)
            }) {
                Text(text = "Enter")
            }
            Button(onClick = {
                print("Botão registrar clicado")
                onRegister(userName, password)
            }) {
                Text(text = "Register")
            }
        }
    }
}


@Preview
@Composable
private fun LoginScreenPreview() {
    LoginScreen(onLogin = { userName, password ->
        userName; password
    }, onRegister = { userName, password ->
        userName; password
    })


}
