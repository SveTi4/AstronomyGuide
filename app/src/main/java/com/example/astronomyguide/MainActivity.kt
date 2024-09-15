package com.example.astronomyguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.astronomyguide.viewmodel.NewsViewModel
import com.example.astronomyguide.viewmodel.NewsItem

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsScreen()
        }
    }
}

@Composable
fun NewsScreen(viewModel: NewsViewModel = viewModel()) {
    val news by viewModel.newsState.collectAsState()

    Column(Modifier.fillMaxSize()) {
        Row(Modifier.weight(1f)) {
            NewsItemComposable(newsItem = news[0], onLikeClick = { viewModel.incrementLikes(0) }, modifier = Modifier.weight(1f))
        }
        Row(Modifier.weight(1f)) {
            NewsItemComposable(newsItem = news[1], onLikeClick = { viewModel.incrementLikes(1) }, modifier = Modifier.weight(1f))
        }
        Row(Modifier.weight(1f)) {
            NewsItemComposable(newsItem = news[2], onLikeClick = { viewModel.incrementLikes(2) }, modifier = Modifier.weight(1f))
        }
        Row(Modifier.weight(1f)) {
            NewsItemComposable(newsItem = news[3], onLikeClick = { viewModel.incrementLikes(3) }, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun NewsItemComposable(newsItem: NewsItem, onLikeClick: () -> Unit, modifier: Modifier) {
    var likes by remember(newsItem.likes) { mutableStateOf(newsItem.likes) }

    Box(modifier = modifier.padding(4.dp).background(Color.LightGray)) {
        Row(Modifier.fillMaxSize()) {
            // Основная новость (90%)
            Box(
                Modifier
                    .weight(0.9f)
                    .fillMaxHeight()
                    .background(Color.White)
                    .padding(8.dp)
            ) {
                Text(text = newsItem.text, fontSize = 16.sp) // Используем sp для размера текста
            }
            // Блок лайков (10%)
            Box(
                Modifier
                    .weight(0.1f)
                    .fillMaxHeight()
                    .background(Color.Gray)
                    .clickable {
                        onLikeClick()
                        likes += 1 // Обновляем локальное состояние лайков
                    }
                    .padding(8.dp)
            ) {
                Text(text = likes.toString(), fontSize = 14.sp, color = Color.White) // Используем sp для размера текста
            }
        }
    }
}
