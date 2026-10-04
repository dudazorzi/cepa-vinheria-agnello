package br.com.cepa.mobile.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.cepa.mobile.data.Wine
import br.com.cepa.mobile.theme.Cream
import br.com.cepa.mobile.theme.Gold
import br.com.cepa.mobile.theme.Muted
import br.com.cepa.mobile.theme.Sand
import br.com.cepa.mobile.theme.Wine as WineColor
import java.text.NumberFormat
import java.util.Locale

@Composable
fun WineCard(wine: Wine, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(width = 112.dp, height = 170.dp)
                    .background(if (wine.darkLabel) WineColor else Sand),
                contentAlignment = Alignment.Center,
            ) {
                BottleArt(dark = wine.darkLabel)
            }
            Column(modifier = Modifier.weight(1f).padding(14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(wine.type.uppercase(), color = Gold, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Text(wine.name, color = WineColor, fontSize = 18.sp, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, lineHeight = 21.sp)
                Text(wine.region, color = Muted, fontSize = 11.sp)
                Text("Combina com ${wine.pairing.lowercase(Locale.forLanguageTag("pt-BR"))}", color = Muted, fontSize = 11.sp)
                Spacer(Modifier.height(2.dp))
                Text(NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR")).format(wine.priceInCents / 100.0), color = WineColor, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun BottleArt(dark: Boolean) {
    val outline = if (dark) Color(0xFFE8D5B9) else WineColor
    val fill = if (dark) Color(0xFF4B1829) else Cream
    Canvas(modifier = Modifier.size(width = 58.dp, height = 128.dp)) {
        val w = size.width
        val h = size.height
        drawRoundRect(outline, topLeft = Offset(w * .32f, 0f), size = Size(w * .36f, h * .30f), cornerRadius = CornerRadius(w * .03f))
        drawRoundRect(outline, topLeft = Offset(w * .08f, h * .22f), size = Size(w * .84f, h * .77f), cornerRadius = CornerRadius(w * .29f))
        drawRoundRect(fill, topLeft = Offset(w * .13f, h * .26f), size = Size(w * .74f, h * .68f), cornerRadius = CornerRadius(w * .23f))
        drawRect(Gold, topLeft = Offset(w * .13f, h * .53f), size = Size(w * .74f, h * .22f))
        drawRect(outline, topLeft = Offset(w * .23f, h * .61f), size = Size(w * .54f, h * .012f))
    }
}
