package dev.mirosh.topusers.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import dev.mirosh.topusers.R
import dev.mirosh.topusers.ui.model.UserUiModel
import dev.mirosh.topusers.ui.model.UsersList

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = viewModel()
) {
    val uiState by mainViewModel.users.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()
    val followFailedMessage = stringResource(R.string.main_screen_follow_failed)

    LaunchedEffect(Unit) {
        mainViewModel.followFailedEvent.collect {
            snackbarHostState.showSnackbar(followFailedMessage)
        }
    }
    Scaffold(
        contentWindowInsets = WindowInsets(top = 0, bottom = 0),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { contentPadding ->
        MainContent(
            uiState = uiState,
            listState = listState,
            onToggleFollow = mainViewModel::toggleFollow,
            modifier = modifier.padding(contentPadding)
        )
    }
}

@Composable
fun MainContent(
    uiState: MainScreenUiState,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    onToggleFollow: (Long) -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (uiState) {
            is MainScreenUiState.Loading ->
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.Center)
                )

            is MainScreenUiState.Error -> MainScreenError()

            is MainScreenUiState.Success ->
                UserList(
                    userList = uiState.usersList,
                    listState = listState,
                    onFollow = onToggleFollow,
                    modifier = modifier
                )
        }
    }
}


@Composable
fun UserList(
    userList: UsersList,
    listState: LazyListState,
    modifier: Modifier = Modifier,
    onFollow: (Long) -> Unit
) {

    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        items(
            items = userList.users,
            key = { it.id }
        ) { user ->
            ListItem(user, onFollow)
        }
    }
}

@Composable
fun ListItem(user: UserUiModel, onFollow: (Long) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                contentScale = ContentScale.Fit,
                contentDescription = stringResource(R.string.main_screen_user_image),
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                error = painterResource(R.drawable.person_error),
                model = user.profileImage,
                placeholder = painterResource(R.drawable.person_placeholder)
            )

            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    text = user.displayName,
                    fontSize = 20.sp
                )
                Text(
                    text = user.reputation,
                    fontSize = 16.sp
                )
            }
            val buttonModifier = Modifier.padding(start = 16.dp)
            if (user.following) {
                Button(
                    onClick = { onFollow(user.id) },
                    modifier = buttonModifier
                ) {
                    Text(
                        text = stringResource(R.string.main_screen_following),
                        fontSize = 16.sp
                    )
                }
            } else {
                OutlinedButton(
                    onClick = { onFollow(user.id) },
                    modifier = buttonModifier
                ) {
                    Text(
                        text = stringResource(R.string.main_screen_follow),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenError(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            stringResource(R.string.main_screen_error),
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun UserListItemPreview() {

    val userUiModel = UserUiModel(
        id = 50L,
        displayName = "Nick Mirosh"
    )

    ListItem(userUiModel) {

    }
}


@Preview(showBackground = true)
@Composable
fun MainContentLoadingPreview() {
    val uiState = MainScreenUiState.Loading
    MainContent(
        uiState = uiState,
        onToggleFollow = {}
    )
}

@Preview(showBackground = true)
@Composable
fun UserListPreview() {
    val user1 = UserUiModel(
        1,
        "John Doe",
        "115k",
        ""
    )

    val user2 = UserUiModel(
        2,
        "John Doe the second",
        "110k",
        ""
    )

    val user3 = UserUiModel(
        3,
        "John Doe the third",
        "100k",
        ""
    )
    UserList(
        listState = rememberLazyListState(),
        userList = UsersList(listOf(user1, user2, user3)),
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
    ) {

    }
}

@Preview
@Composable
fun ListItemWithLongNameFollowing() {
    val userUIModel = UserUiModel(
        id = 1,
        displayName = "Mykola (Nick) Serhiyovych Miroshnychenko",
        reputation = "1,000k",
        profileImage = "",
        following = true
    )
    ListItem(user = userUIModel) {}
}

@Preview
@Composable
fun ListItemWithLongNameNotFollowing() {
    val userUIModel = UserUiModel(
        id = 1,
        displayName = "Mykola (Nick) Serhiyovych Miroshnychenko",
        profileImage = "",
        reputation = "1000k",
    )
    ListItem(user = userUIModel) {}
}

@Preview
@Composable
fun ListItemWithShortNameFollowing() {
    val userUIModel = UserUiModel(
        id = 1,
        displayName = "Nick Mirosh",
        reputation = "1000k",
        following = true,
        profileImage = ""
    )
    ListItem(user = userUIModel) {}
}

@Preview
@Composable
fun ListItemWithShortNameNotFollowing() {
    val userUIModel = UserUiModel(
        id = 1,
        displayName = "Nick Mirosh",
        reputation = "1000k",
        profileImage = ""
    )
    ListItem(user = userUIModel) {}
}
