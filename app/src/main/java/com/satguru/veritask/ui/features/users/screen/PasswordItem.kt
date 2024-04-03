package com.satguru.veritask.ui.features.users.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.satguru.veritask.R
import com.satguru.veritask.di.PreviewDataProvider
import com.satguru.veritask.extensions.toast
import com.satguru.veritask.models.User
import com.satguru.veritask.ui.theme.fcl_body1
import com.satguru.veritask.ui.theme.fcl_body2
import com.satguru.veritask.ui.theme.fcl_content
import com.satguru.veritask.ui.theme.fcl_content_subtle
import com.satguru.veritask.ui.theme.fcl_fill_component
import com.satguru.veritask.ui.theme.fcl_fill_container
import com.satguru.veritask.ui.theme.fcl_neutral_700
import com.satguru.veritask.ui.theme.fcl_primary_100
import com.satguru.veritask.utils.Constants

@Composable
fun PasswordItem(user: User, onLogin: (User) -> Unit, onBack: () -> Unit) {
    val context = LocalContext.current;
    var enteredPassword by remember { mutableStateOf(Constants.DEFAULT_PASSWORD) }
    val onLoginTapped: (password: String) -> Unit = {
        if (it == Constants.DEFAULT_PASSWORD) {
            onLogin(user)
        } else {
            context.toast("Invalid password")
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Text(
            text = "${user.name} | ${user.email}",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.fcl_body2,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Text(
                text = stringResource(R.string.password),
                style = MaterialTheme.typography.fcl_body2,
                color = MaterialTheme.colors.fcl_content_subtle,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.width(32.dp))
            TextField(
                value = enteredPassword,
                onValueChange = { enteredPassword = it },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White,
                    cursorColor = Color.White,
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0x3D767680), RoundedCornerShape(5.dp))
            )
        }

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Button(
                modifier = Modifier.widthIn(86.dp),
                onClick = { onBack() },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.fcl_fill_component,
                    contentColor = MaterialTheme.colors.fcl_content
                )
            ) {
                Text(
                    style = MaterialTheme.typography.fcl_body1,
                    fontWeight = FontWeight.W400,
                    text = stringResource(R.string.back)
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            Button(
                enabled = enteredPassword.isNotEmpty(),
                modifier = Modifier.widthIn(86.dp),
                onClick = { onLoginTapped(enteredPassword) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.fcl_primary_100,
                    contentColor = MaterialTheme.colors.fcl_fill_container
                )
            ) {
                Text(
                    style = MaterialTheme.typography.fcl_body1,
                    fontWeight = FontWeight.W700,
                    text = stringResource(R.string.login)
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PasswordItemPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 100.dp, horizontal = 24.dp)
            .background(MaterialTheme.colors.fcl_neutral_700, RoundedCornerShape(8.dp))
    ) {
        PasswordItem(user = PreviewDataProvider.user, onLogin = {}, onBack = {})
    }
}