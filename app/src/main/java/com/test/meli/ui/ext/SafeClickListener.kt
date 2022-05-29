package com.test.meli.ui.ext

import android.os.SystemClock
import android.view.View

private const val DEFAULT_INTERVAL: Int = 1000
private const val INITIAL_INTERVAL: Long = 0

class SafeClickListener(
    private var defaultInterval: Int = DEFAULT_INTERVAL,
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {
    private var lastTimeClicked: Long = INITIAL_INTERVAL
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v)
    }
}

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener =
        SafeClickListener {
            onSafeClick(it)
        }
    setOnClickListener(safeClickListener)
}
