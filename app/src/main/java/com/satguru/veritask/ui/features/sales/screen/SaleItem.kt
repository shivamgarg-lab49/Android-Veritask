package com.satguru.veritask.ui.features.sales.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.satguru.veritask.R
import com.satguru.veritask.models.Sales
import com.satguru.veritask.ui.theme.fcl_body1
import com.satguru.veritask.ui.theme.fcl_body2
import com.satguru.veritask.ui.theme.fcl_content
import com.satguru.veritask.ui.theme.fcl_content_subtle
import com.satguru.veritask.ui.theme.fcl_fill_component
import com.satguru.veritask.utils.Constants

@Composable
fun SaleItem(item: Sales, modifier: Modifier = Modifier, onItemClick: (Sales) -> Unit) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick(item) },
        shape = RoundedCornerShape(size = 0.dp),
        backgroundColor = MaterialTheme.colors.fcl_fill_component
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = item.client.name,
                color = MaterialTheme.colors.fcl_content,
                style = MaterialTheme.typography.fcl_body1,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.size(8.dp))
            Row(modifier = Modifier) {
                Text(
                    text = stringResource(R.string.sales_rep),
                    color = MaterialTheme.colors.fcl_content_subtle,
                    style = MaterialTheme.typography.fcl_body2,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = item.creator.name,
                    color = MaterialTheme.colors.fcl_content,
                    style = MaterialTheme.typography.fcl_body2,
                    modifier = Modifier
                )
            }
            Spacer(modifier = Modifier.size(8.dp))
            Row(modifier = Modifier) {
                Text(
                    text = stringResource(R.string.total),
                    color = MaterialTheme.colors.fcl_content_subtle,
                    style = MaterialTheme.typography.fcl_body2,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = Constants.formatAmount(item.transactionValue),
                    color = MaterialTheme.colors.fcl_content,
                    style = MaterialTheme.typography.fcl_body2,
                    modifier = Modifier
                )
            }
            Spacer(modifier = Modifier.size(8.dp))
            Row(modifier = Modifier) {
                Text(
                    text = stringResource(R.string.of_products),
                    color = MaterialTheme.colors.fcl_content_subtle,
                    style = MaterialTheme.typography.fcl_body2,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = item.totalQuantity.toString(),
                    color = MaterialTheme.colors.fcl_content,
                    style = MaterialTheme.typography.fcl_body2,
                    modifier = Modifier
                )
            }
        }
    }
}