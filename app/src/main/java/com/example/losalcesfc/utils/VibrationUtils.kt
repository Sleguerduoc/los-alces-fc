package com.example.losalcesfc.utils

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

private fun hasVibrator(context: Context): Boolean {
    val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    return v.hasVibrator()
}

fun vibrarCorto(context: Context, duracionMs: Long = 50) {
    if (!hasVibrator(context)) return
    val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        v.vibrate(VibrationEffect.createOneShot(duracionMs, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        @Suppress("DEPRECATION")
        v.vibrate(duracionMs)
    }
}

fun vibrarError(context: Context) {
    if (!hasVibrator(context)) return
    val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    val pattern = longArrayOf(0, 40, 70, 60)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        v.vibrate(VibrationEffect.createWaveform(pattern, -1))
    } else {
        @Suppress("DEPRECATION")
        v.vibrate(pattern, -1)
    }
}

/** Sonido de confirmación corto (beep). */
fun sonidoConfirmacion() {
    val tone = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100)
    tone.startTone(ToneGenerator.TONE_PROP_BEEP, 150)
}

/** Sonido de error (alerta breve). */
fun sonidoError() {
    val tone = ToneGenerator(AudioManager.STREAM_ALARM, 100)
    tone.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 250)
}
