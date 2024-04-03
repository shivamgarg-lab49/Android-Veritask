package com.satguru.veritask.ui.features.users.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.satguru.veritask.R
import com.satguru.veritask.extensions.UiState
import com.satguru.veritask.extensions.toast
import com.satguru.veritask.models.User
import com.satguru.veritask.ui.components.Empty
import com.satguru.veritask.ui.components.ErrorScreen
import com.satguru.veritask.ui.components.ProgressBar
import com.satguru.veritask.ui.features.users.vm.UsersViewModel
import com.satguru.veritask.ui.theme.fcl_content_plus
import com.satguru.veritask.ui.theme.fcl_neutral_700
import com.satguru.veritask.ui.theme.fcl_title4

@Composable
@Destination(route = "screen-users")
@RootNavGraph(start = true)
fun UsersScreen(
    usersVM: UsersViewModel, destinationsNavigator: DestinationsNavigator
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 100.dp, horizontal = 24.dp)
            .background(MaterialTheme.colors.fcl_neutral_700, RoundedCornerShape(8.dp))
    ) {
        val context = LocalContext.current;
        val tappedUser by usersVM.selectedUser.collectAsState()
        val usersQuery by usersVM.uiStateForUsersData.collectAsState(UiState.Ideal)
        val loginQuery by usersVM.uiStateForLoginData.collectAsState(UiState.Ideal)

        val titleText = if (tappedUser == null) {
            stringResource(R.string.pick_an_account)
        } else {
            stringResource(R.string.enter_password)
        }

        Text(
            text = titleText,
            lineHeight = 20.sp,
            style = MaterialTheme.typography.fcl_title4,
            color = MaterialTheme.colors.fcl_content_plus,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        )

        if (tappedUser == null) {
            if (usersQuery is UiState.Loading) {
                ProgressBar(modifier = Modifier.fillMaxSize())
            } else if (usersQuery is UiState.Error) {
                ErrorScreen(
                    errorMessage = stringResource(R.string.something_went_wrong),
                    onRetry = { usersVM.fetch() },
                    modifier = Modifier.fillMaxSize()
                )
            } else if (usersQuery is UiState.Success) {
                val users = (usersQuery as UiState.Success<List<User>>).data
                if (users.isEmpty()) {
                    Empty(messageId = R.string.no_users_found, modifier = Modifier.fillMaxSize())
                } else {
                    UsersLazyList(items = users, onItemClick = usersVM::setSelectedUser)
                }
            }
        } else {
            PasswordItem(
                user = tappedUser!!,
                onBack = { usersVM.setSelectedUser(null) },
                onLogin = { usersVM.login(it) },
            )
            if (loginQuery is UiState.Loading) {
                ProgressBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp)
                )
            } else if (loginQuery is UiState.Error) {
                context.toast(message = stringResource(id = R.string.something_went_wrong))
            } else if (loginQuery is UiState.Success) {
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



