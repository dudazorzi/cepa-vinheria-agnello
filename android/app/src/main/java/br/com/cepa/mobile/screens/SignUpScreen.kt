package br.com.cepa.mobile.screens

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.cepa.mobile.components.BrandMark
import br.com.cepa.mobile.components.CepaButton
import br.com.cepa.mobile.components.CepaTextField
import br.com.cepa.mobile.theme.Cream
import br.com.cepa.mobile.theme.Muted
import br.com.cepa.mobile.theme.Rose
import br.com.cepa.mobile.theme.Wine

@Composable
fun SignUpScreen(onBack: () -> Unit, onCreate: (String) -> Unit) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmation by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().background(Cream).safeDrawingPadding().verticalScroll(rememberScrollState()).padding(24.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        BrandMark()
        Spacer(Modifier.height(28.dp))
        Text("FAÇA PARTE DO CEPA", color = Rose, fontSize = 11.sp, letterSpacing = 1.6.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(10.dp))
        Text("Sua próxima descoberta começa aqui.", color = Wine, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 32.sp, lineHeight = 36.sp)
        Spacer(Modifier.height(23.dp))
        Surface(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.surface, shadowElevation = 4.dp) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(11.dp)) {
                Text("Criar conta", color = Wine, fontFamily = FontFamily.Serif, fontSize = 23.sp, fontWeight = FontWeight.Bold)
                CepaTextField("Nome completo", name, { name = it; error = "" })
                CepaTextField("E-mail", email, { email = it; error = "" }, keyboardType = KeyboardType.Email)
                CepaTextField("Senha", password, { password = it; error = "" }, password = true)
                CepaTextField("Confirmar senha", confirmation, { confirmation = it; error = "" }, password = true)
                if (error.isNotEmpty()) Text(error, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                CepaButton("Criar conta demonstrativa") {
                    error = when {
                        name.trim().length < 2 -> "Informe seu nome."
                        !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> "Informe um e-mail válido."
                        password.length < 6 -> "A senha deve ter pelo menos 6 caracteres."
                        password != confirmation -> "As senhas não coincidem."
                        else -> ""
                    }
                    if (error.isEmpty()) onCreate(email.trim())
                }
                TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                    Text("Já tenho acesso · Voltar", color = Wine, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        Text("Nesta fase, o cadastro valida a interface, mas não armazena contas.", color = Muted, fontSize = 12.sp)
    }
}
