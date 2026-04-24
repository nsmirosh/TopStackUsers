package dev.mirosh.topusers.domain.usecase

import dev.mirosh.topusers.domain.model.User
import dev.mirosh.topusers.domain.repository.UserRepository
import dev.mirosh.topusers.domain.model.Result
import javax.inject.Inject

class GetTopUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Result<List<User>> = repository.getTopUsers()
}
