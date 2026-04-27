package dev.mirosh.topusers.ui.model

import androidx.compose.runtime.Immutable
import dev.mirosh.topusers.domain.model.User

@Immutable
data class UserUiModel(
    val id: Long,
    val displayName: String = "",
    val reputation: String = "",
    val profileImage: String = "",
    val following: Boolean = false
) {
    companion object {
        fun fromUser(user: User) =
            UserUiModel(
                id = user.id,
                displayName = user.displayName,
                //not handling any corner cases here since this app fetches only 20 first users
                // and they have > 1000 in reputation
                reputation = "${user.reputation / 1000}k",
                profileImage = user.profileImage,
                following = user.following
            )
    }
}