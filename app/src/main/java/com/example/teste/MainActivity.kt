package com.example.teste

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.teste.model.data.local.appDataBase.AppDataBase
import com.example.teste.model.data.local.repository.UserRepository
import com.example.teste.ui.theme.TesteTheme
import com.example.teste.view.LoginScreen
import com.example.teste.viewmodel.UserViewModel
import com.example.teste.viewmodel.UserViewModelFactory


class MainActivity : ComponentActivity() {
    //inicializa o banco de dados
    private lateinit var db: AppDataBase

    //ViewModel
    private lateinit var viewModel: UserViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = AppDataBase.getInstance(this)
        val userDAO = db.userDao()
        val userRepository = UserRepository(userDAO)

        // Configurando o ViewModel com a Factory
        val factory = UserViewModelFactory(userRepository)
        viewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        viewModel.loginResult.observe(this){ message ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }

        viewModel.registerResult.observe(this){ message ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }


        enableEdgeToEdge()
        setContent {
            TesteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen(
                        onLogin = { userName, password ->
                        viewModel.login(userName, password)
                    }, onRegister = { userName, password ->
                        viewModel.registerUser(userName, password)
                    }

                    )
                }

            }
        }
    }


}



