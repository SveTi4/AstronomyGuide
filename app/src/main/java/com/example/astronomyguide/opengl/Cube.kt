package com.example.astronomyguide.opengl

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

/**
 * Класс Cube представляет собой 3D-объект в виде куба для отрисовки с использованием OpenGL ES 2.0.
 * Куб состоит из вершин и граней, каждая вершина имеет свои координаты и цвет.
 *
 * Основные этапы:
 * - Инициализация вершин, индексов и цветов куба.
 * - Компиляция шейдеров и создание программы OpenGL.
 * - Метод {@link #draw(float[])} для отрисовки куба с использованием переданной матрицы преобразования.
 */
class Cube {

    private val vertexCoords = floatArrayOf(
        -1f,  1f,  1f,
        -1f, -1f,  1f,
        1f, -1f,  1f,
        1f,  1f,  1f,
        -1f,  1f, -1f,
        -1f, -1f, -1f,
        1f, -1f, -1f,
        1f,  1f, -1f
    )

    private val indexOrder = shortArrayOf(
        0, 1, 2, 0, 2, 3,
        3, 2, 6, 3, 6, 7,
        7, 6, 5, 7, 5, 4,
        4, 5, 1, 4, 1, 0,
        4, 0, 3, 4, 3, 7,
        1, 5, 6, 1, 6, 2
    )

    private val colorCoords = floatArrayOf(
        0.5f, 0.5f, 0.5f, 0.6f,
        0.5f, 0.5f, 0.5f, 0.6f,
        0.5f, 0.5f, 0.5f, 0.6f,
        0.5f, 0.5f, 0.5f, 0.6f,
        0.5f, 0.5f, 0.5f, 0.6f,
        0.5f, 0.5f, 0.5f, 0.6f,
        0.5f, 0.5f, 0.5f, 0.6f,
        0.5f, 0.5f, 0.5f, 0.6f
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
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        program = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShader)
            GLES20.glAttachShader(it, fragmentShader)
            GLES20.glLinkProgram(it)
        }
    }

    /**
     * Выполняет отрисовку куба с использованием переданной матрицы преобразования.
     *
     * @param mvpMatrix Матрица модели-вида-проекции (Model-View-Projection Matrix), применяемая к объекту.
     */
    fun draw(mvpMatrix: FloatArray) {
        GLES20.glUseProgram(program)

        val positionHandle = GLES20.glGetAttribLocation(program, "aPosition")
        val colorHandle = GLES20.glGetAttribLocation(program, "aColor")
        val mvpMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix")

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

        GLES20.glEnableVertexAttribArray(colorHandle)
        GLES20.glVertexAttribPointer(
            colorHandle,
            4,
            GLES20.GL_FLOAT,
            false,
            4 * 4,
            colorBuffer
        )

        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES,
            indexOrder.size,
            GLES20.GL_UNSIGNED_SHORT,
            indexBuffer
        )

        GLES20.glDisableVertexAttribArray(positionHandle)
        GLES20.glDisableVertexAttribArray(colorHandle)
    }

    /**
     * Компилирует шейдер и проверяет наличие ошибок компиляции.
     *
     * @param type Тип шейдера (вершинный или фрагментный).
     * @param shaderCode Код шейдера, который нужно скомпилировать.
     * @return Идентификатор скомпилированного шейдера.
     * @throws RuntimeException В случае ошибки компиляции шейдера.
     */
    private fun loadShader(type: Int, shaderCode: String): Int {
        return GLES20.glCreateShader(type).also { shader ->
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)

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