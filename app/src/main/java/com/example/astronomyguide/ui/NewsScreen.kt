package com.example.astronomyguide.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.astronomyguide.viewmodel.NewsItem
import com.example.astronomyguide.viewmodel.NewsViewModel

@Composable
fun NewsScreen(viewModel: NewsViewModel = viewModel()) {
    val news = viewModel.newsState

    Column(Modifier.fillMaxSize()) {
        news.forEachIndexed { index, newsItem ->
            Row(Modifier.weight(1f)) {
                NewsItemComposable(
                    newsItem = newsItem,
                    onLikeClick = { viewModel.incrementLikes(index) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun NewsItemComposable(newsItem: NewsItem, onLikeClick: () -> Unit, modifier: Modifier) {
    Box(
        modifier = modifier
            .padding(4.dp)
            .background(Color.LightGray)
    ) {
        Row(Modifier.fillMaxSize()) {
            Box(
                Modifier
                    .weight(0.9f)
                    .fillMaxHeight()
                    .background(Color.White)
                    .padding(8.dp)
            ) {
                Text(text = newsItem.text, fontSize = 16.sp)
            }
            Box(
                Modifier
                    .weight(0.1f)
                    .fillMaxHeight()
                    .background(Color.Gray)
                    .clickable {
                        onLikeClick()
                    }
                    .padding(8.dp)
            ) {
                Text(text = newsItem.likes.toString(), fontSize = 14.sp, color = Color.White)
            }
        }
    }
}
