package com.fixer.servicosapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fixer.servicosapp.data.Prestador

internal object FixerColors {
    val Navy = Color(0xFF103477)
    val Blue = Color(0xFF1765E8)
    val Background = Color(0xFFF4F6FA)
    val Ink = Color(0xFF172746)
    val Muted = Color(0xFF64748B)
    val Line = Color(0xFFE3E9F2)
    val Green = Color(0xFF087F5B)
}

internal enum class FixerSymbol(val data: String) {
    Search("M21 21l-5-5 M18 10a8 8 0 1 1-16 0a8 8 0 0 1 16 0"),
    Back("M19 12H5 M12 5l-7 7l7 7"),
    Arrow("M5 12h14 M12 5l7 7l-7 7"),
    Person("M16 7a4 4 0 1 1-8 0a4 4 0 0 1 8 0 M4 21v-2a8 8 0 0 1 16 0v2"),
    Exit("M9 4H4v16h5 M10 12h11 M17 8l4 4l-4 4"),
    Star("M12 3l2.8 5.7 6.2.9-4.5 4.4 1.1 6.2L12 17.3l-5.6 2.9 1.1-6.2L3 9.6l6.2-.9z"),
    Phone("M7 3l3 5-3 3c1.5 3 3 4.5 6 6l3-3 5 3c0 3-1 5-4 4C9 18 5 14 3 7 2 4 4 3 7 3z"),
    Chat("M21 11.5a9 9 0 0 1-13.5 7.8L3 21l1.7-4.5A9 9 0 1 1 21 11.5 M8 8c0 4 2 6 6 7l2-2-3-1-1 1-2-2 1-1-1-3z"),
    Tool("M14 3a6 6 0 0 0-7 7L3 17a2.8 2.8 0 0 0 4 4l7-7a6 6 0 0 0 7-7l-4 4-4-4z"),
    Check("M5 12l4 4L19 6"),
    Info("M21 12a9 9 0 1 1-18 0a9 9 0 0 1 18 0 M12 11v6 M12 7v.1");

    val vector: ImageVector by lazy {
        ImageVector.Builder(name, 24.dp, 24.dp, 24f, 24f).addPath(
            pathData = addPathNodes(data), fill = null, stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.7f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round
        ).build()
    }
}

@Composable
internal fun FixerIcon(symbol: FixerSymbol, description: String? = null, modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(symbol.vector, description, modifier.size(22.dp), tint)
}

@Composable
internal fun FixerTopBar(title: String? = null, onBack: (() -> Unit)? = null, onConta: (() -> Unit)? = null, onSair: (() -> Unit)? = null) {
    Surface(color = FixerColors.Navy, contentColor = Color.White) {
        Row(Modifier.fillMaxWidth().statusBarsPadding().heightIn(min = 64.dp).padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            if (onBack != null) IconButton(onClick = onBack) { FixerIcon(FixerSymbol.Back, "Voltar") }
            else Box(Modifier.padding(horizontal = 8.dp)) { FixerIcon(FixerSymbol.Tool) }
            Text(title ?: "FIXER", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, modifier = Modifier.weight(1f))
            if (onConta != null) IconButton(onClick = onConta) { FixerIcon(FixerSymbol.Person, "Abrir minha conta") }
            if (onSair != null) IconButton(onClick = onSair) { FixerIcon(FixerSymbol.Exit, "Sair da conta") }
        }
    }
}

@Composable
internal fun FixerBottomBar(contaSelecionada: Boolean, onExplorar: () -> Unit, onConta: () -> Unit) {
    NavigationBar(containerColor = Color.White, tonalElevation = 0.dp) {
        val cores = NavigationBarItemDefaults.colors(selectedIconColor = FixerColors.Blue, selectedTextColor = FixerColors.Blue,
            indicatorColor = Color(0xFFEAF2FF), unselectedIconColor = FixerColors.Muted, unselectedTextColor = FixerColors.Muted)
        NavigationBarItem(selected = !contaSelecionada, onClick = onExplorar,
            icon = { FixerIcon(FixerSymbol.Search) }, label = { Text("Explorar") }, colors = cores)
        NavigationBarItem(selected = contaSelecionada, onClick = onConta,
            icon = { FixerIcon(FixerSymbol.Person) }, label = { Text("Minha conta") }, colors = cores)
    }
}

@Composable
internal fun AvatarPrestador(prestador: Prestador, grande: Boolean = false) {
    val cor = when (prestador.categoria) {
        "Eletricista" -> Color(0xFF9A6200)
        "Encanador" -> FixerColors.Blue
        "Diarista" -> FixerColors.Green
        "Pintor" -> Color(0xFF8745B5)
        else -> FixerColors.Navy
    }
    Box(Modifier.size(if (grande) 88.dp else 60.dp).background(cor.copy(alpha = .10f), RoundedCornerShape(if (grande) 28.dp else 18.dp)), contentAlignment = Alignment.Center) {
        Text(prestador.nome.trim().split("\\s+".toRegex()).take(2).mapNotNull { it.firstOrNull()?.uppercaseChar() }.joinToString(""),
            color = cor, fontWeight = FontWeight.Bold, fontSize = if (grande) 30.sp else 22.sp)
    }
}

@Composable
internal fun AvaliacaoPrestador(prestador: Prestador) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        if (prestador.avaliacao != null) FixerIcon(FixerSymbol.Star, tint = Color(0xFF996600), modifier = Modifier.size(17.dp))
        Text(prestador.avaliacao?.let { "$it · nota ilustrativa" } ?: "Ainda sem avaliações", color = FixerColors.Muted, style = MaterialTheme.typography.labelMedium)
    }
}
