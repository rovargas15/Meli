package com.test.meli.ui.main.fragment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells.Adaptive
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import coil.request.ImageRequest.Builder
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec.RawRes
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.test.meli.R
import com.test.meli.domain.model.Product
import com.test.meli.ui.ext.formatCurrency
import com.test.meli.ui.main.state.SearchEvent
import com.test.meli.ui.main.state.SearchState
import com.test.meli.ui.main.viewmodel.SearchViewModel
import com.test.meli.ui.theme.LocalDimensions
import com.test.meli.ui.theme.Typography
import com.test.meli.ui.util.Condition.New
import com.test.meli.ui.util.Condition.Use
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel,
    query: String,
    onQueryChange: (String) -> Unit,
    onEvent: (SearchEvent) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary
                    )
                    .fillMaxWidth()
            ) {
                val localSoftwareKeyboardController = LocalSoftwareKeyboardController.current
                OutlinedTextField(
                    value = query,
                    onValueChange = { onQueryChange(it) },
                    modifier = Modifier
                        .padding(LocalDimensions.current.paddingMedium)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background),
                    placeholder = { Text(text = stringResource(id = R.string.search_hint)) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        localSoftwareKeyboardController?.hide()
                        onEvent(SearchEvent.ProductByQuery(query = query))
                    }),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            Outlined.Search,
                            "contentDescription",
                            modifier = Modifier.padding(LocalDimensions.current.paddingSmall)
                        )
                    }
                )
            }
        }
    ) {
        ManagerState(
            searchViewModel = searchViewModel,
            modifier = Modifier.padding(it),
            onEvent = onEvent
        ) {
            onEvent(SearchEvent.Reload(query = query))
        }
    }
}

@Composable
fun ManagerState(
    searchViewModel: SearchViewModel,
    modifier: Modifier,
    onEvent: (SearchEvent) -> Unit,
    onRetry: () -> Unit
) {
    when (val state = searchViewModel.viewState.value) {
        is SearchState.Success -> {
            CreateList(
                modifier = modifier,
                products = state.products,
                onEvent = onEvent
            )
        }
        is SearchState.Error -> {
            ContentError(modifier = modifier, onRetry = onRetry)
        }
        is SearchState.Loader -> {
            ContentLoader(modifier = modifier)
        }
        else -> Unit
    }
}

@Composable
fun ContentLoader(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CreateAnimation(raw = RawRes(R.raw.loader))
    }
}

@Composable
fun ContentError(modifier: Modifier, onRetry: () -> Unit) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            LocalDimensions.current.paddingSmall,
            alignment = Alignment.CenterVertically
        ),
    ) {
        CreateAnimation(raw = RawRes(R.raw.error))
        Text(
            text = stringResource(id = R.string.search_message_error),
            style = MaterialTheme.typography.bodyMedium,
        )

        TextButton(
            onClick = {
                onRetry()
            }
        ) {
            Text(
                text = stringResource(id = R.string.btn_retry),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CreateAnimation(raw: RawRes, modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(spec = raw)
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = modifier.size(LocalDimensions.current.imageSmall)
    )
}

@Composable
fun CreateList(modifier: Modifier, products: List<Product>, onEvent: (SearchEvent) -> Unit) {
    LazyVerticalGrid(
        columns = Adaptive(minSize = LocalDimensions.current.heightCard),
        horizontalArrangement = Arrangement.spacedBy(space = LocalDimensions.current.paddingMedium),
        verticalArrangement = Arrangement.spacedBy(space = LocalDimensions.current.paddingMedium),
        modifier = modifier.padding(
            all = LocalDimensions.current.paddingMedium,
        )
    ) {
        items(products.size) { index ->
            CreateCard(products[index], onEvent)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCard(product: Product, onEvent: (SearchEvent) -> Unit) {
    val scope = rememberCoroutineScope()
    Card(
        modifier = Modifier.clickable {
            scope.launch {
                onEvent(SearchEvent.SelectProduct(product = product))
            }
        },
        border = BorderStroke(LocalDimensions.current.borderSmall, Color.Black)
    ) {
        Column(
            modifier = Modifier.padding(PaddingValues(LocalDimensions.current.paddingMedium)),
            verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.paddingSmall),
        ) {
            LoadImage(
                product.thumbnail,
                Modifier
                    .height(LocalDimensions.current.imageSmall)
                    .fillMaxWidth()
            )
            Text(
                text = product.title,
                style = Typography.bodyMedium,
                maxLines = MAX_LINES,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = product.price.formatCurrency(),
                style = Typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
            TextCondition(condition = product.condition)
            TextFreeShipping(isFreeShipping = product.shipping?.freeShipping)
            product.seller.eshop?.nickName?.let {
                Text(
                    text = stringResource(R.string.sold_by, it),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@Composable
fun TextCondition(condition: String) {
    val result = when (condition) {
        New.value -> {
            stringResource(R.string.condition_new)
        }
        Use.value -> {
            stringResource(R.string.condition_use)
        }
        else -> {
            ""
        }
    }
    if (condition.isNotEmpty()) {
        Text(
            text = result,
            style = Typography.labelSmall
        )
    }
}

@Composable
fun TextFreeShipping(isFreeShipping: Boolean?) {
    if (isFreeShipping == true) {
        Text(
            text = stringResource(R.string.product_free_shipping),
            style = Typography.labelSmall
        )
    }
}

@Composable
fun LoadImage(url: String, modifier: Modifier) {
    AsyncImage(
        model = Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        placeholder = painterResource(R.drawable.ic_placeholder),
        contentDescription = null,
        modifier = modifier
    )
}

private const val MAX_LINES = 3
