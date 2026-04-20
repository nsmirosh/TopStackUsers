package dev.mirosh.topusers.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
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

    Column(modifier = modifier) {
        users.forEach {
            Text(
                modifier = Modifier.padding(12.dp),
                text = it.displayName,
                fontSize = 24.sp
            )
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
    UserList(listOf(user1, user2, user3))
}
