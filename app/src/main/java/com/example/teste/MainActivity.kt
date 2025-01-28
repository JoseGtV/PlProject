package com.example.teste

import android.graphics.drawable.shapes.Shape
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teste.data.local.appDataBase.AppDataBase
import com.example.teste.data.local.entity.User
import com.example.teste.ui.theme.TesteTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mindrot.jbcrypt.BCrypt


class MainActivity : ComponentActivity() {
    private lateinit var db: AppDataBase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = AppDataBase.getInstance(this)
        enableEdgeToEdge()
        setContent {
            TesteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen(onLogin = { userName, password ->
                        validadeLogin(userName, password)
                    }, onRegister = { userName, password ->
                        registerUser(userName, password)
                    }

                    )
                }

            }
        }
        showAllUsers()
    }

    private fun validadeLogin(userName: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = db.userDao().getUserbyName(userName)
            withContext(Dispatchers.Main) {
                if (user != null && checkPassword(password, user.passWordHash)) {
                    Toast.makeText(this@MainActivity, "Login bem-sucedido", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(
                        this@MainActivity, "Usuário ou senha inválidos", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    private fun registerUser(userName: String, password: String) {

        if(userName.isBlank() || password.isBlank()){
            Toast.makeText(
                this@MainActivity,"Os campos nao podem estar vazios!",
                Toast.LENGTH_LONG
            ).show()
        }
        val hashedPassword = hashPassword(password)
        val newUser =
            User(
                userName = userName,
                password = password,
                passWordHash = hashedPassword)
        CoroutineScope(Dispatchers.IO).launch {
            val isTaken = db.userDao().isUserNameTaken(userName)

            if (isTaken) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MainActivity, "Nome de Usuario já em uso", Toast.LENGTH_LONG
                    ).show()
                }
                return@launch
            }

            db.userDao().insertUser(newUser)
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    this@MainActivity, "Usuário cadastrado com sucesso!", Toast.LENGTH_LONG
                ).show()

            }
        }

    }

    private fun showAllUsers(){
        CoroutineScope(Dispatchers.IO).launch {
            val users = db.userDao().getAllUsers()
            users.forEach{user ->
                println("Usuario: ${user.userName}, Senha: ${user.password}")
            }
        }
    }

}


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
                onLogin(userName, password) }) {
                Text(text = "Enter")
            }
            Button(onClick = {
                print("Botão registrar clicado")
                onRegister(userName, password) }) {
                Text(text = "Register")
            }
        }
    }
}


fun hashPassword(password: String): String {
    return BCrypt.hashpw(password, BCrypt.gensalt())
}

fun checkPassword(password: String, hashed: String): Boolean {
    return BCrypt.checkpw(password, hashed)
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
