package com.maciejwolf.pso_lab4

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : SensorEventListener, AppCompatActivity() {
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor

    private lateinit var animatedView: AnimatedView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)

        animatedView = AnimatedView(this)
        setContentView(animatedView)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(arg0: Sensor?, arg1: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ORIENTATION) {
            animatedView.onSensorEvent(event)
        }
    }

    class AnimatedView(context: Context?) : View(context) {
        private val paint: Paint = Paint()
        private var x = 0
        private var y = 0
        private var viewWidth = 0
        private var viewHeight = 0
        override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
            super.onSizeChanged(width, height, oldWidth, oldHeight)
            viewWidth = width
            viewHeight = height

            x = viewWidth / 2
            y = viewHeight / 2
        }

        fun onSensorEvent(event: SensorEvent) {

            x -= event.values[2].toInt()
            y -= event.values[1].toInt()

            if (x <= 0 + CIRCLE_RADIUS_IN_PIXELS) {
                x = 0 + CIRCLE_RADIUS_IN_PIXELS
            }
            if (x >= viewWidth - CIRCLE_RADIUS_IN_PIXELS) {
                x = viewWidth - CIRCLE_RADIUS_IN_PIXELS
            }
            if (y <= 0 + CIRCLE_RADIUS_IN_PIXELS) {
                y = 0 + CIRCLE_RADIUS_IN_PIXELS
            }
            if (y >= viewHeight - CIRCLE_RADIUS_IN_PIXELS) {
                y = viewHeight - CIRCLE_RADIUS_IN_PIXELS
            }
        }

        override fun onDraw(canvas: Canvas) {
            canvas.drawCircle(x.toFloat(), y.toFloat(), CIRCLE_RADIUS_IN_PIXELS.toFloat(), paint)
            invalidate()
        }

        companion object {
            private const val CIRCLE_RADIUS_IN_PIXELS = 100
        }

        init {
            paint.color = Color.BLUE
        }
    }
}