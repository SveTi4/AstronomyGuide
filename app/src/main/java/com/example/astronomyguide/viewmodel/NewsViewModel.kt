package com.example.astronomyguide.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

data class NewsItem(val text: String, var likes: Int)

class NewsViewModel : ViewModel() {

    private val _newsList = listOf(
        "Новость 1", "Новость 2", "Новость 3",
        "Новость 4", "Новость 5", "Новость 6",
        "Новость 7", "Новость 8", "Новость 9", "Новость 10"
    )

    private val newsMap = mutableMapOf<String, NewsItem>().apply {
        _newsList.forEach { news ->
            this[news] = NewsItem(news, 0)
        }
    }

    // Состояние новостей как список для Compose
    val newsState = mutableStateListOf<NewsItem>()

    init {
        resetNews()
        startNewsUpdater()
    }

    private fun resetNews() {
        newsState.clear()
        newsState.addAll(newsMap.values.toList().take(4))
    }

    private fun startNewsUpdater() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(5000) // Обновление одной случайной новости каждые 5 секунд
                updateRandomNews()
            }
        }
    }

    private fun updateRandomNews() {
        CoroutineScope(Dispatchers.Main).launch {
            val indexToUpdate = Random.nextInt(0, newsState.size)
            var randomNews: String

            do {
                randomNews = _newsList.random()
            } while (newsState.any { it.text == randomNews })

            newsState[indexToUpdate] = newsMap[randomNews]?.copy() ?: NewsItem(randomNews, 0)
        }
    }

    fun incrementLikes(index: Int) {
        newsState[index] = newsState[index].copy(likes = newsState[index].likes + 1)
        newsMap[newsState[index].text]?.likes = newsState[index].likes
    }
}
