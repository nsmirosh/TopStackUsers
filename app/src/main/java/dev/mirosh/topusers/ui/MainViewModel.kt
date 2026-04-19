package dev.mirosh.topusers.ui

import android.util.Log
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    fun fetchUsers() {
        Log.d("MainViewModel", "fetchUsers called")
    }
}