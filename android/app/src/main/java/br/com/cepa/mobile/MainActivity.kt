package br.com.cepa.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import br.com.cepa.mobile.screens.HomeScreen
import br.com.cepa.mobile.screens.LoginScreen
import br.com.cepa.mobile.screens.SignUpScreen
import br.com.cepa.mobile.theme.CepaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CepaTheme {
                var screen by rememberSaveable { mutableStateOf("login") }
                var email by rememberSaveable { mutableStateOf("") }
                var displayName by rememberSaveable { mutableStateOf("Visitante") }
                var notice by rememberSaveable { mutableStateOf("") }

                BackHandler(enabled = screen != "login") {
                    screen = "login"
                }

                when (screen) {
                    "signup" -> SignUpScreen(
                        onBack = { screen = "login" },
                        onCreate = { newEmail ->
                            email = newEmail
                            notice = "Cadastro demonstrativo concluído. Nenhuma conta foi salva."
                            screen = "login"
                        },
                    )
                    "home" -> HomeScreen(
                        displayName = displayName,
                        onSignOut = {
                            notice = "Você saiu da demonstração."
                            screen = "login"
                        },
                    )
                    else -> LoginScreen(
                        initialEmail = email,
                        notice = notice,
                        onCreateAccount = { screen = "signup" },
                        onEnter = { enteredEmail ->
                            email = enteredEmail
                            displayName = enteredEmail.substringBefore('@').replaceFirstChar { it.uppercase() }
                            notice = ""
                            screen = "home"
                        },
                    )
                }
            }
        }
    }
}
