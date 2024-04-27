package com.example.picsum.feature.settings.component

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.picsum.core.ui.icon.Icon

@Composable
internal fun IconBox(
    icon: Icon,
    @StringRes contentDescriptionResource: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
            .size(IconBoxSize),
        contentAlignment = Alignment.Center,
    ) {
        when (icon) {
            is Icon.ImageVectorIcon -> {
                Icon(
                    imageVector = icon.imageVector,
                    contentDescription = stringResource(id = contentDescriptionResource),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }

            is Icon.DrawableResourceIcon -> {
                Icon(
                    painter = painterResource(id = icon.resourceId),
                    contentDescription = stringResource(id = contentDescriptionResource),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

private val IconBoxSize = 32.dp