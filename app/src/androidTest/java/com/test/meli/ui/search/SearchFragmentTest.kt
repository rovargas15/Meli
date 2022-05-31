package com.test.meli.ui.search

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.SmallTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.test.meli.R
import com.test.meli.idleresources.waitUntilViewIsNotDisplayed
import com.test.meli.ui.ext.formatCurrency
import com.test.meli.ui.search.fragment.SearchFragment
import com.test.meli.util.BaseUITest
import com.test.meli.util.FILE_EMPTY_RESPONSE
import com.test.meli.util.FILE_SUCCESS_PRODUCT_RESPONSE
import com.test.meli.util.checkViewIsDisplayedById
import com.test.meli.util.checkViewWithIdAndTextIsDisplayed
import com.test.meli.util.hasItemCount
import com.test.meli.util.launchFragmentInHiltContainer
import com.test.meli.util.mockResponse
import com.test.meli.util.performClickByViewId
import com.test.meli.util.recyclerItemAtPosition
import com.test.meli.util.searchById
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.QueueDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.net.HttpURLConnection

@RunWith(AndroidJUnit4ClassRunner::class)
@HiltAndroidTest
class SearchFragmentTest : BaseUITest(dispatcher = QueueDispatcher()) {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val successHistoryResponse: MockResponse
        get() = mockResponse(FILE_SUCCESS_PRODUCT_RESPONSE, HttpURLConnection.HTTP_OK)

    private val errorResponse: MockResponse
        get() = mockResponse(FILE_EMPTY_RESPONSE, HttpURLConnection.HTTP_BAD_GATEWAY)

    private val navController = TestNavHostController(
        ApplicationProvider.getApplicationContext()
    )

    @Before
    fun init() {
        hiltRule.inject()
        launchFragmentInHiltContainer<SearchFragment>() {
            navController.setGraph(R.navigation.nav_graph)
            navController.setCurrentDestination(R.id.SearchFragment)
            viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(requireView(), navController)
                }
            }
        }
    }

    @Test
    @SmallTest
    fun giveSearchViewWhenGetProductThenShowRecyclerview() {

        checkViewIsDisplayedById(
            viewId = R.id.et_search
        )
        checkViewIsDisplayedById(
            viewId = R.id.abl_search
        )

        searchById("morotola")

        enqueueResponses(successHistoryResponse)

        waitUntilViewIsNotDisplayed(withId(R.id.lav_loader))

        checkViewIsDisplayedById(
            viewId = R.id.rcv_product
        )

        onView(withId(R.id.rcv_product)).check(
            hasItemCount(
                4
            )
        )

        onView(withId(R.id.rcv_product)).check(
            matches(
                recyclerItemAtPosition(
                    0,
                    hasDescendant(withText(" Moto G31 128 Gb  Gris Meteoro 4 Gb Ram"))
                )
            )
        ).check(
            matches(
                recyclerItemAtPosition(
                    0,
                    hasDescendant(withText(39999.toDouble().formatCurrency()))
                )
            )
        ).check(
            matches(
                recyclerItemAtPosition(
                    0,
                    hasDescendant(withText(R.string.condition_new))
                )
            )
        ).check(
            matches(
                recyclerItemAtPosition(
                    0,
                    hasDescendant(withText(R.string.product_free_shipping))
                )
            )
        ).check(
            matches(
                recyclerItemAtPosition(
                    0,
                    hasDescendant(withText(R.string.condition_new))
                )
            )
        )
    }

    @Test
    @SmallTest
    fun giveSearchViewWhenGetProductThenShowError() {
        searchById("morotola")

        enqueueResponses(errorResponse)

        waitUntilViewIsNotDisplayed(withId(R.id.lav_loader))

        checkViewWithIdAndTextIsDisplayed(R.id.txv_retry, R.string.btn_retry)
        checkViewWithIdAndTextIsDisplayed(R.id.txv_message_error, R.string.search_message_error)
        checkViewIsDisplayedById(R.id.lav_error)
    }

    @Test
    @SmallTest
    fun giveSearchViewWhenRetryThenShowsProducts() {
        searchById("morotola")

        enqueueResponses(errorResponse)

        waitUntilViewIsNotDisplayed(withId(R.id.lav_loader))

        performClickByViewId(R.id.txv_retry)

        enqueueResponses(successHistoryResponse)

        waitUntilViewIsNotDisplayed(withId(R.id.lav_loader))

        checkViewIsDisplayedById(
            viewId = R.id.rcv_product
        )

        onView(withId(R.id.rcv_product)).check(
            hasItemCount(
                4
            )
        )
    }

    @Test
    @SmallTest
    fun giveSearchViewWhenSelectProductThenShowDetails() {
        searchById("morotola")

        enqueueResponses(successHistoryResponse)

        waitUntilViewIsNotDisplayed(withId(R.id.lav_loader))

        onView(withId(R.id.rcv_product)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        assert(navController.currentDestination?.id == R.id.DetailsFragment)
    }
}
