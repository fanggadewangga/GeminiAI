package com.fangga.geminiaisandbox.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.fangga.geminiaisandbox.R

@Composable
fun ChatBubble(isUser: Boolean, text: String, modifier: Modifier = Modifier) {
    val background = if (isUser) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
    val textColor = if (isUser) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimary
    val shape = if (isUser) RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomEnd = 16.dp
    ) else RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp)

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        if (isUser)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(32.dp)
                    .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.iv_person),
                    contentDescription = "User Icon",
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier.size(24.dp)
                )
            }
        Card(
            shape = shape,
            colors = CardDefaults.cardColors(containerColor = background),
            modifier = modifier
        ) {
            Text(
                text = text,
                color = textColor,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)
            )
        }
        if (!isUser)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(32.dp)
                    .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.iv_ai),
                    contentDescription = "AI Icon",
                    modifier = Modifier.size(24.dp)
                )
            }
    }
}