@file:OptIn(ExperimentalMaterialApi::class)

package com.satguru.veritask.ui.features.sales.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.satguru.veritask.BaseMessage
import com.satguru.veritask.R
import com.satguru.veritask.SharedApp
import com.satguru.veritask.extensions.UiState
import com.satguru.veritask.extensions.toast
import com.satguru.veritask.models.Sales
import com.satguru.veritask.ui.components.Empty
import com.satguru.veritask.ui.components.ErrorScreen
import com.satguru.veritask.ui.components.ProgressBar
import com.satguru.veritask.ui.components.ProgressDialog
import com.satguru.veritask.ui.components.SegmentedControl
import com.satguru.veritask.ui.components.Toolbar
import com.satguru.veritask.ui.features.destinations.SalesDestination
import com.satguru.veritask.ui.features.destinations.SalesDetailsDestination
import com.satguru.veritask.ui.features.destinations.UsersScreenDestination
import com.satguru.veritask.ui.features.sales.vm.SalesViewModel
import com.satguru.veritask.ui.theme.fcl_body2
import com.satguru.veritask.ui.theme.fcl_content
import com.satguru.veritask.utils.Constants
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Destination(route = "sales")
fun Sales(
    salesVM: SalesViewModel, destinationsNavigator: DestinationsNavigator
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {

        val logoutApiState by salesVM.uiStateForLogoutApi.collectAsState()
        val isRefreshing by salesVM.isRefreshing.collectAsState()
        val selectedTabIndex by salesVM.selectedTabIndex.collectAsState()
        val pullRefreshState =
            rememberPullRefreshState(isRefreshing, { salesVM.fetch(SalesViewModel.OpType.Pull) })

        Toolbar(title = { Text(text = stringResource(id = R.string.title_sales)) }, actions = {
            val loggedInUser = salesVM.getLoggedInUser()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { salesVM.logout() }) {
                Text(
                    text = loggedInUser?.name ?: stringResource(id = R.string.logout),
                    color = MaterialTheme.colors.fcl_content,
                    style = MaterialTheme.typography.fcl_body2,
                )
                Spacer(modifier = Modifier.size(8.dp))
                Icon(
                    tint = MaterialTheme.colors.fcl_content,
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = stringResource(id = R.string.logout)
                )
            }
        })

        SegmentedControl(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 8.dp), items = listOf(
                stringResource(id = R.string.pending),
                stringResource(id = R.string.approved),
                stringResource(id = R.string.rejected)
            ), onItemSelection = { selectedTabIndexOnClick ->
                salesVM.changeSelectedTabIndex(selectedTabIndexOnClick)
            }, selectedIndex = selectedTabIndex
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState),
        ) {
            val salesQuery by salesVM.uiStateForSalesData.collectAsState(UiState.Ideal)
            if (salesQuery is UiState.Loading) {
                ProgressBar(modifier = Modifier.fillMaxSize())
            } else if (salesQuery is UiState.Error) {
                ErrorScreen(
                    errorMessage = stringResource(R.string.something_went_wrong),
                    onRetry = { salesVM.fetch(SalesViewModel.OpType.Fresh) },
                    modifier = Modifier.fillMaxSize()
                )
            } else if (salesQuery is UiState.Success) {
                val sales = (salesQuery as UiState.Success<List<Sales>>).data
                if (sales.isEmpty()) {
                    Empty(
                        messageId = R.string.no_sales_found,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    SalesLazyColumn(items = sales) {
                        destinationsNavigator.navigate(
                            SalesDetailsDestination(
                                dealId = it.id,
                                action = Constants.NO_ACTION,
                                clientName = it.client.name
                            )
                        )
                    }
                }
            }
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
            LaunchedEffect(key1 = Unit, block = {
                SharedApp.queue
                    .onStart { salesVM.fetch(SalesViewModel.OpType.Fresh) }
                    .filterIsInstance<BaseMessage.DealCreatedMessage>()
                    .onEach { salesVM.fetch(SalesViewModel.OpType.Fresh) }
                    .launchIn(this)
            })

            when (logoutApiState) {
                is UiState.Loading -> {
                    ProgressDialog()
                }

                is UiState.Error -> {
                    LocalContext.current.toast(stringResource(R.string.something_went_wrong))
                }

                is UiState.Success -> {
                    LocalContext.current.toast(
                        stringResource(id = R.string.logout_successful)
                    )
                    destinationsNavigator.navigate(route = UsersScreenDestination.route) {
                        popUpTo(route = SalesDestination.route) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }
}