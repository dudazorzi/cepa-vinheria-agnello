package br.com.cepa.mobile.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.cepa.mobile.components.BrandMark
import br.com.cepa.mobile.components.WineCard
import br.com.cepa.mobile.data.demoWines
import br.com.cepa.mobile.theme.Cream
import br.com.cepa.mobile.theme.Gold
import br.com.cepa.mobile.theme.Muted
import br.com.cepa.mobile.theme.Rose
import br.com.cepa.mobile.theme.Sand
import br.com.cepa.mobile.theme.Wine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(displayName: String, onSignOut: () -> Unit) {
    var showCatalog by rememberSaveable { mutableStateOf(false) }
    var showQuiz by rememberSaveable { mutableStateOf(false) }
    var showAccount by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        containerColor = Cream,
        topBar = {
            TopAppBar(
                title = { BrandMark() },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Cream),
            )
        },
        bottomBar = {
            BottomAppBar(containerColor = Color.White) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    TextButton(onClick = { showCatalog = false }) {
                        Text("Início", color = if (!showCatalog) Wine else Muted, fontWeight = if (!showCatalog) FontWeight.Bold else FontWeight.Normal)
                    }
                    TextButton(onClick = { showCatalog = true }) {
                        Text("Catálogo", color = if (showCatalog) Wine else Muted, fontWeight = if (showCatalog) FontWeight.Bold else FontWeight.Normal)
                    }
                    TextButton(onClick = { showAccount = true }) { Text("Conta", color = Muted) }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showQuiz = true },
                containerColor = Wine,
                contentColor = Color.White,
                modifier = Modifier.semantics { contentDescription = "Descobrir meu vinho" },
            ) { Text("✦", fontSize = 24.sp) }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            if (!showCatalog) {
                item { WelcomeHero(displayName) }
                item {
                    Spacer(Modifier.height(8.dp))
                    Text("Escolhas para descobrir", color = Wine, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 25.sp)
                    Text("Uma seleção inicial da curadoria CEPA.", color = Muted, fontSize = 13.sp)
                }
                items(demoWines.take(3)) { wine -> WineCard(wine) }
                item {
                    TextButton(onClick = { showCatalog = true }, modifier = Modifier.fillMaxWidth()) {
                        Text("Ver catálogo completo →", color = Wine, fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                item {
                    Text("CATÁLOGO CEPA", color = Rose, fontSize = 11.sp, letterSpacing = 1.5.sp, fontWeight = FontWeight.Bold)
                    Text("Encontre seu próximo vinho", color = Wine, fontFamily = FontFamily.Serif, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                    Text("${demoWines.size} vinhos demonstrativos · escolha sem pressa", color = Muted, fontSize = 13.sp)
                }
                items(demoWines) { wine -> WineCard(wine) }
            }
        }
    }

    if (showQuiz) QuickPreferenceDialog(onDismiss = { showQuiz = false })
    if (showAccount) {
        AlertDialog(
            onDismissRequest = { showAccount = false },
            title = { Text("Minha conta", color = Wine, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold) },
            text = { Text("Você está explorando o CEPA como $displayName. Este acesso é demonstrativo e não armazena seus dados.") },
            confirmButton = { TextButton(onClick = { showAccount = false }) { Text("Continuar", color = Wine) } },
            dismissButton = { TextButton(onClick = onSignOut) { Text("Sair", color = Wine) } },
        )
    }
}

@Composable
private fun WelcomeHero(displayName: String) {
    Box(modifier = Modifier.fillMaxWidth().background(Wine, RoundedCornerShape(22.dp)).padding(24.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("CURADORIA PARA VOCÊ", color = Gold, fontSize = 10.sp, letterSpacing = 1.6.sp, fontWeight = FontWeight.Bold)
            Text("Olá, $displayName!", color = Color.White, fontFamily = FontFamily.Serif, fontSize = 29.sp, fontWeight = FontWeight.Bold)
            Text("O vinho certo, na condição certa, até você.", color = Color.White, fontFamily = FontFamily.Serif, fontSize = 21.sp, lineHeight = 26.sp)
            Text("Explore rótulos brasileiros e descubra combinações para cada ocasião.", color = Color(0xFFECDDE0), fontSize = 13.sp)
            Spacer(Modifier.height(4.dp))
            Text("✦  Toque no botão flutuante para uma indicação rápida", color = Gold, fontSize = 11.sp)
        }
    }
}

@Composable
private fun QuickPreferenceDialog(onDismiss: () -> Unit) {
    var selectedStyle by rememberSaveable { mutableStateOf("Leve") }
    val suggestion = demoWines.first { it.style == selectedStyle }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Descubra seu vinho", color = Wine, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Qual estilo combina com você hoje?", color = Muted)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Leve", "Encorpado", "Suave").forEach { style ->
                        FilterChip(selected = selectedStyle == style, onClick = { selectedStyle = style }, label = { Text(style, fontSize = 11.sp) })
                    }
                }
                Box(modifier = Modifier.fillMaxWidth().background(Sand, RoundedCornerShape(12.dp)).padding(14.dp)) {
                    Column {
                        Text("SUGESTÃO CEPA", color = Rose, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(4.dp))
                        Text(suggestion.name, color = Wine, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(suggestion.description, color = Muted, fontSize = 12.sp)
                    }
                }
                Text("Indicação local de demonstração; não é o quiz completo do site.", color = Muted, fontSize = 11.sp)
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Voltar ao catálogo", color = Wine) } },
    )
}
