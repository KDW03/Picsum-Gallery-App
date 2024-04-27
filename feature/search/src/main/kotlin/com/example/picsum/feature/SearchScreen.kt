package com.example.picsum.feature

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.picsum.core.model.FeedResource
import com.example.picsum.core.ui.component.FeedItem
import com.example.picsum.core.ui.icon.PsIcons
import com.example.picsum.feature.settings.R
import com.example.picsum.core.ui.R as uiR

@Composable
internal fun SearchRoute(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onFeedClick: (Int) -> Unit,
    searchViewModel: SearchViewModel = hiltViewModel(),
) {
    val recentSearchQueriesUiState by searchViewModel.recentSearchQueriesUiState.collectAsStateWithLifecycle()
    val searchResultUiState by searchViewModel.searchResultUiState.collectAsStateWithLifecycle()
    val searchQuery by searchViewModel.searchQuery.collectAsStateWithLifecycle()
    SearchScreen(
        modifier = modifier,
        onBackClick = onBackClick,
        onClearRecentSearches = searchViewModel::clearRecentSearches,
        onSearchQueryChanged = searchViewModel::onSearchQueryChanged,
        onSearchTriggered = searchViewModel::onSearchTriggered,
        onFeedClick = onFeedClick,
        recentSearchesUiState = recentSearchQueriesUiState,
        searchQuery = searchQuery,
        searchResultUiState = searchResultUiState,
    )
}

@Composable
internal fun SearchScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onClearRecentSearches: () -> Unit = {},
    onSearchQueryChanged: (String) -> Unit = {},
    onSearchTriggered: (String) -> Unit = {},
    onFeedClick: (Int) -> Unit = {},
    recentSearchesUiState: RecentSearchQueriesUiState = RecentSearchQueriesUiState.Loading,
    searchQuery: String = "",
    searchResultUiState: SearchResultUiState = SearchResultUiState.Loading,
) {
    Column(modifier = modifier) {
        SearchToolbar(
            onBackClick = onBackClick,
            onSearchQueryChanged = onSearchQueryChanged,
            onSearchTriggered = onSearchTriggered,
            searchQuery = searchQuery,
        )
        when (searchResultUiState) {
            is SearchResultUiState.Loading, SearchResultUiState.LoadFailed -> Unit
            is SearchResultUiState.LowQuery -> {
                if (searchQuery.length >= SEARCH_QUERY_MIN_LENGTH) {
                    LoadingContent()
                } else {
                    ShowRecentSearches(
                        recentSearchesUiState = recentSearchesUiState,
                        onClearRecentSearches = onClearRecentSearches,
                        onSearchQueryChanged = onSearchQueryChanged,
                        onSearchTriggered = onSearchTriggered
                    )
                }
            }
            is SearchResultUiState.EmptyResult -> {
                EmptySearchResultBody(searchQuery = searchQuery)
                ShowRecentSearches(
                    recentSearchesUiState = recentSearchesUiState,
                    onClearRecentSearches = onClearRecentSearches,
                    onSearchQueryChanged = onSearchQueryChanged,
                    onSearchTriggered = onSearchTriggered
                )
            }
            is SearchResultUiState.Success -> {
                SearchResultBody(
                    feeds = searchResultUiState.feeds,
                    onFeedClick = onFeedClick,
                )
            }
        }
        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
    }
}

@Composable
private fun ShowRecentSearches(
    recentSearchesUiState: RecentSearchQueriesUiState,
    onClearRecentSearches: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onSearchTriggered: (String) -> Unit
) {
    if (recentSearchesUiState is RecentSearchQueriesUiState.Success) {
        RecentSearchesBody(
            onClearRecentSearches = onClearRecentSearches,
            onRecentSearchClicked = {
                onSearchQueryChanged(it)
                onSearchTriggered(it)
            },
            recentSearchQueries = recentSearchesUiState.recentQueries.map { it.query },
        )
    }
}

