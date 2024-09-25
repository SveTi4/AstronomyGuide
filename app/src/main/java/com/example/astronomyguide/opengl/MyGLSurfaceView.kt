package com.example.astronomyguide.opengl

import android.content.Context
import android.opengl.GLSurfaceView

class MyGLSurfaceView(context: Context) : GLSurfaceView(context) {

    private val renderer: MyGLRenderer

    init {
        // Устанавливаем версию OpenGL ES 2.0
        setEGLContextClientVersion(2)

        renderer = MyGLRenderer(context)

        // Устанавливаем рендерер
        setRenderer(renderer)

        // Режим рендеринга: непрерывный
        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }
}
