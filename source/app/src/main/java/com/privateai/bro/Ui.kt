package com.privateai.bro

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** PRIVATE AI – BRO: midnight liquid-glass visual system. */
object Sig {
    val Paper = Color(0xFF080D1B)
    val Ink = Color(0xFFF4F7FF)
    val Pink = Color(0xFF101A31)
    val Red = Color(0xFFA78BFA)
    val Acid = Color(0xFF67E8F9)
    val Stage = Color(0xFF050914)
    val Grey = Color(0xFF9AA9C4)
    val Mute = Color(0xFF9AA9C4)
    val Glass = Color(0xFF18243B)
    val Violet = Color(0xFF8B5CF6)
}

fun bgFor(s: OrbState): Color = Sig.Paper
fun fgFor(s: OrbState): Color = Sig.Ink

fun wordFor(s: OrbState): String = when (s) {
    OrbState.IDLE -> "HEY BRO."
    OrbState.WAKE -> "HERE."
    OrbState.LISTENING -> "GO ON."
    OrbState.VERIFYING -> "HOLD ON."
    OrbState.THINKING -> "HMM."
    OrbState.EXECUTING -> "ON IT."
    OrbState.SPEAKING -> "LISTEN."
    OrbState.CONFIRM -> "SURE?"
    OrbState.ERROR -> "NOPE."
    OrbState.OFFLINE -> "OFFLINE."
    OrbState.LOCKED -> "LOCKED."
}

@Composable
fun Display(text: String, size: TextUnit, color: Color, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(), modifier = modifier, color = color,
        fontFamily = Fonts.display,
        fontWeight = if (Fonts.displayLoaded) FontWeight.Normal else FontWeight.Black,
        fontSize = size, lineHeight = size * 0.94f, letterSpacing = (-1).sp
    )
}

@Composable
fun Mono(text: String, size: TextUnit, color: Color, modifier: Modifier = Modifier, bold: Boolean = false) {
    Text(
        text = text, modifier = modifier, color = color,
        fontFamily = if (bold) Fonts.monoBold else Fonts.mono,
        fontWeight = if (bold && !Fonts.monoBoldLoaded) FontWeight.Bold else FontWeight.Normal,
        fontSize = size, lineHeight = size * 1.4f, letterSpacing = 0.15.sp
    )
}

@Composable
fun Tag(text: String, modifier: Modifier = Modifier, bg: Color = Sig.Red, fg: Color = Color.White) {
    val shape = RoundedCornerShape(50)
    Box(
        modifier.rotate(-1f).clip(shape)
            .background(bg.copy(alpha = 0.20f), shape)
            .border(1.dp, bg.copy(alpha = 0.52f), shape)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) { Mono(text, 10.sp, fg, bold = true) }
}

@Composable
fun HardCard(
    modifier: Modifier = Modifier,
    bg: Color = Color.White,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(24.dp)
    val tint = when (bg) {
        Color.White, Sig.Ink -> Color.White
        Sig.Red -> Sig.Violet
        Sig.Acid -> Sig.Acid
        else -> bg
    }
    val click = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    Box(
        modifier = modifier.then(click).shadow(18.dp, shape).clip(shape)
            .background(
                Brush.linearGradient(
                    listOf(tint.copy(alpha = 0.19f), Color(0xFF15233D).copy(alpha = 0.55f), tint.copy(alpha = 0.07f))
                )
            )
            .border(
                1.dp,
                Brush.linearGradient(listOf(Color.White.copy(alpha = 0.38f), Color.White.copy(alpha = 0.07f))),
                shape
            )
            .padding(16.dp),
        content = content
    )
}

@Composable
fun BlockButton(
    label: String,
    modifier: Modifier = Modifier,
    bg: Color = Sig.Ink,
    fg: Color = Sig.Acid,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(18.dp)
    Box(
        modifier = modifier.shadow(12.dp, shape).clip(shape)
            .background(Brush.linearGradient(listOf(Sig.Violet.copy(alpha = 0.38f), Sig.Acid.copy(alpha = 0.12f))))
            .border(1.dp, Color.White.copy(alpha = 0.30f), shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 15.dp),
        contentAlignment = Alignment.Center
    ) { Mono(label.uppercase(), 12.sp, if (bg == Sig.Red) Color.White else fg, bold = true) }
}

@Composable
fun BottomBar(tabs: List<String>, selected: Int, onSelect: (Int) -> Unit) {
    val shape = RoundedCornerShape(26.dp)
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp)
            .shadow(20.dp, shape).clip(shape).background(Color(0xE6101A2D))
            .border(1.dp, Color.White.copy(alpha = 0.18f), shape)
            .horizontalScroll(rememberScrollState()).padding(6.dp)
    ) {
        tabs.forEachIndexed { i, label ->
            val active = i == selected
            val itemShape = RoundedCornerShape(20.dp)
            Box(
                Modifier.width(82.dp).clip(itemShape)
                    .background(
                        if (active) Brush.linearGradient(listOf(Sig.Acid.copy(alpha = 0.95f), Sig.Violet.copy(alpha = 0.88f)))
                        else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
                    )
                    .clickable { onSelect(i) }.padding(vertical = 13.dp),
                contentAlignment = Alignment.Center
            ) {
                Mono(label, 10.sp, if (active) Color(0xFF07111F) else Color.White.copy(alpha = 0.76f), bold = active)
            }
        }
    }
}
