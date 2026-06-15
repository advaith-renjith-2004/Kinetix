package com.example.digitalsilhouette

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.digitalsilhouette.data.DefaultDataRepository
import com.example.digitalsilhouette.ui.login.LoginScreen
import com.example.digitalsilhouette.ui.main.MainScreen

@Composable
fun MainNavigation() {
  val context = LocalContext.current.applicationContext
  val repository = remember { DefaultDataRepository.getInstance(context) }
  val isLoggedIn by repository.isLoggedIn.collectAsStateWithLifecycle()

  key(isLoggedIn) {
    val startDestination = if (isLoggedIn) Main else Login
    val backStack = rememberNavBackStack(startDestination)

    NavDisplay(
      backStack = backStack,
      onBack = { backStack.removeLastOrNull() },
      entryProvider = entryProvider {
        entry<Login> {
          LoginScreen(
            onLoginSuccess = { email, name ->
              repository.loginUser(email, name)
            },
            modifier = Modifier.safeDrawingPadding().padding(16.dp)
          )
        }
        entry<Main> {
          MainScreen(
            onItemClick = { navKey -> backStack.add(navKey) },
            modifier = Modifier.safeDrawingPadding().padding(16.dp)
          )
        }
      }
    )
  }
}
