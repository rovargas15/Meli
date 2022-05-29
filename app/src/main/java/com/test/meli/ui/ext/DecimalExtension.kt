package com.test.meli.ui.ext

import java.text.NumberFormat
import java.util.*

fun Double.formatCurrency(): String {
    val locale = Locale.getDefault()
    val numberFormat = NumberFormat.getCurrencyInstance(locale)
    return numberFormat.format(this)
}
