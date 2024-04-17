package com.satguru.veritask.ui.features.details.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import com.satguru.veritask.R
import com.satguru.veritask.extensions.UiState
import com.satguru.veritask.extensions.toast
import com.satguru.veritask.models.Sales
import com.satguru.veritask.ui.components.ErrorScreen
import com.satguru.veritask.ui.components.ProgressBar
import com.satguru.veritask.ui.components.Toolbar
import com.satguru.veritask.ui.features.destinations.ConfirmationScreenDestination
import com.satguru.veritask.ui.features.destinations.RejectPopUpOptionsDestination
import com.satguru.veritask.ui.features.details.vm.SalesDetailViewModel
import com.satguru.veritask.ui.theme.fcl_body1
import com.satguru.veritask.ui.theme.fcl_body2
import com.satguru.veritask.ui.theme.fcl_content
import com.satguru.veritask.ui.theme.fcl_content_subtle
import com.satguru.veritask.ui.theme.fcl_fill_component
import com.satguru.veritask.ui.theme.fcl_fill_container
import com.satguru.veritask.ui.theme.fcl_neutral_900
import com.satguru.veritask.ui.theme.fcl_primary_100
import com.satguru.veritask.ui.theme.fcl_primary_300
import com.satguru.veritask.ui.theme.fcl_secondary_500
import com.satguru.veritask.ui.theme.fcl_status_success_300
import com.satguru.veritask.utils.Constants
import com.satguru.veritask.utils.DeepLinkBuilder

@Composable
@Destination(
    route = "sale-details",
    deepLinks = [DeepLink(uriPattern = DeepLinkBuilder.DEALS_DETAIL_URI_PATTERN)]
)

