package dev.mirosh.topusers.ui.model

import androidx.compose.runtime.Immutable
import dev.mirosh.topusers.domain.model.User

@Immutable
data class UserUiModel(
    val id: Long,
    val displayName: String,
    val reputation: Int,
    val profileImage: String,
    val following: Boolean = false
) {
    companion object {
        fun fromUser(user: User) =
            UserUiModel(
                id = user.id,
                displayName =  user.displayName,
                reputation = user.reputation,
                profileImage =  user.profileImage
            )


            

    }
}