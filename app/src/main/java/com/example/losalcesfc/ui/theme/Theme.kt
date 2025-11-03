package com.example.losalcesfc.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.losalcesfc.R


private val colorScheme = lightColorScheme(
    primary = AzulPrimary,
    onPrimary = Blanco,
    secondary = DoradoAccent,
    onSecondary = Color(0xFF1B1B1B),
    background = SidebarBg,
    onBackground = TextoAzul,
    surface = PanelBg,
    onSurface = TextoAzul,
    error = Error,
    onError = Blanco
)
val AppCornerRadius = 12.dp
val Poppins = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold),
)

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

)

@Composable
fun LosalcesfcTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
@Composable
fun SigaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(
            bodyLarge = Typography().bodyLarge.copy(fontFamily = Poppins),
            bodyMedium = Typography().bodyMedium.copy(fontFamily = Poppins),
            titleLarge = Typography().titleLarge.copy(fontFamily = Poppins, fontWeight = FontWeight.SemiBold),
            labelLarge = Typography().labelLarge.copy(fontFamily = Poppins, fontWeight = FontWeight.Medium),
        ),
        shapes = Shapes(
            extraSmall = MaterialTheme.shapes.extraSmall.copy(),
            small = MaterialTheme.shapes.small.copy(),
            medium = MaterialTheme.shapes.medium.copy(),
            large = MaterialTheme.shapes.large.copy(),
            extraLarge = MaterialTheme.shapes.extraLarge.copy()
        ),
        content = content
    )
}
@Composable
fun SigaBackgroundBrush(): Brush = Brush.verticalGradient(
    colors = listOf(SidebarBg, PanelBg)
)