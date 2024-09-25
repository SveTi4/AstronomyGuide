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
        // Set the background clear color to black
        GLES20.glClearColor(0f, 0f, 0f, 1f)
        // Enable depth testing
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        // Initialize the square
        square = Square(context)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        // Set the OpenGL viewport to the full surface
        GLES20.glViewport(0, 0, width, height)
        val ratio: Float = width.toFloat() / height.toFloat()
        // Set up the projection matrix
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 1f, 10f)
    }

    override fun onDrawFrame(gl: GL10) {
        // Clear the screen
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // Set up the view matrix
        Matrix.setLookAtM(
            viewMatrix, 0,
            0f, 0f, 3f,   // Camera position
            0f, 0f, 0f,   // Look at point
            0f, 1f, 0f    // Up vector
        )

        Matrix.scaleM(viewMatrix, 0, 2f, 3f, 1f)

        // Calculate the MVP matrix
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        // Draw the square
        square.draw(mvpMatrix)
    }
}
