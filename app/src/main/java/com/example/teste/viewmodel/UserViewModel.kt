package com.example.teste.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teste.model.data.local.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _loginResult = MutableLiveData<String>()
    val loginResult : LiveData<String> get() = _loginResult

    fun login(userName: String, password : String){
        viewModelScope.launch {
            val result = userRepository.validadeLogin(userName, password)
            _loginResult.postValue( "Erro desconhecido")
        }
    }
}