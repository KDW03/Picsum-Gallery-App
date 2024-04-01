package com.example.picsum.core.ui.icon

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.picsum.core.ui.R
import com.example.picsum.core.ui.icon.Icon.*


// icon 관리
object DoIcons {
    val Settings = ImageVectorIcon(Icons.Outlined.Settings)

    val Search = ImageVectorIcon(Icons.Outlined.Search)
    val ArrowBack = ImageVectorIcon(Icons.Rounded.ArrowBack)
    val Clear = ImageVectorIcon(Icons.Rounded.Clear)

    val Palette = DrawableResourceIcon(R.drawable.ic_palette)
    val DarkMode = DrawableResourceIcon(R.drawable.ic_dark_mode)
}


// ImageVectorIcon : Compose에서 제공하는 ImageVector 아이콘
// DrawableResourceIcon : 리소스 ID를 통해 접근하는 drawable 아이콘
sealed interface Icon {
    data class ImageVectorIcon(val imageVector : ImageVector) : Icon
    data class DrawableResourceIcon(@DrawableRes val resourced: Int) : Icon
}