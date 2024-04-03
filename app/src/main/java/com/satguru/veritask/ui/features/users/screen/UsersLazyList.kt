package com.satguru.veritask.ui.features.users.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.satguru.veritask.di.PreviewDataProvider
import com.satguru.veritask.models.User
import com.satguru.veritask.ui.components.AppLazyColumn

@Composable
fun UsersLazyList(items: List<User>, onItemClick: (User) -> Unit = {}) {
    AppLazyColumn(
        items = items,
        keyExtractor = { it.id },
        itemContent = { UserItem(user = it, onItemClick = onItemClick) },
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
    )
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun UsersLazyListPreview() {
    UsersLazyList(PreviewDataProvider.users)
}