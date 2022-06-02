package com.test.meli.ui.main.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.test.meli.ui.main.fragment.DetailScreen
import com.test.meli.ui.main.fragment.SearchScreen
import com.test.meli.ui.main.viewmodel.SearchViewModel
import com.test.meli.ui.theme.MeliTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val searchViewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MeliTheme() {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "main") {
                        composable("main") {
                            val (value, onValueChange) = rememberSaveable { mutableStateOf("") }
                            SearchScreen(searchViewModel, value, onValueChange) {
                                searchViewModel.selectProductDetails(it)
                                navController.navigate("detail")
                            }
                        }
                        composable("detail") {
                            val product = searchViewModel.uiStateProductDetail
                            DetailScreen(product) {
                                navController.popBackStack()
                            }
                        }
                    }
                }
            }
        }
    }
}
