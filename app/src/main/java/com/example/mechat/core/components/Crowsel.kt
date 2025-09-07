package com.example.smartjobreminder.core.components//package com.novo.core.components
//import CachedImage
//import android.util.Log
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.pager.HorizontalPager
//import androidx.compose.foundation.pager.rememberPagerState
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.material3.Text
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.unit.dp
//import kotlinx.coroutines.delay
//
//
//
//@Composable
//fun <T> AutoSlidingCarousel(
//    items: List<CarouselItem<T>>,
//    autoSlideDuration: Long = 3000,
//    enableAutoSlide: Boolean = true,
//    modifier: Modifier = Modifier,
//    onItemClick: (CarouselItem<T>) -> Unit = {}
//) {
//    val pagerState = rememberPagerState(pageCount = { items.size })
//    val coroutineScope = rememberCoroutineScope()
//
//    Log.d("HomeScreen", "Recomposation occured in Crowsel")
//
//    // Auto-sliding logic
//    // Auto-sliding logic with safe check for empty list
//    LaunchedEffect(pagerState, enableAutoSlide) {
//        if (enableAutoSlide && items.isNotEmpty()) {  // Ensure items are available
//            while (true) {
//                delay(autoSlideDuration)
//                val nextPage = (pagerState.currentPage + 1) % items.size
//                pagerState.animateScrollToPage(nextPage)
//            }
//        }
//    }
//
////    coroutineScope.launch {
////        val targetPage = (pagerState.currentPage - 1).coerceAtLeast(0)
////        pagerState.animateScrollToPage(targetPage)
////    }
//
//
//
//    Box(
//        modifier = modifier
//    ) {
//        // Carousel content
//        HorizontalPager(
//            state = pagerState,
//            modifier = Modifier.fillMaxSize()
//        ) { index ->
//            val item = items[index]
//
//            CachedImage(
//                imageUrl = item.imageUrl,
//                modifier = Modifier.fillMaxSize()
//                    .clickable { onItemClick(item) },
//                contentDescription = item.content,
//                contentScale = ContentScale.Crop
//            )
//
//        }
//
//
//    }
//}
//
//// Model class for carousel items
//data class CarouselItem<T>(
//    val id: Int,
//    val content: String,
//    val title: String = "",
//    val imageUrl: String = "",
//    val data: T? = null
//)
//
//
