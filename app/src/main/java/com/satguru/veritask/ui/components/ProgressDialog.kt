package com.satguru.veritask.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.satguru.veritask.ui.theme.fcl_status_success_700

@Composable
fun ProgressDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    progressModifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    color: Color = MaterialTheme.colors.fcl_status_success_700
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Box(modifier = modifier, contentAlignment = contentAlignment) {
            CircularProgressIndicator(
                modifier = progressModifier, color = color
            )
        }
    }
}