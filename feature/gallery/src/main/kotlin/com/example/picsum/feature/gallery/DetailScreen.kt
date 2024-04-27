package com.example.picsum.feature.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.picsum.core.model.FeedResource
import com.example.picsum.core.ui.component.PicsumUrl
import com.example.picsum.core.ui.component.PsAsyncImage
import com.example.picsum.core.ui.component.SingleLineText

@Composable
internal fun DetailFeedRoute(
    modifier: Modifier = Modifier,
    viewModel: DetailFeedViewModel = hiltViewModel(),
) {

    val detailFeedUiState by viewModel.uiState.collectAsStateWithLifecycle()

    DetailFeedScreen(
        modifier = modifier,
        detailFeedUiState = detailFeedUiState
    )
}


@Composable
fun DetailFeedScreen(
    modifier: Modifier = Modifier,
    detailFeedUiState: DetailFeedUiState,
) {

    val uriHandler = LocalUriHandler.current

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (detailFeedUiState) {
            DetailFeedUiState.Loading -> Unit
            is DetailFeedUiState.Success -> {
                val feedResource = detailFeedUiState.feed
                LazyColumn(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {

                        val annotatedString = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    textDecoration = TextDecoration.Underline
                                )
                            ) {
                                append("Source: ")
                                pushStringAnnotation(
                                    tag = "URL",
                                    annotation = feedResource.url
                                )
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(feedResource.url)
                                }
                                pop()
                            }
                        }

                        SingleLineText(
                            text = feedResource.author,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        DetailFeedItem(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            feedResource = feedResource
                        )

                        ClickableText(
                            text = annotatedString,
                            style = MaterialTheme.typography.bodyMedium,
                            onClick = { offset ->
                                annotatedString.getStringAnnotations("URL", offset, offset)
                                    .firstOrNull()?.let { annotation ->
                                        uriHandler.openUri(annotation.item)
                                    }
                            }
                        )

                        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
                    }
                }
            }
        }
    }
}


@Composable
fun DetailFeedItem(
    modifier: Modifier = Modifier,
    feedResource: FeedResource
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        BoxWithConstraints {
            val constraints = this.constraints
            val imageHeightDp =
                (constraints.maxWidth / (feedResource.width.toFloat() / feedResource.height.toFloat())).dp

            val finalHeight = minOf(imageHeightDp.value, 300.dp.value).dp

            Column(modifier = Modifier.padding(16.dp)) {
                PsAsyncImage(
                    imageUrl = PicsumUrl.getImage(feedResource.id),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(finalHeight)
                )
                Spacer(modifier = Modifier.height(8.dp))

                SingleLineText(
                    text = "Size: ${feedResource.width} x ${feedResource.height}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}


