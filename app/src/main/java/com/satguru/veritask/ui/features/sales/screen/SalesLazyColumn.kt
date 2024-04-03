package com.satguru.veritask.ui.features.sales.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.satguru.veritask.di.PreviewDataProvider
import com.satguru.veritask.models.Sales
import com.satguru.veritask.ui.components.AppLazyColumn

@Composable
fun SalesLazyColumn(
    items: List<Sales>,
    onItemClick: (Sales) -> Unit = {}
) {
    AppLazyColumn(
        items = items,
        keyExtractor = { it.id },
        itemContent = { SaleItem(item = it, onItemClick = onItemClick) },
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
    )
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xff171A1C)
@Composable
fun SalesLazyColumnPreview() {
    val items = PreviewDataProvider.getDeals(LocalContext.current)
    SalesLazyColumn(items)
}