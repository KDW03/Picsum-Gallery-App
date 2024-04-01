package com.example.picsum.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter.*
import coil.compose.rememberAsyncImagePainter
import com.example.picsum.core.ui.R

// 이미지를 로드하고 표시하는 컴포넌트
@Composable
fun DoAsyncImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    placeholder: Painter = painterResource(id = R.drawable.ic_placeholder_default)
) {
    // 이미지 로딩 상태 추적
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    // imageUrl을 모델로 사용하여 이미지를 로드
    val imageLoader = rememberAsyncImagePainter(
        model = imageUrl,
        // 이미지 로딩 상태 변경시 콜백
        onState = { state ->
            isLoading = state is State.Loading
            isError = state is State.Error
        },
    )


    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {

        // 로딩 시 로딩 인디케이터
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(80.dp),
                color = MaterialTheme.colorScheme.tertiary
            )
        }

        Image(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            // error라 발생하면 pliaceholder
            painter = if (isError.not()) imageLoader else placeholder,
            contentDescription = contentDescription,
        )

    }
}
