package com.example.astronomyguide.opengl

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGLRenderer(private val context: Context) : GLSurfaceView.Renderer {

    private lateinit var square: Square

    private val mvpMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        // Устанавливаем цвет очистки экрана (чёрный)
        GLES20.glClearColor(0f, 0f, 0f, 1f)
        // Включаем тест глубины
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        // Инициализируем квадрат
        square = Square(context)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        // Set the OpenGL viewport to the full surface
        GLES20.glViewport(0, 0, width, height)
        val ratio: Float = width.toFloat() / height.toFloat()
        // Устанавливаем перспективную проекцию
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 1f, 10f)
    }

    override fun onDrawFrame(gl: GL10) {
        // Очищаем экран
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // Устанавливаем камеру
        Matrix.setLookAtM(
            viewMatrix, 0,
            0f, 0f, 3f,   // Положение камеры
            0f, 0f, 0f,   // Точка, на которую смотрим
            0f, 1f, 0f    // Вектор "вверх"
        )

        // Масштабируем квадрат, чтобы он заполнил фон
        Matrix.scaleM(viewMatrix, 0, 2f, 3f, 1f)

        // Вычисляем итоговую матрицу MVP для квадрата
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        // Draw the square
        square.draw(mvpMatrix)
    }
}
