package dev.mirosh.topusers.domain.usecase

import dev.mirosh.topusers.domain.model.User
import dev.mirosh.topusers.domain.repository.StackExchangeRepository
import dev.mirosh.topusers.domain.model.Result

class GetTopUsersUseCase(
    private val repository: StackExchangeRepository
) {
    suspend operator fun invoke(): Result<List<User>> = repository.getTopUsers()
}
