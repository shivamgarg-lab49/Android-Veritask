@file:OptIn(ExperimentalMaterialApi::class)

package com.satguru.veritask.ui.features.sales.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.satguru.veritask.R
import com.satguru.veritask.extensions.UiState
import com.satguru.veritask.extensions.toast
import com.satguru.veritask.models.Sales
import com.satguru.veritask.ui.components.Empty
import com.satguru.veritask.ui.components.ErrorScreen
import com.satguru.veritask.ui.components.ProgressBar
import com.satguru.veritask.ui.components.SegmentedControl
import com.satguru.veritask.ui.components.Toolbar
import com.satguru.veritask.ui.features.destinations.SalesDetailsDestination
import com.satguru.veritask.ui.features.sales.vm.SalesViewModel
import com.satguru.veritask.ui.theme.fcl_body2
import com.satguru.veritask.ui.theme.fcl_content
import com.satguru.veritask.ui.theme.fcl_primary_500
import com.satguru.veritask.utils.Constants

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

//        var expanded by remember { mutableStateOf(false) }
        val uiStateForSalesData by salesVM.uiStateForSalesData.collectAsState(UiState.Loading)
        val selectedTabIndex by salesVM.selectedTabIndex.collectAsState()

        val isRefreshing by salesVM.isRefreshing.collectAsState()
//        val isMyDealOn by salesVM.isMyDealOn.collectAsState()
        val pullRefreshState =
            rememberPullRefreshState(isRefreshing, { salesVM.fetch(SalesViewModel.OpType.Pull) })
        Toolbar(title = { Text(text = stringResource(id = R.string.title_sales)) }, actions = {
            val loggedInUser = salesVM.getLoggedInUser()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { salesVM.logout(destinationsNavigator) }) {
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

//            Box(
//                modifier = Modifier.wrapContentSize(Alignment.TopStart)
//            ) {
//                IconButton(onClick = { expanded = true }) {
//                    Icon(
//                        Icons.Default.MoreVert,
//                        contentDescription = stringResource(R.string.context_menu_icon)
//                    )
//                }
//                DropdownMenu(modifier = Modifier.background(color = Color(0xB3000000)),
////                    modifier = Modifier.background(color = MaterialTheme.colors.fcl_neutral_900),
//                    expanded = expanded, onDismissRequest = { expanded = false }) {
//                    DropdownMenuItem(onClick = { salesVM.toggleMyDeal() }) {
//                        Text(
//                            text = stringResource(R.string.my_sales),
//                            color = MaterialTheme.colors.fcl_content,
//                            style = MaterialTheme.typography.fcl_body2
//                        )
//                        Spacer(modifier = Modifier.size(8.dp))
//                        Switch(
//                            checked = isMyDealOn,
//                            onCheckedChange = { salesVM.toggleMyDeal() },
//                            colors = SwitchDefaults.colors(
//                                checkedThumbColor = MaterialTheme.colors.fcl_primary_500,
//                            )
//                        )
//                    }
//                    DropdownMenuItem(onClick = {
//                        salesVM.logout(destinationsNavigator) {
//                            context.toast(message = context.getString(R.string.logout_successful))
//                        }
//                    }) {
//                        val loggedInUser = salesVM.getLoggedInUser()
//                        Text(
//                            text = loggedInUser?.name ?: stringResource(id = R.string.logout),
//                            color = MaterialTheme.colors.fcl_content,
//                            style = MaterialTheme.typography.fcl_body2,
//                        )
//                        Spacer(modifier = Modifier.size(8.dp))
//                        Icon(
//                            tint = MaterialTheme.colors.fcl_content,
//                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
//                            contentDescription = stringResource(id = R.string.logout)
//                        )
//                    }
//                }
//            }
        })

        SegmentedControl(
            modifier = Modifier
                .padding(top = 16.dp)
                .height(30.dp), items = listOf(
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
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                when (uiStateForSalesData) {
                    is UiState.Ideal -> {

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
                                onRetry = { salesVM.fetch(SalesViewModel.OpType.Fresh) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillParentMaxHeight()
                            )
                        }
                    }

                    else -> {
                        val sales = (uiStateForSalesData as UiState.Success<List<Sales>>).data
                        items(key = { saleItem -> saleItem.id },
                            items = sales,
                            contentType = { _ -> 1 }) { saleItem ->
                            SaleItem(item = saleItem) {
                                destinationsNavigator.navigate(
                                    SalesDetailsDestination(
                                        dealId = it.id,
                                        action = Constants.NO_ACTION,
                                        clientName = it.client.name
                                    )
                                )
                            }
                        }
                        item {
                            if (sales.isEmpty()) {
                                Empty(
                                    messageId = R.string.no_sales_found,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillParentMaxHeight()
                                )
                            }
                        }
                    }
                }
            }
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )

            LaunchedEffect(key1 = Unit, block = {
                salesVM.fetch(SalesViewModel.OpType.Fresh)
            })
        }
    }
}