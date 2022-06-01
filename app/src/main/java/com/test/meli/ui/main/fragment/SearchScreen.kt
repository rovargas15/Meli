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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest.Builder
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec.RawRes
import com.airbnb.lottie.compose.rememberLottieComposition
import com.test.meli.R
import com.test.meli.R.drawable
import com.test.meli.R.raw
import com.test.meli.R.string
import com.test.meli.domain.model.Product
import com.test.meli.ui.ext.formatCurrency
import com.test.meli.ui.main.viewmodel.SearchViewModel
import com.test.meli.ui.theme.PrimaryColor
import com.test.meli.ui.theme.Typography
import com.test.meli.ui.util.Condition.New
import com.test.meli.ui.util.Condition.Use
import kotlinx.coroutines.launch

@Composable
fun ManagerState(
    searchViewModel: SearchViewModel,
    onSelect: (Product) -> Unit
) {
    val stateProduct = searchViewModel.uiStateProduct
    val stateLoader = searchViewModel.uiStateLoader
    val stateError = searchViewModel.uiStateError
    when {
        stateProduct.isNotEmpty() -> {
            CreateList(products = stateProduct, onSelect = onSelect)
        }
        stateError.isNotEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val composition by rememberLottieComposition(RawRes(raw.error))
                LottieAnimation(composition)

                Text(
                    text = stringResource(id = R.string.search_message_error),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        stateLoader -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val composition by rememberLottieComposition(RawRes(raw.loader))
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
fun SearchScreen(
    searchViewModel: SearchViewModel,
    onSelect: (Product) -> Unit
) {
    val (value, onValueChange) = rememberSaveable { mutableStateOf("") }
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
                    .fillMaxWidth()
                    .background(Color.White),
                placeholder = { Text(text = stringResource(id = string.search_hint)) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    searchViewModel.getProductQuery(value)
                    localSoftwareKeyboardController?.hide()
                }),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        Outlined.Search,
                        "contentDescription",
                        modifier = Modifier.padding(5.dp)
                    )
                }
            )
        }
        ManagerState(searchViewModel, onSelect)
    }
}

@Composable
fun CreateList(products: List<Product>, onSelect: (Product) -> Unit) {
    LazyVerticalGrid(
        columns = Adaptive(170.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(start = 8.dp, end = 8.dp)
    ) {
        items(products.size) { index ->
            CreateCard(products[index], onSelect)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCard(product: Product, onSelect: (Product) -> Unit) {
    val scope = rememberCoroutineScope()
    Card(
        modifier = Modifier.clickable {
            scope.launch {
                onSelect(product)
            }
        },
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Column(
            modifier = Modifier.padding(PaddingValues(8.dp))
        ) {
            LoadImage(product.thumbnail, Modifier.height(150.dp))
            Text(
                text = product.title,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = product.price.formatCurrency(),
                style = MaterialTheme.typography.titleLarge
            )

            TextCondition(condition = product.condition)
            TextFreeShipping(isFreeShipping = product.shipping?.freeShipping)

            if (!product.seller.eshop?.nickName.isNullOrEmpty()) {
                Text(
                    text = stringResource(string.product_free_shipping),
                    style = Typography.labelSmall,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun TextCondition(condition: String) {
    val result = when (condition) {
        New.value -> {
            stringResource(string.condition_new)
        }
        Use.value -> {
            stringResource(string.condition_use)
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
            text = stringResource(string.product_free_shipping),
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
        placeholder = painterResource(drawable.ic_placeholder),
        contentDescription = null,
        contentScale = ContentScale.FillHeight,
        modifier = modifier
    )
}