package com.example.astronomyguide.opengl

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGLRenderer(private val context: Context) : GLSurfaceView.Renderer {

    private lateinit var square: Square
    private lateinit var cube: Cube

    private val mvpMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)
    private val tempMatrix = FloatArray(16)

    private var angle: Float = 0f

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        // Устанавливаем цвет очистки экрана (чёрный)
        GLES20.glClearColor(0f, 0f, 0f, 1f)
        // Включаем тест глубины
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        // Инициализируем квадрат и куб
        square = Square(context)
        cube = Cube()
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)

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

        // ---- Отрисовка квадрата (фона) ----

        // Сбрасываем модельную матрицу
        Matrix.setIdentityM(modelMatrix, 0)
        // Масштабируем квадрат, чтобы он заполнил фон
        Matrix.scaleM(modelMatrix, 0, 6f, 6f, 1f)
        // Отодвигаем квадрат назад по оси Z
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -1f)
        // Вычисляем итоговую матрицу MVP для квадрата
        Matrix.multiplyMM(tempMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, tempMatrix, 0)
        // Рисуем квадрат
        square.draw(mvpMatrix)

        // ---- Отрисовка куба ----

        // Сбрасываем модельную матрицу
        Matrix.setIdentityM(modelMatrix, 0)
        // Вращаем куб
        Matrix.rotateM(modelMatrix, 0, angle, 1f, 1f, 0f)
        angle += 0.5f  // Увеличиваем угол для анимации вращения
        Matrix.scaleM(modelMatrix, 0, 0.5f, 0.5f, 0.5f)
        // Вычисляем итоговую матрицу MVP для куба
        Matrix.multiplyMM(tempMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, tempMatrix, 0)
        // Рисуем куб
        cube.draw(mvpMatrix)
    }
}