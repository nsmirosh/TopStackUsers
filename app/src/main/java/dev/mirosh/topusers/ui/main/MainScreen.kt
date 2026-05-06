package dev.mirosh.topusers.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
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
                val painter = rememberAsyncImagePainter(user.profileImage)
                val state by painter.state.collectAsState()

                Image(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    painter = when (state) {
                        is AsyncImagePainter.State.Success -> painter
                        is AsyncImagePainter.State.Empty,
                        is AsyncImagePainter.State.Loading -> painterResource(R.drawable.person_placeholder)
                        else -> painterResource(R.drawable.person_error)

                    },
                    contentDescription = stringResource(R.string.main_screen_user_image)
                )
                Column(
                    modifier =
                        Modifier
                            .padding(start = 16.dp)
                            .weight(1f)
                ) {
                    Text(
                        text = user.displayName,
                        fontSize = 20.sp
                    )

                    Text(
                        text = user.reputation,
                        fontSize = 16.sp
                    )
                }
                Text(
                    modifier = Modifier
                        .clickable {
                            onFollow(user.id)
                        }
                        .padding(start = 16.dp)
                        .border(1.dp, Color.Blue, RoundedCornerShape(12.dp))
                        .padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 8.dp),
                    text = stringResource(
                        if (!user.following)
                            R.string.main_screen_follow else R.string.main_screen_unfollow
                    ),
                    fontSize = 20.sp,
                    color = Color.Blue
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenError(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(

            "Not supposed to be here",
//            stringResource(R.string.main_screen_error),
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


@Preview
@Composable
fun MainContentLoadingPreview() {
    val uiState = MainScreenUiState.Loading
    MainContent(
        uiState = uiState,
        onToggleFollow = {}
    )
}

@Preview
@Composable
fun MainContentErrorPreview() {
    val uiState = MainScreenUiState.Error
    MainContent(
        uiState = uiState,
        onToggleFollow = {}
    )
}

@Preview
@Composable
fun UserListPreview() {
    val user1 = UserUiModel(
        -1,
        "John Doe",
        "115k",
        ""
    )

    val user2 = UserUiModel(
        -1,
        "John Doe the second",
        "110k",
        ""
    )

    val user3 = UserUiModel(
        -1,
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
