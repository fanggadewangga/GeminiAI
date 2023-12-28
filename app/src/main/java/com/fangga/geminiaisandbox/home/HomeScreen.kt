package com.fangga.geminiaisandbox.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fangga.geminiaisandbox.BuildConfig
import com.fangga.geminiaisandbox.R
import com.fangga.geminiaisandbox.components.ChatBubble
import com.fangga.geminiaisandbox.data.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen() {
    val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.apiKey,
        generationConfig = generationConfig {
            temperature = 0.8f
        }
    )

    val viewModel = HomeViewModel(generativeModel)
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        bottomBar = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = viewModel.prompt.value,
                    onValueChange = {
                        viewModel.prompt.value = it
                    },
                    placeholder = {
                        Text(text = "Message Gemini AI")
                    },
                    shape = RoundedCornerShape(100.dp),
                    modifier = Modifier.weight(0.8f)
                )
                IconButton(
                    onClick = {
                        keyboardController?.hide()
                        if (viewModel.prompt.value.isNotEmpty()) {
                            viewModel.apply {
                                chats.add(Chat(isUser = true, viewModel.prompt.value))
                                generateContent(viewModel.prompt.value)
                                prompt.value = ""
                            }
                        } else
                            Toast.makeText(
                                context,
                                "Insert message first",
                                Toast.LENGTH_SHORT
                            ).show()
                    },
                    enabled = viewModel.prompt.value.isNotEmpty(),
                    modifier = Modifier
                        .background(
                            color = if (viewModel.prompt.value.isNotEmpty()) MaterialTheme.colorScheme.primary else Color.Gray,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send icon",
                        tint = Color.White,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(2.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
            }
        },
    ) {
        val bottomPadding = it.calculateBottomPadding()
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = bottomPadding + 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            when (uiState.value) {
                is HomeUiState.Initial -> {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.iv_ai),
                                contentDescription = "Ai Image",
                                modifier = Modifier.size(48.dp)
                            )
                            Text(text = "Chat bot with Gemini AI")
                        }
                    }
                }

                is HomeUiState.Loading -> {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator()
                    }
                }

                is HomeUiState.Success -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        viewModel.chats.forEach { item ->
                            ChatBubble(
                                isUser = item.isUser,
                                text = item.message,
                                modifier = if (item.isUser) Modifier
                                    .padding(end = 48.dp, bottom = 8.dp)
                                    .align(Alignment.Start)
                                else
                                    Modifier
                                        .padding(start = 48.dp, bottom = 8.dp)
                                        .align(Alignment.End)
                            )
                        }
                    }
                }

                is HomeUiState.Error -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.iv_ai_error),
                            contentDescription = "Ai Image",
                            modifier = Modifier.size(48.dp)
                        )
                        Text(text = (uiState.value as HomeUiState.Error).errorMessage)
                    }
                }
            }
        }
    }
}