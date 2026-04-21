package dev.mirosh.topusers.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import dev.mirosh.topusers.domain.model.User

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    val users by viewModel.users.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.fetchUsers()
    }
    UserList(users, modifier)
}

@Composable
fun UserList(users: List<User>, modifier: Modifier = Modifier) {

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        users.forEach {
            item {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    AsyncImage(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        model = it.profileImage,
                        contentDescription = null,
                    )

                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = it.displayName,
                        fontSize = 24.sp
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun UserListPreview() {
    val user1 = User(
        "John Doe",
        0,
        ""
    )

    val user2 = User(
        "John Doe the second",
        94000,
        ""
    )

    val user3 = User(
        "John Doe the third",
        100,
        ""
    )
    UserList(
        users = listOf(user1, user2, user3),
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
    )
}
