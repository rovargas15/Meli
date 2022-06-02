package com.test.meli.ui.main.fragment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.test.meli.R.string
import com.test.meli.domain.model.Attribute
import com.test.meli.domain.model.Product
import com.test.meli.ui.ext.formatCurrency
import com.test.meli.ui.ext.isPar
import com.test.meli.ui.theme.LocalDimensions
import com.test.meli.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(product: Product?, onBackPress: () -> Unit) {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                modifier = Modifier.background(
                    color = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Text(text = String())
                },
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
                    }
                },
            )
        },
    ) {
        ContentDetailScreen(Modifier.padding(it), product)
    }
}

@Composable
fun ContentDetailScreen(modifier: Modifier, product: Product?) {
    checkNotNull(product)
    BoxWithConstraints(modifier = Modifier.padding(LocalDimensions.current.paddingMedium)) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(this@BoxWithConstraints.maxHeight),
                verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.paddingMedium)
            ) {
                Text(
                    text = product.title,
                    style = Typography.titleMedium
                )
                LoadImage(
                    url = product.thumbnail,
                    modifier
                        .height(LocalDimensions.current.imageMedium)
                        .fillMaxWidth()
                )
                Text(
                    text = product.price.formatCurrency(),
                    style = Typography.headlineLarge,
                    fontWeight = FontWeight.SemiBold
                )
                TextCondition(condition = product.condition)
                TextFreeShipping(isFreeShipping = product.shipping?.freeShipping)
                Text(
                    text = stringResource(id = string.product_attributes),
                    style = Typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                TableScreen(product.attributes)
            }
        }
    }
}

@Composable
fun TableScreen(attributes: List<Attribute>) {
    val column1Weight = .5f // 50%
    val column2Weight = .5f // 50%
    LazyColumn(
        userScrollEnabled = false
    ) {
        itemsIndexed(attributes) { index, attribute ->
            val modifier = if (index.isPar()) {
                Modifier
                    .background(MaterialTheme.colorScheme.onSecondary)
                    .fillMaxSize()
            } else {
                Modifier.fillMaxSize()
            }
            Row(modifier = modifier) {
                TableCell(text = attribute.name, weight = column1Weight)
                if (attribute.valueName.isNullOrEmpty()) {
                    TableCell(text = attribute.values.first(), weight = column2Weight)
                } else {
                    TableCell(text = attribute.valueName, weight = column2Weight)
                }
            }
        }
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {
    Text(
        text = text,
        Modifier
            .weight(weight)
            .padding(LocalDimensions.current.paddingMedium)
    )
}
