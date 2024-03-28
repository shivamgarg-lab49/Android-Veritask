package com.satguru.veritask.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.satguru.veritask.ui.theme.fcl_body3
import com.satguru.veritask.ui.theme.fcl_content
import com.satguru.veritask.ui.theme.fcl_fill_component
import com.satguru.veritask.ui.theme.fcl_neutral_700
import com.satguru.veritask.ui.theme.fcl_neutral_900

/**
 * items : list of items to be render
 * onItemSelection : Get selected item index
 * defaultSelectedItemIndex : to highlight item by default (Optional)
 * useFixedWidth : set true if you want to set fix width to item (Optional)
 * itemWidth : Provide item width if useFixedWidth is set to true (Optional)
 * cornerRadius : To make control as rounded (Optional)
 * color : Set color to control (Optional)
 */
@Composable
fun SegmentedControl(
    selectedIndex: Int,
    items: List<String>,
    onItemSelection: (selectedIndex: Int) -> Unit,
    modifier: Modifier = Modifier,
    useFixedWidth: Boolean = false,
    itemWidth: Dp = 120.dp,
    cornerRadius: Dp = 4.dp,
) {
    require(selectedIndex >= 0 && selectedIndex < items.size) { "invalid index" }
    Row(
        modifier = modifier
    ) {
        items.forEachIndexed { index, item ->
            OutlinedButton(
                modifier = when (index) {
                    0 -> {
                        if (useFixedWidth) {
                            Modifier
                                .width(itemWidth)
                                .offset(0.dp, 0.dp)
                        } else {
                            Modifier
                                .wrapContentSize()
                                .offset(0.dp, 0.dp)
                        }
                    }
                    else -> {
                        if (useFixedWidth)
                            Modifier
                                .width(itemWidth)
                                .offset((-1 * index).dp, 0.dp)
                        else Modifier
                            .wrapContentSize()
                            .offset((-1 * index).dp, 0.dp)
                    }
                },
                onClick = {
                    onItemSelection(index)
                },
                shape = when (index) {
                    0 -> RoundedCornerShape(
                        topStart = cornerRadius,
                        topEnd = 0.dp,
                        bottomStart = cornerRadius,
                        bottomEnd = 0.dp
                    )

                    items.size - 1 -> RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = cornerRadius,
                        bottomStart = 0.dp,
                        bottomEnd = cornerRadius
                    )

                    else -> RoundedCornerShape(size = 0.dp)
                },
                border = BorderStroke(
                    1.dp, MaterialTheme.colors.fcl_neutral_700
                ),
                colors = if (selectedIndex == index) {
                    ButtonDefaults.outlinedButtonColors(
                        backgroundColor = MaterialTheme.colors.fcl_fill_component,
                        contentColor = MaterialTheme.colors.fcl_content
                    )
                } else {
                    ButtonDefaults.outlinedButtonColors(
                        backgroundColor = MaterialTheme.colors.fcl_neutral_900,
                        contentColor = MaterialTheme.colors.fcl_content
                    )
                },
            ) {
                Text(
                    text = item,
                    style = MaterialTheme.typography.fcl_body3
                )
            }
        }
    }
}