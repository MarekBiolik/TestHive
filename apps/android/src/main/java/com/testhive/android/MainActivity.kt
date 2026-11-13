package com.testhive.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.testhive.android.session.QuizViewModel
import com.testhive.android.theme.NeonGlassTheme
import com.testhive.android.theme.NeonGlassTokens
import com.testhive.android.ui.HomeScreen
import com.testhive.android.ui.SessionScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val app = application as TestHiveApp
        setContent {
            NeonGlassTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding(),
                    color = NeonGlassTokens.Canvas,
                ) {
                    val navController = rememberNavController()
                    val vm: QuizViewModel = viewModel(
                        factory = remember {
                            object : androidx.lifecycle.ViewModelProvider.Factory {
                                @Suppress("UNCHECKED_CAST")
                                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                                    return QuizViewModel(app.container) as T
                                }
                            }
                        },
                    )
                    val decks by vm.decks.collectAsState()
                    val selected by vm.selected.collectAsState()
                    val session by vm.session.collectAsState()

                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            HomeScreen(
                                decks = decks,
                                selectedIds = selected,
                                onToggle = vm::toggleDeck,
                                onStart = {
                                    vm.startSession()
                                    navController.navigate("session")
                                },
                            )
                        }
                        composable("session") {
                            SessionScreen(
                                state = session,
                                onRevealControls = vm::revealControls,
                                onToggleOption = vm::toggleOption,
                                onSetOrder = vm::setOrder,
                                onSetMatch = vm::setMatch,
                                onSetCloze = vm::setCloze,
                                onRevealKnew = vm::submitReveal,
                                onSubmit = vm::submit,
                                onNext = { vm.next() },
                                onExit = {
                                    vm.exitSession()
                                    navController.popBackStack("home", inclusive = false)
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
