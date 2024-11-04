package com.example.astronomyguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.astronomyguide.ui.NewsScreen
import com.example.astronomyguide.opengl.MyGLSurfaceView
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import androidx.compose.ui.viewinterop.AndroidView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppContent()
        }
    }
}

@Composable
fun AppContent() {
    val pagerState = rememberPagerState()
    val context = LocalContext.current

    HorizontalPager(count = 2, state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
        when (page) {
            0 -> NewsScreen() // Экран с новостями
            1 -> AndroidView(
                factory = { MyGLSurfaceView(context) },
                modifier = Modifier.fillMaxSize()
            ) // Экран с квадратом и кубом
        }
    }
}