@Composable
fun EmptySearchResultBody(
    searchQuery: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 48.dp),
    ) {
        val message = stringResource(id = R.string.search_result_not_found, searchQuery)
        val start = message.indexOf(searchQuery)
        Text(
            text = AnnotatedString(
                text = message,
                spanStyles = listOf(
                    AnnotatedString.Range(
                        SpanStyle(fontWeight = FontWeight.Bold),
                        start = start,
                        end = start + searchQuery.length,
                    ),
                ),
            ),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 24.dp),
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SearchResultBody(
    feeds: List<FeedResource>,
    onFeedClick: (Int) -> Unit,
) {
    val state = rememberLazyStaggeredGridState()
    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    val groupedFeeds = feeds.groupBy { it.author }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(300.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalItemSpacing = 24.dp,
            modifier = Modifier.fillMaxSize(),
            state = state,
        ) {
            groupedFeeds.forEach { (author, feedsByAuthor) ->
                item(span = StaggeredGridItemSpan.FullLine) {
                    Text(
                        text = "Author: $author",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                }
                feedsByAuthor.forEach { feed ->
                    val feedId = feed.id
                    item(
                        key = "feed-$feedId",
                        span = StaggeredGridItemSpan.FullLine,
                    ) {
                        FeedItem(
                            onFeedClick = onFeedClick,
                            feedResource = feed,
                            screenWidthDp = screenWidthDp
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun RecentSearchesBody(
    onClearRecentSearches: () -> Unit,
    onRecentSearchClicked: (String) -> Unit,
    recentSearchQueries: List<String>,
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(stringResource(id = R.string.recent_searches))
                    }
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
            if (recentSearchQueries.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onClearRecentSearches()
                    },
                    modifier = Modifier.padding(horizontal = 16.dp),
                ) {
                    Icon(
                        imageVector = PsIcons.Clear.imageVector,
                        contentDescription = stringResource(
                            id = R.string.clear_recent_searches_content_desc,
                        ),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
        LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
            items(recentSearchQueries) { recentSearch ->
                Text(
                    text = recentSearch,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .clickable { onRecentSearchClicked(recentSearch) }
                        .fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun SearchToolbar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    searchQuery: String = "",
    onSearchTriggered: (String) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        IconButton(onClick = { onBackClick() }) {
            Icon(
                imageVector = PsIcons.ArrowBack.imageVector,
                contentDescription = stringResource(
                    id = uiR.string.back,
                ),
            )
        }
        SearchTextField(
            onSearchQueryChanged = onSearchQueryChanged,
            onSearchTriggered = onSearchTriggered,
            searchQuery = searchQuery,
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchTextField(
    onSearchQueryChanged: (String) -> Unit,
    searchQuery: String,
    onSearchTriggered: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val onSearchExplicitlyTriggered = {
        keyboardController?.hide()
        onSearchTriggered(searchQuery)
    }

    TextField(
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        leadingIcon = {
            Icon(
                imageVector = PsIcons.Search.imageVector,
                contentDescription = stringResource(
                    id = R.string.search,
                ),
                tint = MaterialTheme.colorScheme.onSurface,
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onSearchQueryChanged("")
                    },
                ) {
                    Icon(
                        imageVector = PsIcons.Clear.imageVector,
                        contentDescription = stringResource(
                            id = R.string.clear_search_text_content_desc,
                        ),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        },
        onValueChange = {
            if (!it.contains("\n")) {
                onSearchQueryChanged(it)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .focusRequester(focusRequester)
            .onKeyEvent {
                if (it.key == Key.Enter) {
                    onSearchExplicitlyTriggered()
                    true
                } else {
                    false
                }
            },
        shape = RoundedCornerShape(32.dp),
        value = searchQuery,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearchExplicitlyTriggered()
            },
        ),
        maxLines = 1,
        singleLine = true,
    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
fun LoadingContent() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 50.dp)
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stringResource(id = R.string.load_search_result))
        }
    }
}
