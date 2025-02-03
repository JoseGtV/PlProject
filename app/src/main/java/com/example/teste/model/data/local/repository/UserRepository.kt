package com.example.teste.model.data.local.repository

import com.example.teste.model.data.local.appDataBase.AppDataBase
import com.example.teste.model.data.local.dao.UserDAO
import com.example.teste.model.data.local.entity.User
import com.example.teste.utils.checkPassword
import com.example.teste.utils.hashPassword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val userDAO: UserDAO) {
    private lateinit var db: AppDataBase

    //Registra um novo usuario
    suspend fun registerUser(userName: String, password: String): String? {
        if (userName.isBlank() || password.isBlank()) {
            return "Os campos nao podem estar vazios!"
        }

        val hashedPassword = hashPassword(password)
        val newUser =
            User(
                userName = userName,
                password = password,
                passWordHash = hashedPassword
            )
        return withContext(Dispatchers.IO) {
            val isTaken = userDAO.isUserNameTaken(userName)
            if (isTaken) {
                return@withContext "Nome de Usuário em uso!"
            }
            userDAO.insertUser(newUser)
            "Usuario cadastrado com sucesso!"
        }

    }

    //Valida o Login
    suspend fun validadeLogin(userName: String, password: String): String {
        return withContext(Dispatchers.IO) {
            val user = userDAO.getUserbyName(userName)
            if (user != null && checkPassword(password, user.passWordHash)) {
                return@withContext "Login bem-sucedido!"
            } else {
                return@withContext "Nome de usuário ou senha inválidos"
            }
        }
    }
}

