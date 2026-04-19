package dev.mirosh.topusers.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mirosh.topusers.data.StackExchangeApi
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val stackExchangeApi: StackExchangeApi
) : ViewModel() {

    fun fetchUsers() {
        viewModelScope.launch {
            try {
                val response = stackExchangeApi.getUsers()
                Log.d("MainViewModel", "response = $response")
            } catch (e: Exception) {
                Log.e("MainViewModel", "${e.message}")
            }
        }
    }
}