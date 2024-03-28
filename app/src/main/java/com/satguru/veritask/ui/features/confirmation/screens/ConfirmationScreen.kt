package com.satguru.veritask.ui.features.confirmation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.satguru.veritask.R
import com.satguru.veritask.ui.components.Toolbar
import com.satguru.veritask.ui.features.destinations.SalesDestination
import com.satguru.veritask.ui.theme.fcl_body1
import com.satguru.veritask.ui.theme.fcl_body2
import com.satguru.veritask.ui.theme.fcl_content
import com.satguru.veritask.ui.theme.fcl_fill_component

@Composable
@Destination(route = "confirmation")
fun ConfirmationScreen(
    salesApprovedOrRejected: Boolean,
    message: String,
    extraMessage: String,
    destinationsNavigator: DestinationsNavigator
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
    ) {
        val toolBarText =
            if (salesApprovedOrRejected) {
                stringResource(R.string.sale_approved)
            } else {
                stringResource(R.string.sale_rejected)
            }
        Toolbar(title = { Text(text = toolBarText) })
        Text(
            modifier = Modifier.padding(16.dp),
            text = message,
            lineHeight = 20.sp,
            style = MaterialTheme.typography.fcl_body2,
            color = MaterialTheme.colors.fcl_content
        )
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = extraMessage,
            style = MaterialTheme.typography.fcl_body2,
            color = MaterialTheme.colors.fcl_content
        )
        Button(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .height(45.dp),
            onClick = { destinationsNavigator.popBackStack() },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.fcl_fill_component,
                contentColor = MaterialTheme.colors.fcl_content
            )
        ) {
            Text(
                style = MaterialTheme.typography.fcl_body1,
                fontWeight = FontWeight.W400,
                text = stringResource(R.string.view_other_tasks_pending_approval)
            )
        }
    }
}