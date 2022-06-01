package com.test.meli.ui.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.ImeAction.Companion
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.test.meli.R
import com.test.meli.domain.model.Product
import com.test.meli.ui.ext.formatCurrency
import com.test.meli.ui.search.viewmodel.SearchViewModel
import com.test.meli.ui.ui.theme.MeliTheme
import com.test.meli.ui.ui.theme.PrimaryColor
import com.test.meli.ui.ui.theme.Typography
import com.test.meli.ui.util.Condition
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val searchViewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MeliTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val (value, changeValue) = rememberSaveable { mutableStateOf("") }
                    CreateSearchView(
                        value = value,
                        onValueChange = changeValue,
                        onSearch = {
                            searchViewModel.getProductQuery(value)
                        },
                        content = {
                            ManagerUiState(searchViewModel = searchViewModel)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ManagerUiState(searchViewModel: SearchViewModel) {
    val stateProduct = searchViewModel.uiStateProduct
    val stateLoader = searchViewModel.uiStateLoader
    val stateError = searchViewModel.uiStateError
    when {
        stateProduct.isNotEmpty() -> {
            CreateList(stateProduct)
        }
        stateError.isNotEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error))
                LottieAnimation(composition)

                Text(
                    text = stringResource(id = R.string.search_message_error),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = Bold
                )
            }
        }
        stateLoader -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader))
                LottieAnimation(
                    composition = composition,
                    isPlaying = true,
                    modifier = Modifier.size(150.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateSearchView(
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .background(color = PrimaryColor)
                .fillMaxWidth()
        ) {
            val localSoftwareKeyboardController = LocalSoftwareKeyboardController.current
            OutlinedTextField(
                value = value,
                onValueChange = { onValueChange(it) },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth().background(Color.White),
                placeholder = { Text(text = stringResource(id = R.string.search_hint)) },
                keyboardOptions = KeyboardOptions(imeAction = Companion.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    onSearch()
                    localSoftwareKeyboardController?.hide()
                }),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Search,
                        "contentDescription",
                        modifier = Modifier.padding(5.dp)
                    )
                }
            )
        }
        content()
    }
}

@Composable
fun CreateList(products: List<Product>) {
    val scope = rememberCoroutineScope()
    LazyVerticalGrid(
        columns = GridCells.Adaptive(170.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
    ) {
        items(products.size) { index ->
            Card(
                modifier = Modifier.clickable {
                    scope.launch {
                    }
                },
                elevation = 1.dp,
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Column(
                    modifier = Modifier.padding(PaddingValues(8.dp))
                ) {
                    val product = products[index]
                    LoadImage(product.thumbnail)
                    Text(
                        text = product.title,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = product.price.formatCurrency(),
                        style = MaterialTheme.typography.titleLarge
                    )
                    if (product.shipping?.freeShipping == true) {
                        Text(
                            text = stringResource(R.string.product_free_shipping),
                            style = Typography.labelSmall
                        )
                    }
                    val condition = when (product.condition) {
                        Condition.New.value -> {
                            stringResource(R.string.condition_new)
                        }
                        Condition.Use.value -> {
                            stringResource(R.string.condition_use)
                        }
                        else -> {
                            ""
                        }
                    }
                    if (condition.isNotEmpty()) {
                        Text(
                            text = condition,
                            style = Typography.labelSmall
                        )
                    }
                    if (!product.seller.eshop?.nickName.isNullOrEmpty()) {
                        Text(
                            text = stringResource(R.string.product_free_shipping),
                            style = Typography.labelSmall,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoadImage(url: String) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        placeholder = painterResource(R.drawable.ic_placeholder),
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .fillMaxSize()
    )
}
