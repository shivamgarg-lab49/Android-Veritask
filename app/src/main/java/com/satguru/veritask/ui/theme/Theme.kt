package com.satguru.veritask.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

val Typography.fcl_title1: TextStyle
    get() = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 56.sp,
        lineHeight = 56.sp,
        letterSpacing = 0.0.sp
    )
val Typography.fcl_title2: TextStyle
    get() = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 48.sp,
        lineHeight = 48.sp,
        letterSpacing = 0.0.sp
    )
val Typography.fcl_title3: TextStyle
    get() = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 40.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.0.sp
    )
val Typography.fcl_title4: TextStyle
    get() = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.0.sp
    )
val Typography.fcl_title5: TextStyle
    get() = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.0.sp
    )
val Typography.fcl_title6: TextStyle
    get() = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.0.sp
    )
val Typography.fcl_title7: TextStyle
    get() = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.0.sp
    )
val Typography.fcl_title8: TextStyle
    get() = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.0.sp
    )
val Typography.fcl_body1: TextStyle
    get() = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.0.sp
    )
val Typography.fcl_body2: TextStyle
    get() = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.0.sp
    )
val Typography.fcl_body3: TextStyle
    get() = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 12.sp,
        letterSpacing = 0.0.sp
    )
val Typography.fcl_caption1: TextStyle
    get() = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        lineHeight = 10.sp,
        letterSpacing = 0.0.sp
    )
val Typography.fcl_caption2: TextStyle
    get() = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 8.sp,
        lineHeight = 8.sp,
        letterSpacing = 0.0.sp
    )

val Colors.fcl_fill_page: Color
    get() = if (isLight) Color(0xff171A1C) else Color(0xff171A1C)

val Colors.fcl_fill_container: Color
    get() = if (isLight) Color(0xff1B2124) else Color(0xff1B2124)

val Colors.fcl_fill_component: Color
    get() = if (isLight) Color(0xff3A4348) else Color(0xff3A4348)

// Content
val Colors.fcl_content: Color
    get() = if (isLight) Color(0xffDCEAF5) else Color(0xffDCEAF5)

val Colors.fcl_content_plus: Color
    get() = if (isLight) Color(0xffACBAC5) else Color(0xffACBAC5)

val Colors.fcl_content_subtle: Color
    get() = if (isLight) Color(0xff7C8A94) else Color(0xff7C8A94)

val Colors.fcl_content_dark: Color
    get() = if (isLight) Color(0xff5E676E) else Color(0xff5E676E)

val Colors.fcl_primary_100: Color
    get() = if (isLight) Color(0xff8FEBFA) else Color(0xff8FEBFA)

val Colors.fcl_primary_300: Color
    get() = if (isLight) Color(0xff24CAEC) else Color(0xff24CAEC)

val Colors.fcl_primary_500: Color
    get() = if (isLight) Color(0xff08ADD2) else Color(0xff08ADD2)

val Colors.fcl_primary_700: Color
    get() = if (isLight) Color(0xff0A8AB0) else Color(0xff0A8AB0)

val Colors.fcl_primary_900: Color
    get() = if (isLight) Color(0xff0A8AB0) else Color(0xff0A8AB0)

val Colors.fcl_secondary_100: Color
    get() = if (isLight) Color(0xffFFD1C8) else Color(0xffFFD1C8)

val Colors.fcl_secondary_300: Color
    get() = if (isLight) Color(0xffFFB1A1) else Color(0xffFFB1A1)

val Colors.fcl_secondary_500: Color
    get() = if (isLight) Color(0xffF86F53) else Color(0xffF86F53)

val Colors.fcl_secondary_700: Color
    get() = if (isLight) Color(0xffC03215) else Color(0xffC03215)

val Colors.fcl_secondary_900: Color
    get() = if (isLight) Color(0xff842A18) else Color(0xff842A18)

val Colors.fcl_neutral_100: Color
    get() = if (isLight) Color(0xffF2F2F2) else Color(0xffF2F2F2)

val Colors.fcl_neutral_300: Color
    get() = if (isLight) Color(0xff808080) else Color(0xff808080)

val Colors.fcl_neutral_500: Color
    get() = if (isLight) Color(0xff4D4D4D) else Color(0xff4D4D4D)

val Colors.fcl_neutral_700: Color
    get() = if (isLight) Color(0xff2E363A) else Color(0xff2E363A)

val Colors.fcl_neutral_900: Color
    get() = if (isLight) Color(0xff171A1C) else Color(0xff171A1C)

val Colors.fcl_status_warn_300: Color
    get() = if (isLight) Color(0xffF86F53) else Color(0xffF86F53)

val Colors.fcl_status_warn_500: Color
    get() = if (isLight) Color(0xffA14F3E) else Color(0xffA14F3E)

val Colors.fcl_status_warn_900: Color
    get() = if (isLight) Color(0xff4A2E29) else Color(0xff4A2E29)

val Colors.fcl_status_normal_300: Color
    get() = if (isLight) Color(0xff8FEBFA) else Color(0xff8FEBFA)

val Colors.fcl_status_normal_500: Color
    get() = if (isLight) Color(0xff488596) else Color(0xff488596)

val Colors.fcl_status_normal_900: Color
    get() = if (isLight) Color(0xff196066) else Color(0xff196066)

val Colors.fcl_status_success_300: Color
    get() = if (isLight) Color(0xff14B89C) else Color(0xff14B89C)

val Colors.fcl_status_success_500: Color
    get() = if (isLight) Color(0xff178270) else Color(0xff178270)

val Colors.fcl_status_success_900: Color
    get() = if (isLight) Color(0xff0A5C4E) else Color(0xff0A5C4E)

@Composable
fun VeriTaskTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = when {
        darkTheme -> {
            darkColors()
        }

        else -> lightColors()
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.fcl_fill_container.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colors = colors,
        typography = Typography(),
        content = content
    )
}