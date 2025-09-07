package com.example.mechat.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mechat.presentation.auth.viewmodel.AuthUiEvent


@Composable
fun ErrorBar(
    message: String,
    onClose: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .padding(horizontal = 4.dp)
            .background(color = Color.Red.copy(alpha = 0.5f)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Leading error icon
        Icon(
            imageVector = Icons.Outlined.Error,
            contentDescription = "Error Icon",
            tint = Color.White,
            modifier = Modifier
                .padding(start = 8.dp, end = 4.dp)
                .size(20.dp)
        )

        // Message text that expands
        Text(
            text = message,
            color = Color.White,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // Trailing close icon
        Icon(
            imageVector = Icons.Outlined.Close,
            contentDescription = "Close Icon",
            tint = Color.White,
            modifier = Modifier
                .clickable { onClose() }
                .padding(start = 4.dp, end = 8.dp)
                .size(20.dp)
        )
    }
}
