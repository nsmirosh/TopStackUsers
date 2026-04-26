package dev.mirosh.topusers.data.model

import dev.mirosh.topusers.domain.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    // I want to get an error in case the user_id is null. Since we won't be able to do anything with it
    @SerialName("user_id") val id: Long,
    @SerialName("display_name") val displayName: String? = null,
    @SerialName("profile_image") val profileImage: String? = null,
    val reputation: Int = 0,
) {
    fun toDomain() = User(
        id = id,
        displayName = displayName.orEmpty(),
        profileImage = profileImage.orEmpty(),
        reputation = reputation,
    )
}
