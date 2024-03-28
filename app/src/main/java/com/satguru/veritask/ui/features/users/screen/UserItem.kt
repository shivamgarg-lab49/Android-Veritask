package com.satguru.veritask.ui.features.users.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.satguru.veritask.models.Users
import com.satguru.veritask.ui.theme.fcl_body1
import com.satguru.veritask.ui.theme.fcl_content
import com.satguru.veritask.ui.theme.fcl_content_subtle
import com.satguru.veritask.ui.theme.fcl_fill_component
import com.satguru.veritask.ui.theme.fcl_title7
import kotlinx.coroutines.launch

@Composable
fun UserItem(user: Users, onItemClick: (Users) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { onItemClick(user) },
        shape = RoundedCornerShape(size = 8.dp),
        backgroundColor = MaterialTheme.colors.fcl_fill_component
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Circular image icon
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "User icon",
                tint = Color.White,
                modifier = Modifier.size(50.dp)
            )

            // User email and name
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = user.name,
                    color = MaterialTheme.colors.fcl_content,
                    style = MaterialTheme.typography.fcl_title7,
                    modifier = Modifier
                )
                Text(
                    text = user.email,
                    color = MaterialTheme.colors.fcl_content_subtle,
                    style = MaterialTheme.typography.fcl_body1,
                    modifier = Modifier
                )
            }
            // Right row for navigation
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Navigate Forward",
                tint = Color.White
            )
        }
    }
}