fun SalesDetails(
    dealId: String,
    action: String,
    clientName: String? = null,
    salesDetailsVm: SalesDetailViewModel,
    destinationsNavigator: DestinationsNavigator,
    resultRecipient: ResultRecipient<RejectPopUpOptionsDestination, String>
) {
    val staticToolbarTitle = clientName ?: stringResource(id = R.string.sales_details)
    var dynamicToolbarTitle by remember { mutableStateOf(staticToolbarTitle) }
    val uiStateSalesRejectCall by salesDetailsVm.uiStateSalesRejectCall.collectAsState()
    val uiStateForSalesDetailData by salesDetailsVm.uiStateForSalesDetailData.collectAsState()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Toolbar(title = { Text(text = dynamicToolbarTitle) }, navigationIcon = {
            IconButton(onClick = { destinationsNavigator.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back_arrow)
                )
            }
        })
        when (uiStateForSalesDetailData) {
            is UiState.Ideal -> {
            }

            is UiState.Loading -> {
                ProgressBar(modifier = Modifier.fillMaxSize())
            }

            is UiState.Error -> {
                ErrorScreen(
                    errorMessage = stringResource(R.string.something_went_wrong),
                    onRetry = { salesDetailsVm.fetch(dealId) },
                    modifier = Modifier.fillMaxSize()
                )
            }

            is UiState.Success -> {
                val sales = (uiStateForSalesDetailData as UiState.Success<Sales>).data
                val products = sales.details.orEmpty()
                dynamicToolbarTitle = sales.client.name

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    SaleDetailRow(
                        title = stringResource(id = R.string.client), value = sales.client.name
                    )
                    SaleDetailRow(
                        title = stringResource(id = R.string.sales_rep),
                        value = sales.creator.name
                    )
                    SaleDetailRow(
                        title = stringResource(id = R.string.transaction_price),
                        value = Constants.formatAmount(sales.transactionValue)
                    )
                    SaleDetailRow(
                        title = stringResource(id = R.string.products),
                        value = sales.totalQuantity.toString()
                    )
                    when (sales.status) {

                        Constants.PENDING -> {
                            DealStatusRow(
                                title = stringResource(id = R.string.status),
                                value = stringResource(id = R.string.pending_approval),
                                valueTextBgColor = MaterialTheme.colors.fcl_primary_300,
                            )
                            SaleDetailRow(
                                title = stringResource(id = R.string.date_requested),
                                value = Constants.formatDate(sales.createdAt).first
                            )
                        }

                        Constants.APPROVED -> {
                            DealStatusRow(
                                title = stringResource(id = R.string.status),
                                value = stringResource(id = R.string.approved_sale),
                                valueTextBgColor = MaterialTheme.colors.fcl_status_success_300,
                            )
                            SaleDetailRow(
                                title = stringResource(id = R.string.date_approved),
                                value = Constants.formatDate(sales.updatedAt).first
                            )
                        }

                        Constants.REJECTED -> {
                            DealStatusRow(
                                title = stringResource(id = R.string.status),
                                value = stringResource(id = R.string.rejected_sale),
                                valueTextBgColor = MaterialTheme.colors.fcl_secondary_500,
                            )
                            SaleDetailRow(
                                title = stringResource(id = R.string.date_rejected),
                                value = Constants.formatDate(sales.updatedAt).first
                            )
                            SaleDetailRow(
                                title = stringResource(id = R.string.rejected_by),
                                value = sales.approver.name
                            )
                            if (sales.reason.isNullOrBlank().not()) {
                                SaleDetailRow(
                                    title = stringResource(id = R.string.rejection_comment),
                                    value = "\"${sales.reason.orEmpty()}\""
                                )
                            }
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(horizontal = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (products.isNotEmpty()) {
                        item {
                            ProductRowItem(
                                name = stringResource(R.string.products),
                                quantity = stringResource(R.string.quantity),
                                price = stringResource(R.string.total_price),
                                isHeading = true,
                            )
                        }
                    }
                    items(items = products,
                        key = { detail -> detail.id },
                        contentType = { _ -> 1 }) { detail ->
                        ProductRowItem(
                            name = detail.productName,
                            quantity = detail.productQuantity.toString(),
                            price = Constants.formatAmount(detail.productTotalPrice),
                            isHeading = false
                        )
                    }
                }
                if (sales.status == Constants.PENDING) {
                    if (uiStateSalesRejectCall is UiState.Ideal || uiStateSalesRejectCall is UiState.Success || uiStateSalesRejectCall is UiState.Error) {
                        if (salesDetailsVm.isSameManagerLoggedIn(sales.approverId)) {
                            ApproveOrRejectedComposable(
                                modifier = Modifier.padding(16.dp),
                                onApproved = {
                                    salesDetailsVm.approve()
                                },
                                onRejected = {
                                    destinationsNavigator.navigate(RejectPopUpOptionsDestination)
                                }
                            )
                            SideEffect {
                                if (action == Constants.APPROVED) {
                                    salesDetailsVm.approve()
                                } else if (action == Constants.REJECTED) {
                                    destinationsNavigator.navigate(RejectPopUpOptionsDestination)
                                }
                            }
                        } else {
                            DisclaimerText(modifier = Modifier.padding(16.dp))
                        }
                    }
                }

                when (uiStateSalesRejectCall) {
                    is UiState.Ideal -> {
                    }

                    is UiState.Loading -> {
                        ProgressBar(modifier = Modifier.padding(16.dp))
                    }

                    is UiState.Error -> {
                        LocalContext.current.toast(stringResource(id = R.string.something_went_wrong))
                    }

                    is UiState.Success -> {
                        val approvedOrRejected =
                            (uiStateSalesRejectCall as UiState.Success<Int>).data

                        val extraMessage =
                            sales.creator.name + " " + stringResource(R.string.will_be_notified)
                        val dateInfo = Constants.formatDate(sales.updatedAt)

                        if (approvedOrRejected == R.string.successfully_approved) {
                            val message =
                                "You have approved the sale created by ${sales.creator.name} for ${sales.client.name} costing ${
                                    Constants.formatAmount(sales.transactionValue)
                                } at ${dateInfo.second} on ${dateInfo.first}."
                            destinationsNavigator.popBackStack()
                            destinationsNavigator.navigate(
                                ConfirmationScreenDestination.invoke(
                                    salesApprovedOrRejected = true,
                                    message = message,
                                    extraMessage = extraMessage
                                )
                            )
                        } else if (approvedOrRejected == R.string.successfully_rejected) {
                            val message =
                                "You have rejected the sale created by ${sales.creator.name} for ${sales.client.name} costing ${
                                    Constants.formatAmount(sales.transactionValue)
                                } at ${dateInfo.second} on ${dateInfo.first} because of “${sales.reason}”."
                            destinationsNavigator.popBackStack()
                            destinationsNavigator.navigate(
                                ConfirmationScreenDestination.invoke(
                                    salesApprovedOrRejected = false,
                                    message = message,
                                    extraMessage = extraMessage
                                )
                            )
                        }
                    }

                    else -> {
                        throw IllegalStateException("Dev issue")
                    }
                }
            }

            else -> {
                throw IllegalStateException("Dev issue")
            }
        }
    }
    resultRecipient.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {
            }

            is NavResult.Value -> {
                salesDetailsVm.reject(result.value)
            }
        }
    }
}

