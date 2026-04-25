package dev.mirosh.topusers.domain.usecase

import dev.mirosh.topusers.domain.model.User
import dev.mirosh.topusers.domain.repository.UserRepository
import dev.mirosh.topusers.domain.model.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<Result<List<User>>> = repository.observeTopUsers()
}
