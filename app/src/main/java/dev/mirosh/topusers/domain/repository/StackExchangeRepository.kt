package dev.mirosh.topusers.domain.repository

import dev.mirosh.topusers.data.model.UserDTO

interface StackExchangeRepository {
      suspend fun getTopUsers(): List<UserDTO>
  }