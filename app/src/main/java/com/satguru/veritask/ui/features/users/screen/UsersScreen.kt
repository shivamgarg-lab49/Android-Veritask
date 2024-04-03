package com.satguru.veritask.ui.features.users.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.satguru.veritask.ui.features.users.vm.UsersViewModel
import com.satguru.veritask.ui.theme.fcl_body1
import com.satguru.veritask.ui.theme.fcl_body2
import com.satguru.veritask.ui.theme.fcl_content
import com.satguru.veritask.ui.theme.fcl_content_plus
import com.satguru.veritask.ui.theme.fcl_content_subtle
import com.satguru.veritask.ui.theme.fcl_fill_component
import com.satguru.veritask.ui.theme.fcl_fill_container
import com.satguru.veritask.ui.theme.fcl_neutral_700
import com.satguru.veritask.ui.theme.fcl_primary_100
import com.satguru.veritask.ui.theme.fcl_title4
import com.satguru.veritask.utils.Constants

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
            .padding(horizontal = 16.dp)
    ) {
        val context = LocalContext.current;
        val selectedUser by usersVM.selectedUser.collectAsState()
        val uiStateForUsersData by usersVM.uiStateForUsersData.collectAsState(UiState.Ideal)
        val uiStateForLoginData by usersVM.uiStateForLoginData.collectAsState(UiState.Ideal)
        if (selectedUser == null) {
            Text(
                text = stringResource(R.string.pick_an_account),
                lineHeight = 20.sp,
                style = MaterialTheme.typography.fcl_title4,
                color = MaterialTheme.colors.fcl_content_plus,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 16.dp),
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
                                usersVM.setSelectedUser(user)
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
        } else {
            val user = selectedUser!!
            var password by remember { mutableStateOf(Constants.DEFAULT_PASSWORD) }
            val onLoginTapped: (password: String) -> Unit = {
                if (it == Constants.DEFAULT_PASSWORD) {
                    usersVM.login(user)
                } else {
                    context.toast("Invalid password")
                }
            }

            Text(
                text = stringResource(R.string.enter_password),
                lineHeight = 20.sp,
                style = MaterialTheme.typography.fcl_title4,
                color = MaterialTheme.colors.fcl_content_plus,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
            Text(
                text = "${user.name} | ${user.email}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.fcl_body2,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.password),
                    style = MaterialTheme.typography.fcl_body2,
                    color = MaterialTheme.colors.fcl_content_subtle,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.width(32.dp))
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.White,
                        cursorColor = Color.White,
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0x3D767680), RoundedCornerShape(5.dp))
                )
            }

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Button(
                    modifier = Modifier.widthIn(86.dp),
                    onClick = { usersVM.setSelectedUser(null) },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.fcl_fill_component,
                        contentColor = MaterialTheme.colors.fcl_content
                    )
                ) {
                    Text(
                        style = MaterialTheme.typography.fcl_body1,
                        fontWeight = FontWeight.W400,
                        text = stringResource(R.string.back)
                    )
                }
                Spacer(modifier = Modifier.size(16.dp))
                Button(
                    enabled = password.isNotEmpty(),
                    modifier = Modifier.widthIn(86.dp),
                    onClick = { onLoginTapped(password) },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.fcl_primary_100,
                        contentColor = MaterialTheme.colors.fcl_fill_container
                    )
                ) {
                    Text(
                        style = MaterialTheme.typography.fcl_body1,
                        fontWeight = FontWeight.W700,
                        text = stringResource(R.string.login)
                    )
                }
            }
        }
        when (uiStateForLoginData) {
            is UiState.Ideal -> {}
            is UiState.Loading -> {
                ProgressBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
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



