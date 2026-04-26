package dev.mirosh.topusers.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import dev.mirosh.topusers.ui.model.UserUiModel
import dev.mirosh.topusers.ui.model.UsersList

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    val users by viewModel.users.collectAsStateWithLifecycle()

    UserList(users, modifier) { userIdToFollow ->
        viewModel.toggleFollow(userIdToFollow)
    }
}

@Composable
fun UserList(userList: UsersList, modifier: Modifier = Modifier, onFollow: (Long) -> Unit) {

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        items(
            items = userList.users,
            key = { it.id }
        ) { user ->
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    model = user.profileImage,
                    contentDescription = null,
                )

                Text(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f),
                    text = user.displayName,
                    fontSize = 24.sp
                )

                Text(
                    modifier = Modifier
                        .clickable {
                            onFollow(user.id)
                        }
                        .padding(start = 16.dp)
                        .border(2.dp, Color.Blue, RoundedCornerShape(12.dp))
                        .padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 8.dp),
                    text = if (!user.following) "Follow" else "Following",
                    fontSize = 24.sp,
                    color = Color.Blue
                )
            }
        }
    }
}


@Preview
@Composable
fun UserListPreview() {
    val user1 = UserUiModel(
        -1,
        "John Doe",
        0,
        ""
    )

    val user2 = UserUiModel(
        -1,
        "John Doe the second",
        94000,
        ""
    )

    val user3 = UserUiModel(
        -1,
        "John Doe the third",
        100,
        ""
    )
    UserList(
        userList = UsersList(listOf(user1, user2, user3)),
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
    ) {

    }
}
