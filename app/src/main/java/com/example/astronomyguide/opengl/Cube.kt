package com.example.astronomyguide.opengl

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class Cube {

    // Координаты вершин куба (x, y, z)
    private val vertexCoords = floatArrayOf(
        // Передняя грань
        -1f,  1f,  1f,   // Верхний левый передний угол (0)
        -1f, -1f,  1f,   // Нижний левый передний угол (1)
        1f, -1f,  1f,   // Нижний правый передний угол (2)
        1f,  1f,  1f,   // Верхний правый передний угол (3)
        // Задняя грань
        -1f,  1f, -1f,   // Верхний левый задний угол (4)
        -1f, -1f, -1f,   // Нижний левый задний угол (5)
        1f, -1f, -1f,   // Нижний правый задний угол (6)
        1f,  1f, -1f    // Верхний правый задний угол (7)
    )

    // Индексы вершин для отрисовки граней куба
    private val indexOrder = shortArrayOf(
        // Передняя грань
        0, 1, 2, 0, 2, 3,
        // Правая грань
        3, 2, 6, 3, 6, 7,
        // Задняя грань
        7, 6, 5, 7, 5, 4,
        // Левая грань
        4, 5, 1, 4, 1, 0,
        // Верхняя грань
        4, 0, 3, 4, 3, 7,
        // Нижняя грань
        1, 5, 6, 1, 6, 2
    )

    // Цвета вершин (RGBA)
    private val colorCoords = floatArrayOf(
        // Каждый цвет соответствует вершине
        0.5f, 0.5f, 0.5f, 0.6f,  // Вершина 0 - красный
        0.5f, 0.5f, 0.5f, 0.6f,  // Вершина 1 - зеленый
        0.5f, 0.5f, 0.5f, 0.6f,  // Вершина 2 - синий
        0.5f, 0.5f, 0.5f, 0.6f,  // Вершина 3 - желтый
        0.5f, 0.5f, 0.5f, 0.6f,  // Вершина 4 - пурпурный
        0.5f, 0.5f, 0.5f, 0.6f,  // Вершина 5 - бирюзовый
        0.5f, 0.5f, 0.5f, 0.6f,  // Вершина 6 - белый
        0.5f, 0.5f, 0.5f, 0.6f   // Вершина 7 - черный
    )

    private val vertexBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(vertexCoords.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply {
                put(vertexCoords)
                position(0)
            }

    private val indexBuffer: ShortBuffer =
        ByteBuffer.allocateDirect(indexOrder.size * 2)
            .order(ByteOrder.nativeOrder())
            .asShortBuffer()
            .apply {
                put(indexOrder)
                position(0)
            }

    private val colorBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(colorCoords.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply {
                put(colorCoords)
                position(0)
            }

    // Шейдеры
    private val vertexShaderCode = """
        attribute vec4 aPosition;
        attribute vec4 aColor;
        varying vec4 vColor;
        uniform mat4 uMVPMatrix;
        void main() {
            vColor = aColor;
            gl_Position = uMVPMatrix * aPosition;
        }
    """.trimIndent()

    private val fragmentShaderCode = """
        precision mediump float;
        varying vec4 vColor;
        void main() {
            gl_FragColor = vColor;
        }
    """.trimIndent()

    private var program: Int

    init {
        // Компилируем шейдеры и создаём программу
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        program = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShader)
            GLES20.glAttachShader(it, fragmentShader)
            GLES20.glLinkProgram(it)
        }
    }

    fun draw(mvpMatrix: FloatArray) {
        GLES20.glUseProgram(program)

        // Получаем ссылки на атрибуты и униформы
        val positionHandle = GLES20.glGetAttribLocation(program, "aPosition")
        val colorHandle = GLES20.glGetAttribLocation(program, "aColor")
        val mvpMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix")

        // Устанавливаем матрицу преобразования
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0)        // Включаем и устанавливаем координаты вершин
        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glVertexAttribPointer(
            positionHandle,
            3,
            GLES20.GL_FLOAT,
            false,
            3 * 4,
            vertexBuffer
        )

        // Включаем и устанавливаем цвета вершин
        GLES20.glEnableVertexAttribArray(colorHandle)
        GLES20.glVertexAttribPointer(
            colorHandle,
            4,
            GLES20.GL_FLOAT,
            false,
            4 * 4,
            colorBuffer
        )

        // Рисуем куб
        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES,
            indexOrder.size,
            GLES20.GL_UNSIGNED_SHORT,
            indexBuffer
        )

        // Отключаем атрибуты
        GLES20.glDisableVertexAttribArray(positionHandle)
        GLES20.glDisableVertexAttribArray(colorHandle)
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        return GLES20.glCreateShader(type).also { shader ->
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)

            // Проверка ошибок компиляции
            val compiled = IntArray(1)
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0)
            if (compiled[0] == 0) {
                val errorMsg = GLES20.glGetShaderInfoLog(shader)
                GLES20.glDeleteShader(shader)
                throw RuntimeException("Ошибка компиляции шейдера: $errorMsg")
            }
        }
    }
}