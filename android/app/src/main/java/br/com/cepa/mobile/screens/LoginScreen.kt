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
fun LoginScreen(
    initialEmail: String,
    notice: String,
    onCreateAccount: () -> Unit,
    onEnter: (String) -> Unit,
) {
    var email by rememberSaveable(initialEmail) { mutableStateOf(initialEmail) }
    var password by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().background(Cream).safeDrawingPadding().verticalScroll(rememberScrollState()).padding(24.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        BrandMark()
        Spacer(Modifier.height(42.dp))
        Text("BEM-VINDO À SUA ADEGA", color = Rose, fontSize = 11.sp, letterSpacing = 1.6.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        Text("O vinho certo, para você.", color = Wine, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 36.sp, lineHeight = 39.sp)
        Spacer(Modifier.height(12.dp))
        Text("Entre para explorar uma seleção feita para o seu paladar.", color = Muted, fontSize = 14.sp)
        Spacer(Modifier.height(30.dp))

        Surface(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.surface, shadowElevation = 4.dp) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Acessar demonstração", color = Wine, fontFamily = FontFamily.Serif, fontSize = 23.sp, fontWeight = FontWeight.Bold)
                CepaTextField("E-mail", email, { email = it; error = "" }, isError = error.isNotEmpty(), keyboardType = KeyboardType.Email)
                CepaTextField("Senha", password, { password = it; error = "" }, isError = error.isNotEmpty(), password = true)
                if (error.isNotEmpty()) Text(error, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                if (notice.isNotEmpty()) Text(notice, color = Wine, fontSize = 12.sp)
                Spacer(Modifier.height(2.dp))
                CepaButton("Entrar na demonstração") {
                    error = when {
                        !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> "Informe um e-mail válido."
                        password.isBlank() -> "Informe uma senha para continuar."
                        else -> ""
                    }
                    if (error.isEmpty()) onEnter(email.trim())
                }
                TextButton(onClick = onCreateAccount, modifier = Modifier.fillMaxWidth()) {
                    Text("Criar conta demonstrativa", color = Wine, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        Spacer(Modifier.height(25.dp))
        Text("Protótipo acadêmico: o acesso não autentica usuários nem envia dados ao servidor.", color = Muted, fontSize = 12.sp)
    }
}
