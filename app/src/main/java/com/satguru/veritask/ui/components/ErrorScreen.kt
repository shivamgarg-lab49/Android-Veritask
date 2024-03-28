package com.satguru.veritask.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.satguru.veritask.R
import com.satguru.veritask.ui.theme.fcl_body2
import com.satguru.veritask.ui.theme.fcl_body3
import com.satguru.veritask.ui.theme.fcl_fill_page
import com.satguru.veritask.ui.theme.fcl_status_normal_300

@Composable
fun ErrorScreen(errorMessage: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.fcl_body3,
            color = MaterialTheme.colors.fcl_status_normal_300
        )
        Button(
            onClick = { onRetry() },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.fcl_status_normal_300,
                contentColor = MaterialTheme.colors.fcl_fill_page
            )
        ) {
            Text(text = stringResource(R.string.retry), style = MaterialTheme.typography.fcl_body2)
        }
    }
}
