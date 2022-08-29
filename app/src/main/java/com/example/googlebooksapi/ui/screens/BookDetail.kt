package com.example.googlebooksapi.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.googlebooksapi.R
import com.example.googlebooksapi.model.remote.BookItem

@ExperimentalUnitApi
@Composable
fun BookDetail(bookItem: BookItem) {
    //todo implement remember.

    Column(modifier = Modifier.fillMaxWidth()) {
        BookDetailHeader(bookItem)
        Spacer(modifier = Modifier.fillMaxWidth())
        BookDetailBody(bookItem)
    }
    
}

@Composable
fun BookDetailBody(bookItem: BookItem) {
    Card (
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(3.dp, Color.DarkGray),
        elevation = 0.dp
    ){
        Column {
            Text(
                text = bookItem.volumeInfo.authors.toString(),
                modifier = Modifier
                    .border(BorderStroke(3.dp, Color.DarkGray))
                    .padding(5.dp)
            )
            bookItem.volumeInfo.description?.let {
                Text(
                    text = it,
                    modifier = Modifier
                        .border(BorderStroke(3.dp, Color.DarkGray))
                        .padding(5.dp)
                )
            }
        }
    }
}


@ExperimentalUnitApi
@Composable
fun BookDetailHeader(bookItem: BookItem) {
    Card (
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(3.dp, Color.DarkGray),
        elevation = 0.dp
    ){
        Column {

            Text(
                text = bookItem.volumeInfo.title,
                fontSize = TextUnit(value = 20f, type = TextUnitType.Sp),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(5.dp)
            )

            bookItem.volumeInfo.publishedDate?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.ExtraLight,
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
        AsyncImage(model = ImageRequest.Builder(LocalContext.current)
            .data(bookItem.volumeInfo.imageLinks?.smallThumbnail)
            .crossfade(true)
            .build(),
            contentDescription = LocalContext.current.getString(R.string.book_cover_content_description)
        )
    }

}
