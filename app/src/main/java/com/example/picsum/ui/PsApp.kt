package com.example.picsum.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.picsum.R
import com.example.picsum.component.PsTopAppBar
import com.example.picsum.core.data.util.NetworkMonitor
import com.example.picsum.core.ui.component.PsBackground
import com.example.picsum.core.ui.icon.PsIcons
import com.example.picsum.navigation.PsNavHost
import com.example.picsum.feature.settings.R as settingsR
import com.example.picsum.feature.search.R as searchR


@OptIn(
    ExperimentalLayoutApi::class,
    ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
)
@Composable
fun PsApp(
    networkMonitor: NetworkMonitor,
    appState: PsAppState = rememberPsAppState(
        networkMonitor = networkMonitor
    )
) {
    PsBackground {
        Box(Modifier.fillMaxSize()) {
            val snackbarHostState = remember { SnackbarHostState() }
            val isOffline by appState.isOffline.collectAsStateWithLifecycle()
            val listState = rememberLazyGridState()

            val isScrolling by remember {
                derivedStateOf {
                    listState.isScrollInProgress
                }
            }
            val alpha by animateFloatAsState(
                targetValue = if (isScrolling) 0.6f else 1f,
                label = "alpha"
            )
            val notConnectedMessage = stringResource(R.string.not_connected)
            LaunchedEffect(isOffline) {
                if (isOffline) {
                    snackbarHostState.showSnackbar(
                        message = notConnectedMessage,
                        duration = SnackbarDuration.Indefinite,
                    )
                }
            }

            val destination = appState.currentTopLevelDestination

            Scaffold(
                modifier = Modifier.semantics {
                    testTagsAsResourceId = true
                },
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground,
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                snackbarHost = { SnackbarHost(snackbarHostState) },
            ) { padding ->
                Row(
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .consumeWindowInsets(padding)
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing.only(
                                WindowInsetsSides.Horizontal,
                            ),
                        ),
                ) {
                    Column(
                        Modifier.fillMaxSize()
                    ) {
                        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
                        PsNavHost(appState = appState, listState = listState)
                    }
                }
            }

            if (destination != null) {
                AnimatedVisibility(
                    visible = !isScrolling,
                    enter = fadeIn(tween(150)),
                    exit = fadeOut(tween(150))
                ) {
                    PsTopAppBar(
                        modifier = Modifier
                            .alpha(alpha)
                            .align(Alignment.TopCenter),
                        titleRes = destination.titleTextId,
                        navigationIcon = PsIcons.Search.imageVector,
                        navigationIconContentDescription = stringResource(
                            id = searchR.string.search,
                        ),
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        ),
                        onNavigationClick = { appState.navigateToSearch() },
                        actionIcon = PsIcons.Settings.imageVector,
                        actionIconContentDescription = stringResource(
                            id = settingsR.string.settings
                        ),
                        onActionClick = {
                            appState.navigateToSetting()
                        }
                    )
                }
            }
        }
    }
}
