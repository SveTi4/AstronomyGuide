package com.example.astronomyguide.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    // Map для хранения количества лайков для каждой новости
    private val newsMap = mutableMapOf<String, NewsItem>().apply {
        _newsList.forEach { news ->
            this[news] = NewsItem(news, 0)
        }
    }

    private val _newsState = MutableStateFlow(newsMap.values.toList().take(4))
    val newsState = _newsState.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(5000) // Обновление каждые 5 секунд
                updateRandomNews()
            }
        }
    }

    private fun updateRandomNews() {
        val currentNews = _newsState.value.toMutableList()
        val indexToUpdate = Random.nextInt(0, currentNews.size)
        val randomNews = _newsList.random()
        currentNews[indexToUpdate] = newsMap[randomNews] ?: NewsItem(randomNews, 0)
        _newsState.value = currentNews
    }

    fun incrementLikes(index: Int) {
        val currentNews = _newsState.value[index]
        currentNews.likes += 1
        newsMap[currentNews.text] = currentNews // Обновляем Map
        _newsState.value = _newsState.value.toMutableList().apply {
            this[index] = currentNews
        }
    }
}
