package br.com.cepa.mobile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.CircleShape
import br.com.cepa.mobile.theme.Gold
import br.com.cepa.mobile.theme.Wine

@Composable
fun BrandMark(modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(9.dp)) {
        Box(
            modifier = Modifier.size(32.dp).background(Wine, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text("◊", color = Gold, fontSize = 24.sp, fontFamily = FontFamily.Serif)
        }
        Text("CEPA", color = Wine, fontSize = 25.sp, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold)
    }
}
