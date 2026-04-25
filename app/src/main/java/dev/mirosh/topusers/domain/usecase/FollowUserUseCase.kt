package dev.mirosh.topusers.domain.usecase

import dev.mirosh.topusers.domain.model.Result
import dev.mirosh.topusers.domain.repository.UserRepository
import javax.inject.Inject

class FollowUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userId: Long): Result<Unit> = repository.followUser(userId)
}
