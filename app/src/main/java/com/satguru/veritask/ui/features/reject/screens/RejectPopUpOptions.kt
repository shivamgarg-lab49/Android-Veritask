package com.satguru.veritask.ui.features.reject.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import com.satguru.veritask.R
import com.satguru.veritask.extensions.UiState
import com.satguru.veritask.models.RejectReasonItem
import com.satguru.veritask.ui.components.ErrorScreen
import com.satguru.veritask.ui.components.ProgressBar
import com.satguru.veritask.ui.features.reject.vm.RejectPopupViewModel
import com.satguru.veritask.ui.features.sales.vm.SalesViewModel
import com.satguru.veritask.ui.theme.fcl_body2
import com.satguru.veritask.ui.theme.fcl_content
import com.satguru.veritask.ui.theme.fcl_fill_page
import com.satguru.veritask.ui.theme.fcl_title7


@Destination(route = "reject-popup", style = DestinationStyle.Dialog::class)
@Composable
fun RejectPopUpOptions(
    rejectPopUpOptionVM: RejectPopupViewModel,
    resultNavigator: ResultBackNavigator<String>,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val uiStateForReasonsData by rejectPopUpOptionVM.uiStateForReasonsData.collectAsState()
        when (uiStateForReasonsData) {
            is UiState.Ideal -> {
            }

            is UiState.Loading -> {
                ProgressBar()
            }

            is UiState.Error -> {
                ErrorScreen(
                    errorMessage = stringResource(R.string.something_went_wrong),
                    onRetry = { rejectPopUpOptionVM.fetchReasons() },
                )
            }

            is UiState.Success -> {
                val rejectReasons =
                    (uiStateForReasonsData as UiState.Success<List<RejectReasonItem>>).data
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = stringResource(R.string.reason_for_rejection),
                        color = MaterialTheme.colors.fcl_fill_page,
                        style = MaterialTheme.typography.fcl_title7,
                        fontWeight = FontWeight.W500,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .padding(horizontal = 16.dp)
                            .wrapContentHeight(align = Alignment.CenterVertically)
                    )
                    Divider(
                        color = MaterialTheme.colors.fcl_content, modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                    ) {
                        items(items = rejectReasons,
                            key = { rejectReasonItem -> rejectReasonItem.id },
                            contentType = { _ -> 1 }) { reasonItem ->
                            Text(
                                text = reasonItem.description,
                                color = Color.DarkGray,
                                style = MaterialTheme.typography.fcl_body2,
                                lineHeight = 24.sp,
                                fontWeight = FontWeight.W500,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .clickable {
                                        resultNavigator.navigateBack(reasonItem.description)
                                    }
                                    .padding(horizontal = 16.dp)
                                    .wrapContentHeight(align = Alignment.CenterVertically)

                            )
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = Unit, block = {
        rejectPopUpOptionVM.fetchReasons()
    })
}