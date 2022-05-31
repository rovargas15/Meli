package com.test.meli.util

import android.view.KeyEvent
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressKey
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText

fun checkViewWithIdAndTextIsDisplayed(@IdRes viewId: Int, @StringRes viewText: Int) {
    onView(withId(viewId))
        .check(matches(withText(viewText)))
        .check(matches(isDisplayed()))
}

fun checkViewIsDisplayedById(@IdRes viewId: Int) {
    onView(withId(viewId))
        .check(matches(isDisplayed()))
}

fun searchById(newString: String) {
    onView(withId(androidx.appcompat.R.id.search_src_text))
        .perform(ViewActions.typeText(newString))
        .perform(pressKey(KeyEvent.KEYCODE_ENTER))
}

fun performClickByViewId(@IdRes viewId: Int) {
    onView(withId(viewId)).perform(click())
}
