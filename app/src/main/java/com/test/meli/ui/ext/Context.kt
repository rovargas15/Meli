package com.test.meli.ui.ext

import android.content.Context
import android.os.Build
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import com.test.meli.R

fun Context.createRow(
    view: View?,
    block: (TableRow.() -> Unit)? = null
): TableRow {
    val row = TableRow(this).apply {
        weightSum = 2f
        addView(view)
    }
    block?.let { it(row) }
    return row
}

fun Context.createTextview(
    block: (TextView.() -> Unit)? = null
): TextView {
    val text = TextView(this).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setTextAppearance(R.style.TextViewTableRow)
        } else {
            setTextAppearance(context, R.style.TextViewTableRow)
        }
    }
    block?.let { it(text) }
    return text
}
