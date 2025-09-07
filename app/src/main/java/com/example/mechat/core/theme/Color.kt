package com.novo.core.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

//val Purple80 = Color(0xFFD0BCFF)
//val PurpleGrey80 = Color(0xFFCCC2DC)
//val Pink80 = Color(0xFFEFB8C8)
//
//val Purple40 = Color(0xFF6650a4)
//val PurpleGrey40 = Color(0xFF625b71)
//val Pink40 = Color(0xFF7D5260)


data class CustomColors(
    val success: Color,
    val warning: Color,
    val info: Color,
    val neutralBackground: Color,
    val onSuccess: Color,
    val onWarning: Color,
    val onInfo: Color,
)

val LightCustomColors = CustomColors(
    success = Color(0xFF4CAF50),
    warning = Color(0xFFFFC107),
    info = Color(0xFF2196F3),
    neutralBackground = Color(0xFFF5F5F5),
    onSuccess = Color.White,
    onWarning = Color.Black,
    onInfo = Color.White
)

 val DarkCustomColors = CustomColors(
    success = Color(0xFF81C784),
    warning = Color(0xFFFFD54F),
    info = Color(0xFF64B5F6),
    neutralBackground = Color(0xFF303030),
    onSuccess = Color.Black,
    onWarning = Color.Black,
    onInfo = Color.Black
)

val LocalCustomColors = staticCompositionLocalOf<CustomColors> {
    error("No CustomColors provided")
}