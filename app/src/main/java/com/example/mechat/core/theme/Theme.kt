package com.novo.core.theme

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

//private val DarkColorScheme = darkColorScheme(
//    primary = Purple80,
//    secondary = PurpleGrey80,
//    tertiary = Pink80,
//)
//
//private val LightColorScheme = lightColorScheme(
//    primary = Purple40,
//    secondary = PurpleGrey40,
//    tertiary = Pink40
//)


data class DeviceUtils(
    val screenWidth: Dp,
    val screenHeight: Dp,
    val isPortrait: Boolean,
    val isLandscape: Boolean,
    val isTablet: Boolean = screenWidth > 600.dp,
    val isPhone: Boolean = !isTablet,
    val isDarkTheme: Boolean ,
)

val LocalDeviceUtils = staticCompositionLocalOf<DeviceUtils> {
    error("No DeviceUtils provided")
}






@Composable
fun NovoCinemasAndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    val isDarkTheme = isSystemInDarkTheme()
    val colors = if (isDarkTheme) DarkCustomColors else LightCustomColors

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    val deviceUtils = DeviceUtils(
        screenWidth = screenWidth,
        screenHeight = screenHeight,
        isPortrait = isPortrait,
        isLandscape = !isPortrait,
        isDarkTheme = isDarkTheme
    )



    CompositionLocalProvider(
        LocalCustomColors provides colors,
        LocalDeviceUtils provides deviceUtils,
        LocalCustomTypography provides provideCustomTypography(isDarkTheme),
        content = content
    )

}