@Composable
fun ApproveOrRejectedComposable(
    onRejected: () -> Unit, onApproved: () -> Unit, modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Button(
            modifier = Modifier.widthIn(115.dp),
            onClick = onRejected,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.fcl_fill_component,
                contentColor = MaterialTheme.colors.fcl_content
            )
        ) {
            Text(
                style = MaterialTheme.typography.fcl_body1,
                fontWeight = FontWeight.W400,
                text = stringResource(R.string.reject)
            )
        }
        Spacer(modifier = Modifier.size(16.dp))
        Button(
            modifier = Modifier.widthIn(115.dp),
            onClick = onApproved,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.fcl_primary_100,
                contentColor = MaterialTheme.colors.fcl_fill_container
            )
        ) {
            Text(
                style = MaterialTheme.typography.fcl_body1,
                fontWeight = FontWeight.W700,
                text = stringResource(R.string.approve)
            )
        }
    }
}

@Composable
fun DisclaimerText(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = stringResource(R.string.info_icon),
            tint = MaterialTheme.colors.fcl_content
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            color = MaterialTheme.colors.fcl_content,
            style = MaterialTheme.typography.fcl_body1,
            fontWeight = FontWeight.W600,
            text = stringResource(R.string.you_are_not_authorized_to_approve_reject_this_sale),
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Composable
fun SaleDetailRow(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(bottom = 8.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = MaterialTheme.colors.fcl_content_subtle,
            style = MaterialTheme.typography.fcl_body2,
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            text = value,
            color = MaterialTheme.colors.fcl_content,
            style = MaterialTheme.typography.fcl_body2,
        )
    }
}

@Composable
fun DealStatusRow(
    title: String,
    value: String,
    valueTextBgColor: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(bottom = 8.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = MaterialTheme.colors.fcl_content_subtle,
            style = MaterialTheme.typography.fcl_body2,
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = value,
            color = MaterialTheme.colors.fcl_neutral_900,
            style = MaterialTheme.typography.fcl_body2,
            modifier = Modifier
                .background(
                    color = valueTextBgColor, shape = RoundedCornerShape(6.dp)
                )
                .padding(horizontal = 10.dp, vertical = 3.dp)
        )
    }
}


@Composable
fun ProductRowItem(
    name: String, quantity: String, price: String, isHeading: Boolean, modifier: Modifier = Modifier
) {
    val fontWeight = if (isHeading) FontWeight.W700 else FontWeight.W400
    Row(modifier = modifier.fillMaxWidth()) {
        Text(
            text = name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colors.fcl_content,
            style = MaterialTheme.typography.fcl_body2,
            fontWeight = fontWeight,
            modifier = Modifier.weight(3f)
        )
        Text(
            textAlign = TextAlign.End,
            color = MaterialTheme.colors.fcl_content,
            style = MaterialTheme.typography.fcl_body2,
            fontWeight = fontWeight,
            text = quantity,
            modifier = Modifier
                .weight(1.2f)
                .padding(horizontal = 4.dp)
        )
        Text(
            text = price,
            color = MaterialTheme.colors.fcl_content,
            style = MaterialTheme.typography.fcl_body2,
            fontWeight = fontWeight,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(2f)
        )
    }
}


