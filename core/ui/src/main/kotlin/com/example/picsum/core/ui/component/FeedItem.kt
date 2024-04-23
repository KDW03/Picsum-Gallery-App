package com.example.picsum.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.picsum.core.model.FeedResource

@Composable
fun FeedItem(
    modifier: Modifier = Modifier,
    onFeedClick: (Int) -> Unit,
    feedResource: FeedResource,
    screenWidthDp: Int
) {
    val cardHeight = calculateCardHeight(screenWidthDp)

    Card(
        modifier = modifier
            .requiredHeight(cardHeight)
            .fillMaxWidth()
            .clickable { onFeedClick(feedResource.id) },
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            PsAsyncImage(
                imageUrl = PicsumUrl.getImage(feedResource.id),
                contentDescription = null,
                modifier = Modifier.fillMaxSize().align(Alignment.Center)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black),
                            startY = 300f
                        ))
            )
            Text(
                text = feedResource.author,
                style = TextStyle(
                    color = Color.White,
                    fontSize = calculateFontSize(screenWidthDp),
                    fontWeight = FontWeight.Bold,
                    shadow = Shadow(Color.Black, offset = Offset(2f, 2f))
                ),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(8.dp)
            )
        }
    }
}
private fun calculateCardHeight(screenWidthDp: Int): Dp {
    return if (screenWidthDp < 600) 200.dp else 250.dp
}

private fun calculateFontSize(screenWidthDp: Int): TextUnit {
    return if (screenWidthDp < 600) 12.sp else 14.sp
}


object PicsumUrl {
    private const val BASE_IMAGE = "https://picsum.photos/id/"
    fun getImage(id: Int, width: Int = 400, height: Int = 300): String {
        return "$BASE_IMAGE${id}/$width/$height"
    }
}