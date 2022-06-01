package com.test.meli.ui.main.fragment

import LoadImage
import TextCondition
import TextFreeShipping
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.test.meli.R.string
import com.test.meli.domain.model.Attribute
import com.test.meli.domain.model.Product
import com.test.meli.ui.ext.formatCurrency
import com.test.meli.ui.theme.Grey
import com.test.meli.ui.theme.PrimaryColor
import com.test.meli.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(product: Product?, onBackPress: () -> Unit) {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(text = "Detail")
                },
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
                    }
                },
            )
        },
        modifier = Modifier.background(color = PrimaryColor)
    ) {
        ContentDetailScreen(Modifier.padding(it), product)
    }
}

@Composable
fun ContentDetailScreen(modifier: Modifier, product: Product?) {
    checkNotNull(product)
    Column(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = product.title,
            style = Typography.titleLarge
        )
        LoadImage(url = product.thumbnail, modifier.height(200.dp))
        Text(
            text = product.price.formatCurrency(),
            style = Typography.headlineSmall
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

@Composable
fun TableScreen(attributes: List<Attribute>) {
    // Each cell of a column must have the same weight.
    val column1Weight = .5f // 50%
    val column2Weight = .5f // 50%
    // The LazyColumn will be our table. Notice the use of the weights below
    LazyColumn(
        userScrollEnabled = false
    ) {
        itemsIndexed(attributes) { index, attribute ->
            val modifier = if (index.mod(2) == 0) {
                Modifier
                    .background(Grey)
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
            .padding(8.dp)
    )
}
