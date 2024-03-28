package com.satguru.veritask.ui.features.users.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.satguru.veritask.R
import com.satguru.veritask.extensions.UiState
import com.satguru.veritask.extensions.toast
import com.satguru.veritask.models.Users
import com.satguru.veritask.ui.components.Empty
import com.satguru.veritask.ui.components.ErrorScreen
import com.satguru.veritask.ui.components.ProgressBar
import com.satguru.veritask.ui.components.Toolbar
import com.satguru.veritask.ui.features.users.vm.UsersViewModel

@Composable
@Destination(route = "screen-users")
@RootNavGraph(start = true)
fun UsersScreen(
    usersVM: UsersViewModel, destinationsNavigator: DestinationsNavigator
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        val context = LocalContext.current;
        val uiStateForUsersData by usersVM.uiStateForUsersData.collectAsState(UiState.Ideal)
        val uiStateForLoginData by usersVM.uiStateForLoginData.collectAsState(UiState.Ideal)
        Toolbar(title = { Text(text = stringResource(R.string.login_as)) })
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            when (uiStateForUsersData) {
                is UiState.Ideal -> {
                    // NP
                }

                is UiState.Loading -> {
                    item {
                        ProgressBar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillParentMaxHeight(),
                        )
                    }
                }

                is UiState.Error -> {
                    item {
                        ErrorScreen(
                            errorMessage = stringResource(R.string.something_went_wrong),
                            onRetry = { usersVM.fetch() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillParentMaxHeight()
                        )
                    }
                }

                is UiState.Success -> {
                    val sales = (uiStateForUsersData as UiState.Success<List<Users>>).data

                    items(key = { saleItem -> saleItem.id },
                        items = sales,
                        contentType = { _ -> 1 }) { user ->
                        UserItem(user = user) {
                            usersVM.login(loggedInUser = user)
                        }
                    }
                    item {
                        if (sales.isEmpty()) {
                            Empty(
                                messageId = R.string.no_users_found,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillParentMaxHeight()
                            )
                        }
                    }
                }
            }
        }
        when (uiStateForLoginData) {
            is UiState.Loading -> {
                ProgressBar()
            }

            is UiState.Error -> {
                context.toast(message = stringResource(id = R.string.something_went_wrong))
            }

            is UiState.Success -> {
                context.toast(message = stringResource(id = R.string.login_successful))
                usersVM.navigateToNextPostLogin(navigator = destinationsNavigator)
            }
        }
    }
    LaunchedEffect(key1 = Unit, block = {
        usersVM.checkLoginState(
            userLoggedIn = {
                usersVM.navigateToNextPostLogin(navigator = destinationsNavigator)
            },
            userNotLoggedIn = {
                usersVM.fetch()
            },
        )
    })
}



